package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.AirportMapper;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import com.flybook.model.entity.Airport;
import com.flybook.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AirportServiceImplTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportServiceImpl airportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAirportWhenGetAirportIsCalledWithValidId() throws FlybookException {
        Long id = 1L;
        Airport airport = new Airport();
        when(airportRepository.findById(id)).thenReturn(Optional.of(airport));
        AirportDTOResponse expectedResponse = AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(airport);

        AirportDTOResponse response = airportService.getAirport(id);

        assertEquals(expectedResponse, response);
        verify(airportRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenGetAirportIsCalledWithNullId() {
        assertThrows(FlybookException.class, () -> airportService.getAirport(null));
        verify(airportRepository, never()).findById(any());
    }

    @Test
    void shouldThrowExceptionWhenGetAirportIsCalledWithNonExistingId() {
        Long id = -1L;
        when(airportRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlybookException.class, () -> airportService.getAirport(id));
        verify(airportRepository, times(1)).findById(id);
    }

    @Test
    void testAddAirport_Success() throws FlybookException {
        // Arrange
        AirportDTORequest airportDTORequest = new AirportDTORequest();
        airportDTORequest.setAirportName("CDG");
        Airport createdAirport = new Airport();
        AirportDTOResponse expectedResponse = new AirportDTOResponse();
        when(airportRepository.findByAirportName(anyString())).thenReturn(Optional.empty());
        when(airportRepository.save(any())).thenReturn(createdAirport);

        // Act
        AirportDTOResponse response = airportService.addAirport(airportDTORequest);

        // Assert
        assertNotNull(response);
    }

    @Test
    void testUpdateAirport_Success() throws FlybookException {
        // Arrange
        Long id = 1L;
        AirportDTORequest airportDTORequest = new AirportDTORequest();
        airportDTORequest.setAirportName("CDG");
        Airport updatedAirport = new Airport();
        updatedAirport.setAirportId(id);
        when(airportRepository.findById(id)).thenReturn(Optional.of(updatedAirport));
        when(airportRepository.save(any())).thenReturn(updatedAirport);

        // Act
        AirportDTOResponse response = airportService.updateAirport(id, airportDTORequest);

        // Assert
        assertNotNull(response);
        assertEquals("CDG", response.getAirportName());
    }

    @Test
    void testDeleteAirport_Success() throws FlybookException {
        // Arrange
        Long id = 1L;
        Airport airport = new Airport();
        when(airportRepository.findById(id)).thenReturn(Optional.of(airport));

        // Act
        airportService.deleteAirport(id);

        // Assert
        verify(airportRepository, times(1)).delete(airport);
    }

    @Test
    void testDeleteAirport_Failure_IdNull() {
        // Act & Assert
        assertThrows(FlybookException.class, () -> airportService.deleteAirport(null));
    }

    @Test
    void testDeleteAirport_Failure_AirportNotFound() {
        // Arrange
        Long id = 1L;
        when(airportRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FlybookException.class, () -> airportService.deleteAirport(id));
    }
}
