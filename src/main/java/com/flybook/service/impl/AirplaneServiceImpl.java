package com.flybook.service.impl;
import com.flybook.exception.FlybookException;
import com.flybook.mapper.AirplaneMapper;
import com.flybook.model.dto.request.AirplaneDTORequest;
import com.flybook.model.dto.response.AirplaneDTOResponse;
import com.flybook.model.entity.Airplane;
import com.flybook.repository.AirplaneRepository;
import com.flybook.service.AirplaneService;
import com.flybook.utils.AirplaneValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AirplaneServiceImpl implements AirplaneService {
    private final AirplaneRepository airplaneRepository;

    public AirplaneServiceImpl(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    @Override
    public AirplaneDTOResponse addAirplane(AirplaneDTORequest airplaneDTORequest) throws FlybookException {
        Airplane createdAirplane = AirplaneMapper.INSTANCE.airplaneDTORequestToAirplaneEntity(airplaneDTORequest);

        if (!AirplaneValidationUtils.isValidAirplane(createdAirplane)) {
            throw new FlybookException("Missing element in the JSON", HttpStatus.BAD_REQUEST);
        }

        Optional<Airplane> existingAirplane = airplaneRepository.findByBrandAndModel(createdAirplane.getBrand(), createdAirplane.getModel());

        if (existingAirplane.isPresent()) {
            return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(existingAirplane.get());
        }

        return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(airplaneRepository.save(createdAirplane));
    }

    @Override
    public AirplaneDTOResponse updateAirplane(Long id, AirplaneDTORequest airplaneDTORequest) {
        if (id == null || airplaneRepository.findById(id).isEmpty()) {
            throw new FlybookException("No airplane in the database", HttpStatus.NOT_FOUND);
        }

        Airplane updatedAirplane = AirplaneMapper.INSTANCE.airplaneDTORequestToAirplaneEntity(airplaneDTORequest);

        if (!AirplaneValidationUtils.isValidAirplane(updatedAirplane)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        updatedAirplane.setAirplaneId(id);
        airplaneRepository.save(updatedAirplane);
        return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(updatedAirplane);

    }

    @Override
    public void deleteAirplane(Long id) {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Airplane airplane = airplaneRepository.findById(id).orElse(null);
        if (airplane != null) {
            airplaneRepository.delete(airplane);
        } else {
            throw new FlybookException("No airplane in the data base", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public AirplaneDTOResponse getAirplane(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Airplane targetAirplane = airplaneRepository.findById(id).orElse(null);

        if (targetAirplane == null) {
            throw new FlybookException("No airplane in the data base", HttpStatus.NOT_FOUND);
        }
        return AirplaneMapper.INSTANCE.airplaneEntityToAirplaneDTOResponse(targetAirplane);
    }

    public Airplane findOrSaveAirplane(Airplane airplane) {
        return airplaneRepository.findByBrandAndModel(
                airplane.getBrand(),
                airplane.getModel()
        ).orElseGet(() -> airplaneRepository.save(airplane));
    }
}
