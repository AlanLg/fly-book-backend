package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.ReservationMapper;
import com.flybook.model.dto.request.ReservationDTORequest;
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

        return ReservationMapper.INSTANCE.reservationEntityToReservationDTOResponse(reservation);
    }
}
