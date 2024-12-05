package com.flybook.service;

import com.flybook.exception.GalacticsAirlinesException;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import jakarta.transaction.Transactional;

@Transactional
public interface AirportService {
    AirportDTOResponse getAirport(Long id) throws GalacticsAirlinesException;
    AirportDTOResponse addAirport(AirportDTORequest airportDTORequest) throws GalacticsAirlinesException;
    AirportDTOResponse updateAirport(Long id, AirportDTORequest airportDTORequest) throws GalacticsAirlinesException;
    void deleteAirport(Long id) throws GalacticsAirlinesException;
}
