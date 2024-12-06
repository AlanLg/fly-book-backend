package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.ReservationMapper;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.request.ReservationDTORequestWithExistingClient;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.model.entity.Client;
import com.flybook.model.entity.Flight;
import com.flybook.model.entity.Reservation;
import com.flybook.repository.ReservationRepository;
import com.flybook.service.ClientService;
import com.flybook.service.FlightService;
import com.flybook.service.ReservationService;
import com.flybook.utils.ReservationValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientService clientService;
    private final FlightService flightService;

    @Override
    public ReservationDTOResponse createReservation(ReservationDTORequestWithExistingClient reservationDTORequest) {
        log.info("Création d'une réservation avec un client existant");
        Client client = clientService.getClientForReservation(reservationDTORequest);
        log.info("Client trouvé : {}", client);
        return finaliserReservation(reservationDTORequest, client);
    }

    private ReservationDTOResponse finaliserReservation(ReservationDTORequest reservationDTORequest, Client client) {
        log.info("Chercher un vol");
        Flight flight = flightService.getFlightForReservation(reservationDTORequest);
        log.info("Vol trouvé");

        int numberOfSeatsForFlight = reservationRepository.countDistinctByFlightAndDepartureDate(flight, reservationDTORequest.getDepartureDate());
        if (numberOfSeatsForFlight == flight.getNumberOfSeats()) {
            throw new FlybookException("Il n'y a plus de place pour ce vol", HttpStatus.CONFLICT);
        }

        Reservation createdReservation = ReservationMapper.INSTANCE.clientEntityAndFlightEntityToReservationEntity(client, flight, reservationDTORequest.getDepartureDate());
        log.info("mapping de la réservation");

        if (!ReservationValidationUtils.isValidReservation(createdReservation)) {
            throw new FlybookException("Il manque un élément dans la réservation", HttpStatus.BAD_REQUEST);
        }

        Optional<Reservation> existingReservation = reservationRepository
                .findByFlight_FlightIdAndClient_Id(createdReservation.getFlight().getFlightId(), createdReservation.getClient().getId());
        log.info("recherche de la réservation existante");

        if (existingReservation.isPresent()) {
            log.info("trouve un reservation existante");
            return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(existingReservation.get());
        }

        log.info("sauvegarde de la reservation creee");
        Reservation reservation = reservationRepository.save(createdReservation);

        return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(reservation);
    }
}
