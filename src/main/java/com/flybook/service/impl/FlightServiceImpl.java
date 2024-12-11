package com.flybook.service.impl;

import com.flybook.dbaccess.FlightDbAccess;
import com.flybook.dbaccess.ReservationDbAccess;
import com.flybook.exception.FlybookException;
import com.flybook.mapper.FlightMapper;
import com.flybook.model.dto.db.AirplaneDTO;
import com.flybook.model.dto.db.AirportDTO;
import com.flybook.model.dto.db.FlightDTO;
import com.flybook.model.dto.request.FilterFlightDTORequest;
import com.flybook.model.dto.request.FlightDTORequest;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.FlightDTOResponse;
import com.flybook.model.dto.db.ReservationDTO;
import com.flybook.service.FlightService;
import com.flybook.utils.FlightValidationUtils;
import com.flybook.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final AirportServiceImpl airportService;
    private final AirplaneServiceImpl airplaneService;
    private final FlightDbAccess flightDbAccess;
    private final ReservationDbAccess reservationDbAccess;

    @Override
    public FlightDTOResponse addFlight(FlightDTORequest flightDTORequest) throws FlybookException {
        FlightDTO createdFlightDTO = linkAndSaveAssociatedEntities(flightDTORequest);

        if (flightDTORequest.getArrivalAirport().equals(flightDTORequest.getDepartureAirport())) {
            log.info("Departure and arrival airport cannot be the same");
            throw new FlybookException("Departure and arrival airport cannot be the same", HttpStatus.CONFLICT);
        }

        Optional<FlightDTO> existingFlight = flightDbAccess.findByDepartureAndArrivalAirport(
                createdFlightDTO.getDepartureAirportDTO().getAirportName(),
                createdFlightDTO.getArrivalAirportDTO().getAirportName()
        );

        if (existingFlight.isPresent()) {
            return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(existingFlight.get());
        }

        return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(flightDbAccess.saveFlight(createdFlightDTO));
    }

    @Override
    public FlightDTOResponse updateFlight(Long id, FlightDTORequest flightDTORequest) throws FlybookException {
        if (id == null || flightDbAccess.findById(id).isEmpty()) {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }

        FlightDTO updatedFlightDTO = linkAndSaveAssociatedEntities(flightDTORequest);;
        updatedFlightDTO.setFlightId(id);
        flightDbAccess.saveFlight(updatedFlightDTO);
        return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(updatedFlightDTO);
    }

    private FlightDTO linkAndSaveAssociatedEntities(FlightDTORequest flightDTORequest) {
        FlightDTO updatedFlightDTO = FlightMapper.INSTANCE.flightDTORequestToFlightEntity(flightDTORequest);

        if (!FlightValidationUtils.verifyElementInEntityToSave(updatedFlightDTO)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        AirplaneDTO airplaneDTO = airplaneService.findOrSaveAirplane(updatedFlightDTO.getAirplaneDTO());
        updatedFlightDTO.setAirplaneDTO(airplaneDTO);

        AirportDTO departureAirportDTO = airportService.findOrSaveAirport(updatedFlightDTO.getDepartureAirportDTO());
        updatedFlightDTO.setDepartureAirportDTO(departureAirportDTO);

        AirportDTO arrivalAirportDTO = airportService.findOrSaveAirport(updatedFlightDTO.getArrivalAirportDTO());
        updatedFlightDTO.setArrivalAirportDTO(arrivalAirportDTO);

        return updatedFlightDTO;
    }

    @Override
    public void deleteFlight(Long id) {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        FlightDTO flightDTO = flightDbAccess.findById(id).orElse(null);
        if (flightDTO != null ) {
            List<ReservationDTO> reservationsOfFlight = reservationDbAccess.findByFlight_FlightId(flightDTO.getFlightId()).orElse(null);
            if (reservationsOfFlight != null && !reservationsOfFlight.isEmpty()){
                throw new FlybookException("Impossible to delete flight because some reservations are links with this flight", HttpStatus.CONFLICT);
            }
            flightDbAccess.deleteFlight(flightDTO.getFlightId());
        } else {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<FlightDTOResponse> searchFlight(FilterFlightDTORequest filter) {
        List<FlightDTO> flightDTOS = flightDbAccess.findAll();

        if (filter.getDepartureAirport() != null && filter.getDepartureAirport().isPresent() && ValidationUtils.isNotEmpty(filter.getDepartureAirport().get())) {
            flightDTOS = filterByDepartureAirport(flightDTOS, filter.getDepartureAirport().get());
        }

        if (filter.getArrivalAirport() != null && filter.getArrivalAirport().isPresent() && ValidationUtils.isNotEmpty(filter.getArrivalAirport().get())) {
            flightDTOS = filterByArrivalAirport(flightDTOS, filter.getArrivalAirport().get());
        }

        return FlightMapper.INSTANCE.flightEntitiesToFlightDTOResponses(flightDTOS);
    }

    public FlightDTO getFlightForReservation(ReservationDTORequest reservationDTORequest) {
        return flightDbAccess.findByDepartureAndArrivalAirport(reservationDTORequest.getDepartureAirport(), reservationDTORequest.getArrivalAirport()
        ).orElseThrow(() -> new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND));
    }

    @Override
    public FlightDTOResponse getFlight(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        FlightDTO targetFlightDTO = flightDbAccess.findById(id).orElse(null);

        if (targetFlightDTO == null) {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }
        return FlightMapper.INSTANCE.flightEntityToFlightDTOResponse(targetFlightDTO);
    }

    @Override
    public List<FlightDTOResponse> getAllFlight() throws FlybookException {
        List<FlightDTO> targetFlightDTOS = flightDbAccess.findAll();

        if (targetFlightDTOS.isEmpty()) {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }

        return FlightMapper.INSTANCE.flightEntitiesToFlightDTOResponses(targetFlightDTOS);
    }

    private List<FlightDTO> filterByDepartureAirport(List<FlightDTO> flightDTOS, String departureAirport) {
        return flightDTOS.stream()
                .filter(flight -> flight.getDepartureAirportDTO().getAirportName().equalsIgnoreCase(departureAirport))
                .toList();
    }

    private List<FlightDTO> filterByArrivalAirport(List<FlightDTO> flightDTOS, String arrivalAirport) {
        return flightDTOS.stream()
                .filter(flight -> flight.getArrivalAirportDTO().getAirportName().equalsIgnoreCase(arrivalAirport))
                .toList();
    }
}

