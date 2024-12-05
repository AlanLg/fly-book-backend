package com.flybook.controller;

import com.flybook.exception.GalacticsAirlinesException;
import com.flybook.model.dto.request.FlightDTORequest;
import com.flybook.model.dto.response.FlightDTOResponse;
import com.flybook.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnFlightWhenGetFlightIsCalledWithValidId() throws GalacticsAirlinesException {
        Long id = 1L;
        FlightDTOResponse expectedResponse = new FlightDTOResponse();
        when(flightService.getFlight(id)).thenReturn(expectedResponse);

        ResponseEntity<FlightDTOResponse> response = flightController.getFlight(id);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(flightService, times(1)).getFlight(id);
    }

    @Test
    void shouldThrowExceptionWhenGetFlightIsCalledWithInvalidId() throws GalacticsAirlinesException {
        Long id = -1L;
        when(flightService.getFlight(id)).thenThrow(new GalacticsAirlinesException("Invalid id"));

        assertThrows(GalacticsAirlinesException.class, () -> flightController.getFlight(id));
        verify(flightService, times(1)).getFlight(id);
    }

    @Test
    void testAddFlight_Success() throws GalacticsAirlinesException {
        // Arrange
        FlightDTORequest request = new FlightDTORequest();
        FlightDTOResponse expectedResponse = new FlightDTOResponse();
        when(flightService.addFlight(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<FlightDTOResponse> response = flightController.addFlight(request);

        // Assert
        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        // Verify that flightService.addFlight was called once with the correct parameter
        verify(flightService, times(1)).addFlight(request);
    }

    @Test
    void testUpdateFlight_Success() throws GalacticsAirlinesException {
        // Arrange
        Long id = 1L;
        FlightDTORequest request = new FlightDTORequest();
        FlightDTOResponse expectedResponse = new FlightDTOResponse();
        when(flightService.updateFlight(id, request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<FlightDTOResponse> response = flightController.updateFlight(id, request);

        // Assert
        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());

        // Verify that flightService.updateFlight was called once with the correct parameters
        verify(flightService, times(1)).updateFlight(id, request);
    }

    @Test
    void testDeleteFlight_Success() throws GalacticsAirlinesException {
        // Arrange
        Long id = 1L;

        // Act
        ResponseEntity<Void> response = flightController.deleteFlight(id);

        // Assert
        assertEquals(202, response.getStatusCodeValue());

        // Verify that flightService.deleteFlight was called once with the correct parameter
        verify(flightService, times(1)).deleteFlight(id);
    }

    @Test
    void testDeleteFlight_Failure() throws GalacticsAirlinesException {
        // Arrange
        Long id = 1L;
        doThrow(new GalacticsAirlinesException("Flight not found")).when(flightService).deleteFlight(id);

        // Act
        ResponseEntity<Void> response = flightController.deleteFlight(id);

        // Assert
        assertEquals(400, response.getStatusCodeValue());

        // Verify that flightService.deleteFlight was called once with the correct parameter
        verify(flightService, times(1)).deleteFlight(id);
    }
}
