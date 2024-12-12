package com.flybook.service.impl;

import com.flybook.dbaccess.AirportDbAccess;
import com.flybook.exception.FlybookException;
import com.flybook.mapper.AirportMapper;
import com.flybook.model.dto.db.AirportDTO;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import com.flybook.service.AirportService;
import com.flybook.utils.AirportValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportDbAccess airportDbAccess;

    @Override
    public AirportDTOResponse getAirport(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Optional<AirportDTO> targetAirport = airportDbAccess.findById(id);

        if (targetAirport.isEmpty()) {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }
        return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(targetAirport.get());
    }

    @Override
    public List<String> getAllAirport() throws FlybookException {
        List<AirportDTO> targetAirportDTOS = airportDbAccess.findAll();

        if (targetAirportDTOS.isEmpty()) {
            throw new FlybookException("No airport in the data base", HttpStatus.NOT_FOUND);
        }

        return targetAirportDTOS.stream()
                .map(AirportDTO::getAirportName)
                .toList();
    }

    @Override
    public AirportDTOResponse addAirport(AirportDTORequest airportDTORequest) throws FlybookException {
        AirportDTO createdAirportDTO = AirportMapper.INSTANCE.airportDTORequestToAirportEntity(airportDTORequest);

        if (!AirportValidationUtils.isValidAirport(createdAirportDTO)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Optional<AirportDTO> existingAirport = airportDbAccess.findByAirportName(createdAirportDTO.getAirportName());

        if (existingAirport.isPresent()) {
            return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(existingAirport.get());
        }

        return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(airportDbAccess.saveAirport(createdAirportDTO));
    }

    @Override
    public AirportDTOResponse updateAirport(Long id, AirportDTORequest airportDTORequest) throws FlybookException {
        if (id == null || airportDbAccess.findById(id).isEmpty()) {
            throw new FlybookException("No airport in the data base", HttpStatus.NOT_FOUND);
        }

        AirportDTO updatedAirportDTO = AirportMapper.INSTANCE.airportDTORequestToAirportEntity(airportDTORequest);

        if (!AirportValidationUtils.isValidAirport(updatedAirportDTO)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        updatedAirportDTO.setAirportId(id);
        airportDbAccess.saveAirport(updatedAirportDTO);
        return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(updatedAirportDTO);
    }

    @Override
    public void deleteAirport(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        AirportDTO airportDTO = airportDbAccess.findById(id).orElse(null);

        if (airportDTO != null) {
            airportDbAccess.deleteAirport(airportDTO.getAirportId());
        } else {
            throw new FlybookException("No airport in the data base", HttpStatus.NOT_FOUND);
        }
    }

    public AirportDTO findOrSaveAirport(AirportDTO airportDTO) {
        return airportDbAccess.findByAirportName(airportDTO.getAirportName())
                .orElseGet(() -> airportDbAccess.saveAirport(airportDTO));
    }
}
