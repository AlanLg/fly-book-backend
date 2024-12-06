package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.FlightMapper;
import com.flybook.model.dto.request.FilterFlightDTORequest;
import com.flybook.model.dto.request.FlightDTORequest;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.FlightDTOResponse;
import com.flybook.model.entity.Airport;
import com.flybook.model.entity.Flight;
import com.flybook.model.entity.Reservation;
import com.flybook.repository.FlightRepository;
import com.flybook.repository.ReservationRepository;
import com.flybook.service.FlightService;
import com.flybook.utils.FlightValidationUtils;
import com.flybook.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FlightServiceImpl implements FlightService {

    private final AirportServiceImpl airportService;
    private final FlightRepository flightRepository;

    private final ReservationRepository reservationRepository;


    public FlightServiceImpl(AirportServiceImpl airportService, FlightRepository flightRepository, ReservationRepository reservationRepository) {
        this.airportService = airportService;
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public FlightDTOResponse addFlight(FlightDTORequest flightDTORequest) throws FlybookException {
        Flight createdFlight = linkAndSaveAssociatedEntities(flightDTORequest);

        if (flightDTORequest.getArrivalAirport().equals(flightDTORequest.getDepartureAirport())) {
            log.info("Departure and arrival airport cannot be the same");
            throw new FlybookException("Departure and arrival airport cannot be the same", HttpStatus.CONFLICT);
        }

        Optional<Flight> existingFlight = flightRepository.findByDepartureAirport_AirportNameAndArrivalAirport_AirportName(
                createdFlight.getDepartureAirport().getAirportName(),
                createdFlight.getArrivalAirport().getAirportName()
        );

        if (existingFlight.isPresent()) {
            return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(existingFlight.get());
        }

        return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(flightRepository.save(createdFlight));
    }

    @Override
    public FlightDTOResponse updateFlight(Long id, FlightDTORequest flightDTORequest) throws FlybookException {
        if (id == null || flightRepository.findById(id).isEmpty()) {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }

        Flight updatedFlight = linkAndSaveAssociatedEntities(flightDTORequest);;
        updatedFlight.setFlightId(id);
        flightRepository.save(updatedFlight);
        return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(updatedFlight);
    }

    private Flight linkAndSaveAssociatedEntities(FlightDTORequest flightDTORequest) {
        Flight updatedFlight = FlightMapper.INSTANCE.flightDTORequestToFlightEntity(flightDTORequest);

        if (!FlightValidationUtils.verifyElementInEntityToSave(updatedFlight)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Airport departureAirport = airportService.findOrSaveAirport(updatedFlight.getDepartureAirport());
        updatedFlight.setDepartureAirport(departureAirport);

        Airport arrivalAirport = airportService.findOrSaveAirport(updatedFlight.getArrivalAirport());
        updatedFlight.setArrivalAirport(arrivalAirport);

        return updatedFlight;
    }

    @Override
    public void deleteFlight(Long id) {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Flight flight = flightRepository.findById(id).orElse(null);
        if (flight != null ) {
            List<Reservation> reservationsOfFlight = reservationRepository.findByFlight_FlightId(flight.getFlightId()).orElse(null);
            if (reservationsOfFlight != null && !reservationsOfFlight.isEmpty()){
                throw new FlybookException("Impossible to delete flight because some reservations are links with this flight", HttpStatus.CONFLICT);
            }
            flightRepository.delete(flight);
        } else {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<FlightDTOResponse> searchFlight(FilterFlightDTORequest filter) {
        List<Flight> flights = flightRepository.findAll();

        if (filter.getDepartureAirport() != null && filter.getDepartureAirport().isPresent() && ValidationUtils.isNotEmpty(filter.getDepartureAirport().get())) {
            flights = filterByDepartureAirport(flights, filter.getDepartureAirport().get());
        }

        if (filter.getArrivalAirport() != null && filter.getArrivalAirport().isPresent() && ValidationUtils.isNotEmpty(filter.getArrivalAirport().get())) {
            flights = filterByArrivalAirport(flights, filter.getArrivalAirport().get());
        }

        return FlightMapper.INSTANCE.flightEntitiesToFlightDTOResponses(flights);
    }

    @Override
    public Flight getFlightForReservation(ReservationDTORequest reservationDTORequest) {
        return flightRepository.findByDepartureAirport_AirportNameAndArrivalAirport_AirportName(reservationDTORequest.getDepartureAirport(), reservationDTORequest.getArrivalAirport()
        ).orElseThrow(() -> new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND));
    }

    @Override
    public FlightDTOResponse getFlight(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Flight targetFlight = flightRepository.findById(id).orElse(null);

        if (targetFlight == null) {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }
        return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(targetFlight);
    }

    private List<Flight> filterByDepartureAirport(List<Flight> flights, String departureAirport) {
        return flights.stream()
                .filter(flight -> flight.getDepartureAirport().getAirportName().equalsIgnoreCase(departureAirport))
                .toList();
    }

    private List<Flight> filterByArrivalAirport(List<Flight> flights, String arrivalAirport) {
        return flights.stream()
                .filter(flight -> flight.getArrivalAirport().getAirportName().equalsIgnoreCase(arrivalAirport))
                .toList();
    }
}

