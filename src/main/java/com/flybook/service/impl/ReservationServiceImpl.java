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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public ReservationDTOResponse createReservation(ReservationDTORequest reservationDTORequest) {
        Client client = clientService.getClientForReservation(reservationDTORequest);
        return finaliserReservation(reservationDTORequest, client);
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

        Reservation reservation = reservationRepository.save(createdReservation);

        List<Profile> profiles = new ArrayList<>();
        for (ProfilDTORequest profilDTORequest : reservationDTORequest.getProfilDTORequestList()) {
            Profile profile = ProfilMapper.INSTANCE.profilDTORequestToProfilEntity(profilDTORequest);
            profile.setReservation(reservation);
            profiles.add(profilRepository.save(profile));
        }
        reservation.setProfiles(profiles);
        reservation.setPriceOfReservation(getTotalPriceOfReservation(flight.getPrice(), profiles, reservationDTORequest.getCurrency()));

        return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(reservation);
    }

    private double getTotalPriceOfReservation(double singleFlightPrice, List<Profile> profiles, String currency) {
        Map<String, Double> currencies = currencyService.getCurrencies();
        double priceOfAllLuggage = profiles.stream().mapToInt(Profile::getNbLuggage).sum() * 100;
        double totalFlightPrice = singleFlightPrice * profiles.size();

        if (profiles.size() >= 4) {
            double discountValue = getDiscountValue(singleFlightPrice, profiles);
            totalFlightPrice -= discountValue;
        }

        return (totalFlightPrice + priceOfAllLuggage) * currencies.get(currency);
    }

    private double getDiscountValue(double singleFlightPrice, List<Profile> profiles) {
        long amountOfChildren = profiles.stream()
                .filter(this::isChild)
                .count();

        return Long.min(amountOfChildren, 2) * singleFlightPrice / 2;
    }

    private boolean isChild(Profile profile) {
        return profile.getBirthday().isBefore(LocalDate.now().minusYears(15));
    }
}
