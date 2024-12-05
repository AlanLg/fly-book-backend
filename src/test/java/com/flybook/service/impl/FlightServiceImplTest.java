package com.flybook.service.impl;

import com.flybook.exception.GalacticsAirlinesException;
import com.flybook.mapper.FlightMapper;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.request.FilterFlightDTORequest;
import com.flybook.model.dto.request.FlightDTORequest;
import com.flybook.model.dto.response.FlightDTOResponse;
import com.flybook.model.entity.Airport;
import com.flybook.model.entity.Flight;
import com.flybook.repository.FlightRepository;
import com.flybook.repository.ReservationRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FlightServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportServiceImpl airportService;

    private static final EasyRandom generator = new EasyRandom();

    @InjectMocks
    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnFlightWhenGetFlightIsCalledWithValidId() throws GalacticsAirlinesException {
        Long id = 1L;
        Flight flight = new Flight();
        when(flightRepository.findById(id)).thenReturn(Optional.of(flight));
        FlightDTOResponse expectedResponse = FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(flight);

        FlightDTOResponse response = flightService.getFlight(id);

        assertEquals(expectedResponse, response);
        verify(flightRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenGetFlightIsCalledWithNullId() {
        assertThrows(GalacticsAirlinesException.class, () -> flightService.getFlight(null));
        verify(flightRepository, never()).findById(any());
    }

    @Test
    void shouldThrowExceptionWhenGetFlightIsCalledWithNonExistingId() {
        Long id = -1L;
        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(GalacticsAirlinesException.class, () -> flightService.getFlight(id));
        verify(flightRepository, times(1)).findById(id);
    }

    @Test
    void testAddFlight_Success() throws GalacticsAirlinesException {
        AirportDTORequest departureAirport = new AirportDTORequest();
        departureAirport.setAirportName("TestAirportName");

        AirportDTORequest arrivalAirport = new AirportDTORequest();
        arrivalAirport.setAirportName("TestAirportName2");

        FlightDTORequest request = new FlightDTORequest();
        request.setDepartureAirport(departureAirport);
        request.setArrivalAirport(arrivalAirport);

        Flight flight = new Flight();

        when(flightRepository.findByDepartureAirport_AirportNameAndArrivalAirport_AirportName(
                any(), any())).thenReturn(Optional.empty());
        when(flightRepository.save(any())).thenReturn(flight);
        when(airportService.findOrSaveAirport(any())).thenReturn(new Airport());

        FlightDTOResponse response = flightService.addFlight(request);

        assertNotNull(response);
    }

    @Test
    void testUpdateFlight_Success() throws GalacticsAirlinesException {
        Long id = 1L;
        AirportDTORequest airport = new AirportDTORequest();
        airport.setAirportName("TestAirportName");

        FlightDTORequest request = new FlightDTORequest();
        request.setDepartureAirport(airport);
        request.setArrivalAirport(airport);

        Flight flight = new Flight();

        when(flightRepository.findById(id)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any())).thenReturn(flight);
        when(airportService.findOrSaveAirport(any())).thenReturn(new Airport());

        FlightDTOResponse response = flightService.updateFlight(id, request);

        assertNotNull(response);
    }

    @Test
    void testDeleteFlight_Success() throws GalacticsAirlinesException {
        Long id = 1L;
        Flight flight = new Flight();
        when(flightRepository.findById(id)).thenReturn(Optional.of(flight));
        when(reservationRepository.findByFlight_FlightId(any())).thenReturn(Optional.empty());

        flightService.deleteFlight(id);

        verify(flightRepository, times(1)).delete(flight);
    }

    @Test
    void testDeleteFlight_Failure_IdNull() {
        assertThrows(GalacticsAirlinesException.class, () -> flightService.deleteFlight(null));
    }

    @Test
    void testDeleteFlight_Failure_FlightNotFound() {
        Long id = 1L;
        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(GalacticsAirlinesException.class, () -> flightService.deleteFlight(id));
    }

    @Test
    void testSearchFlight_NoFilter() throws GalacticsAirlinesException {
        FilterFlightDTORequest filter = new FilterFlightDTORequest();
        List<Flight> flights = Arrays.asList(new Flight(), new Flight(), new Flight());
        when(flightRepository.findAll()).thenReturn(flights);

        List<FlightDTOResponse> response = flightService.searchFlight(filter);

        assertNotNull(response);
        assertEquals(flights.size(), response.size());
    }

    @Test
    void testSearchFlight_WithDepartureAirportFilter() throws GalacticsAirlinesException {
        Airport airport1 = new Airport();
        airport1.setAirportName("CDG");
        Flight flight1 = new Flight();
        flight1.setDepartureAirport(airport1);
        Flight flight2 = new Flight();
        flight2.setDepartureAirport(airport1);
        FilterFlightDTORequest filter = new FilterFlightDTORequest();
        filter.setDepartureAirport(Optional.of("CDG"));
        List<Flight> flights = Arrays.asList(flight1, flight2);
        when(flightRepository.findAll()).thenReturn(flights);

        List<FlightDTOResponse> response = flightService.searchFlight(filter);

        assertNotNull(response);
        assertEquals(flights.size(), response.size());
    }

    @Test
    void testSearchFlight_WithArrivalAirportFilter() throws GalacticsAirlinesException {
        Airport airport1 = new Airport();
        airport1.setAirportName("CDG");
        Flight flight1 = new Flight();
        flight1.setArrivalAirport(airport1);
        Flight flight2 = new Flight();
        flight2.setArrivalAirport(airport1);
        FilterFlightDTORequest filter = new FilterFlightDTORequest();
        filter.setArrivalAirport(Optional.of("CDG"));
        List<Flight> flights = Arrays.asList(flight1, flight2);
        when(flightRepository.findAll()).thenReturn(flights);

        List<FlightDTOResponse> response = flightService.searchFlight(filter);

        assertNotNull(response);
        assertEquals(flights.size(), response.size());
    }
}
