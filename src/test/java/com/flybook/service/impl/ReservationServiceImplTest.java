package com.flybook.service.impl;

import com.flybook.model.dto.request.ReservationDTORequestWithExistingClient;
import com.flybook.model.entity.Client;
import com.flybook.model.entity.Flight;
import com.flybook.repository.ReservationRepository;
import com.flybook.service.ClientService;
import com.flybook.service.FlightService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceImplTest {

    private final static EasyRandom generator = new EasyRandom();

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createReservationWithExistingClientSuccessfully() {
        ReservationDTORequestWithExistingClient request = generator.nextObject(ReservationDTORequestWithExistingClient.class);
        Client client = generator.nextObject(Client.class);
        Flight flight = generator.nextObject(Flight.class);

        when(clientService.getClientForReservation(request)).thenReturn(client);
        when(flightService.getFlightForReservation(request)).thenReturn(flight);
        when(reservationRepository.findByFlight_FlightIdAndClient_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> reservationService.createReservation(request));
    }
}