package com.flybook.service;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface AirportService {
    AirportDTOResponse getAirport(Long id) throws FlybookException;
    List<AirportDTOResponse> getAllAirport() throws FlybookException;
    AirportDTOResponse addAirport(AirportDTORequest airportDTORequest) throws FlybookException;
    AirportDTOResponse updateAirport(Long id, AirportDTORequest airportDTORequest) throws FlybookException;
    void deleteAirport(Long id) throws FlybookException;
}
