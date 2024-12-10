package com.flybook.service;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.AirplaneDTORequest;
import com.flybook.model.dto.response.AirplaneDTOResponse;
import jakarta.transaction.Transactional;

@Transactional
public interface AirplaneService {
    AirplaneDTOResponse addAirplane(AirplaneDTORequest airplaneDTORequest) throws FlybookException;
    AirplaneDTOResponse updateAirplane(Long id, AirplaneDTORequest airplaneDTORequest);
    void deleteAirplane(Long id);
    AirplaneDTOResponse getAirplane(Long id) throws FlybookException;
}