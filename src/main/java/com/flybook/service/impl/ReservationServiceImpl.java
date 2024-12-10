package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.ProfilMapper;
import com.flybook.mapper.ReservationMapper;
import com.flybook.model.dto.request.ProfilDTORequest;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.model.entity.Client;
import com.flybook.model.entity.Flight;
import com.flybook.model.entity.Profile;
import com.flybook.model.entity.Reservation;
import com.flybook.repository.ProfilRepository;
import com.flybook.repository.ReservationRepository;
import com.flybook.service.ReservationService;
import com.flybook.utils.ReservationValidationUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientServiceImpl clientService;
    private final FlightServiceImpl flightService;
    private final ProfilRepository profilRepository;
    private final CurrencyServiceImpl currencyService;
    @Getter
    private final Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public ReservationDTOResponse createReservation(ReservationDTORequest reservationDTORequest, String email) {
        Client client = clientService.getClientForReservation(email);
        return finaliserReservation(reservationDTORequest, client);
    }

    @Override
    public List<ReservationDTOResponse> getAllReservationsForClient(String clientEmail) {
        return ReservationMapper.INSTANCE
                .reservationEntityToReservationDTOResponse(reservationRepository.findByClient_Email(clientEmail));
    }

    private ReservationDTOResponse finaliserReservation(ReservationDTORequest reservationDTORequest, Client client) {
        Flight flight = flightService.getFlightForReservation(reservationDTORequest);

        int numberOfSeatsForFlight = reservationRepository.countDistinctByFlightAndDepartureDate(flight, reservationDTORequest.getDepartureDate());
        if (numberOfSeatsForFlight == flight.getNumberOfSeats()) {
            throw new FlybookException("The flight is full", HttpStatus.CONFLICT);
        }

        Reservation createdReservation = ReservationMapper.INSTANCE.clientEntityAndFlightEntityAndReservationDTORequestToReservationEntity(client, flight, reservationDTORequest);
        if (!ReservationValidationUtils.isValidReservation(createdReservation)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Optional<Reservation> existingReservation = reservationRepository.findByFlight_FlightIdAndClient_Id(createdReservation.getFlight().getFlightId(), createdReservation.getClient().getId());

        if (existingReservation.isPresent()) {
            return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(existingReservation.get());
        }

        List<Profile> profiles = ProfilMapper.INSTANCE.profilDTORequestListToProfilListEntity(reservationDTORequest.getProfilDTORequestList());
        createdReservation.setPriceOfReservation(getTotalPriceOfReservation(flight.getPrice(), profiles, reservationDTORequest.getCurrency()));
        createdReservation.setNbLuggage(profiles.stream().mapToInt(Profile::getNbLuggage).sum());
        createdReservation = reservationRepository.save(createdReservation);

        for (Profile profile : profiles) {
            profile.setReservation(createdReservation);
            profilRepository.save(profile);
        }

        createdReservation.setProfiles(profiles);

        Sinks.EmitResult result = sink.tryEmitNext("Hello World" + createdReservation.getId());
        if (result.isFailure()) {
            log.error("Failed to push event");
        }
        return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(createdReservation);
    }

    private double getTotalPriceOfReservation(double singleFlightPrice, List<Profile> profiles, String currency) {
        Map<String, Double> currencies = currencyService.getCurrencies();
        double priceOfAllLuggage = profiles.stream().mapToInt(Profile::getNbLuggage).sum() * 100;
        double totalFlightPrice = singleFlightPrice * profiles.size();
        long amountOfChildren = profiles.stream()
                .filter(this::isChild)
                .count();
        long amountOfAdult = profiles.size() - amountOfChildren;

        if (profiles.size() >= 4 && amountOfChildren >= 2 && amountOfAdult >= 2) {
            totalFlightPrice -= singleFlightPrice;
        }

        return (totalFlightPrice + priceOfAllLuggage) * currencies.get(currency);
    }

    private boolean isChild(Profile profile) {
        return profile.getBirthday().isBefore(LocalDate.now().minusYears(15));
    }

    @Override
    public Long getNumberOfReservationsInRealTime() throws FlybookException {
        return reservationRepository.countAllByCreationDateIsAfter(LocalDateTime.now());
    }
}
