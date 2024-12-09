package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.ProfilMapper;
import com.flybook.mapper.ReservationMapper;
import com.flybook.model.dto.request.ProfilDTORequest;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.model.entity.Client;
import com.flybook.model.entity.Flight;
import com.flybook.model.entity.Profil;
import com.flybook.model.entity.Reservation;
import com.flybook.model.parse.Currency;
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

        List<Profil> profiles = new ArrayList<>();
        for (ProfilDTORequest profilDTORequest : reservationDTORequest.getProfilDTORequestList()) {
            Profil profil = ProfilMapper.INSTANCE.profilDTORequestToProfilEntity(profilDTORequest);
            profil.setReservation(reservation);
            profiles.add(profilRepository.save(profil));
        }
        reservation.setProfils(profiles);
        reservation.setPriceOfReservation(setTotalPriceOfReservation(flight.getPrice(), profiles, reservationDTORequest.getCurrency()));

        return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(reservation);
    }

    private double setTotalPriceOfReservation(double flightPrice, List<Profil> profiles, String currency) {
        Map<String, Double> currencies = currencyService.getCurrencies();
        double totalPrice = flightPrice * profiles.size();
        isDiscountAvailable();
        double totalPriceOfReservation = 0;
        int nbReduction;
        if (profiles.size() >= 4) {
            nbReduction = 2;
        } else {
            nbReduction = 0;
        }

        for (Profil profile : profiles) {
            if (isChild(profile) && nbReduction != 0) {
                nbReduction--;
                totalPriceOfReservation = totalPriceOfReservation + (flightPrice/2) + (profile.getNbLuggage() * (100 * currencies.get(currency)));
            } else {
                totalPriceOfReservation = totalPriceOfReservation + flightPrice + (profile.getNbLuggage() * (100 * currencies.get(currency)));
            }
        }

        return totalPriceOfReservation;
    }

    private boolean isChild(Profil profile) {
        return profile.getBirthday().isBefore(LocalDate.now().minusYears(15));
    }
}
