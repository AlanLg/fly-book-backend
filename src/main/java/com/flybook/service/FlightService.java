package com.flybook.service;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.FilterFlightDTORequest;
import com.flybook.model.dto.request.FlightDTORequest;
import com.flybook.model.dto.response.FlightDTOResponse;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface FlightService {
    FlightDTOResponse addFlight(FlightDTORequest flightDTOIn) throws FlybookException;
    FlightDTOResponse updateFlight(Long id, FlightDTORequest flightDTORequest);
    FlightDTOResponse getFlight(Long id) throws FlybookException;
    List<FlightDTOResponse> searchFlight(FilterFlightDTORequest filterFlightDTORequest);
    List<FlightDTOResponse> getAllFlight() throws FlybookException;
    void deleteFlight(Long id);
}
