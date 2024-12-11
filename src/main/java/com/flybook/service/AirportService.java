package com.flybook.service;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;

import java.util.List;

public interface AirportService {
    AirportDTOResponse getAirport(Long id) throws FlybookException;
    List<String> getAllAirport() throws FlybookException;
    AirportDTOResponse addAirport(AirportDTORequest airportDTORequest) throws FlybookException;
    AirportDTOResponse updateAirport(Long id, AirportDTORequest airportDTORequest) throws FlybookException;
    void deleteAirport(Long id) throws FlybookException;
}
