package com.flybook.service.impl;

import com.flybook.dbaccess.AirplaneDbAccess;
import com.flybook.exception.FlybookException;
import com.flybook.mapper.AirplaneMapper;
import com.flybook.model.dto.db.AirplaneDTO;
import com.flybook.model.dto.request.AirplaneDTORequest;
import com.flybook.model.dto.response.AirplaneDTOResponse;
import com.flybook.service.AirplaneService;
import com.flybook.utils.AirplaneValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirplaneServiceImpl implements AirplaneService {

    private final AirplaneDbAccess airplaneDbAccess;

    @Override
    public AirplaneDTOResponse addAirplane(AirplaneDTORequest airplaneDTORequest) throws FlybookException {
        AirplaneDTO createdAirplaneDTO = AirplaneMapper.INSTANCE.airplaneDTORequestToAirplaneEntity(airplaneDTORequest);

        if (!AirplaneValidationUtils.isValidAirplane(createdAirplaneDTO)) {
            throw new FlybookException("Missing element in the JSON", HttpStatus.BAD_REQUEST);
        }

        Optional<AirplaneDTO> existingAirplane = airplaneDbAccess.findByBrandAndModel(createdAirplaneDTO.getBrand(), createdAirplaneDTO.getModel());

        if (existingAirplane.isPresent()) {
            return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(existingAirplane.get());
        }

        return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(airplaneDbAccess.saveAirplane(createdAirplaneDTO));
    }

    @Override
    public AirplaneDTOResponse updateAirplane(Long id, AirplaneDTORequest airplaneDTORequest) {
        if (id == null || airplaneDbAccess.findById(id).isEmpty()) {
            throw new FlybookException("No airplane in the database", HttpStatus.NOT_FOUND);
        }

        AirplaneDTO updatedAirplaneDTO = AirplaneMapper.INSTANCE.airplaneDTORequestToAirplaneEntity(airplaneDTORequest);

        if (!AirplaneValidationUtils.isValidAirplane(updatedAirplaneDTO)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        updatedAirplaneDTO.setAirplaneId(id);
        airplaneDbAccess.saveAirplane(updatedAirplaneDTO);
        return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(updatedAirplaneDTO);

    }

    @Override
    public void deleteAirplane(Long id) {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        AirplaneDTO airplaneDTO = airplaneDbAccess.findById(id).orElse(null);

        if (airplaneDTO != null) {
            airplaneDbAccess.deleteAirplane(airplaneDTO.getAirplaneId());
        } else {
            throw new FlybookException("No airplane in the data base", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public AirplaneDTOResponse getAirplane(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        AirplaneDTO targetAirplaneDTO = airplaneDbAccess.findById(id).orElse(null);

        if (targetAirplaneDTO == null) {
            throw new FlybookException("No airplane in the data base", HttpStatus.NOT_FOUND);
        }
        return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(targetAirplaneDTO);
    }

    public AirplaneDTO findOrSaveAirplane(AirplaneDTO airplaneDTO) {
        return airplaneDbAccess.findByBrandAndModel(
                airplaneDTO.getBrand(),
                airplaneDTO.getModel()
        ).orElseGet(() -> airplaneDbAccess.saveAirplane(airplaneDTO));
    }
}
