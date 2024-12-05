package com.flybook.controller;
import com.flybook.exception.GalacticsAirlinesException;
import com.flybook.model.dto.request.ReservationDTORequestWithExistingClient;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addReservationWithExistingClientReturnsReservationResponseSuccessfully() throws GalacticsAirlinesException {
        ReservationDTORequestWithExistingClient request = new ReservationDTORequestWithExistingClient();
        ReservationDTOResponse expectedResponse = new ReservationDTOResponse();
        when(reservationService.createReservation(request)).thenReturn(expectedResponse);

        ResponseEntity<ReservationDTOResponse> response = reservationController.addReservationWithExistingClient(request);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(reservationService, times(1)).createReservation(request);
    }
}