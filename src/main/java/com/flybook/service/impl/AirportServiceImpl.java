package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.AirportMapper;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import com.flybook.model.entity.Airport;
import com.flybook.repository.AirportRepository;
import com.flybook.service.AirportService;
import com.flybook.utils.AirportValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public AirportDTOResponse getAirport(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Airport targetAirport = airportRepository.findById(id).orElse(null);

        if (targetAirport == null) {
            throw new FlybookException("No flight in the data base", HttpStatus.NOT_FOUND);
        }
        return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(targetAirport);
    }

    @Override
    public List<String> getAllAirport() throws FlybookException {
        List<Airport> targetAirports = airportRepository.findAll();

        if (targetAirports.isEmpty()) {
            throw new FlybookException("No airport in the data base", HttpStatus.NOT_FOUND);
        }

        List<String> airports = targetAirports.stream()
                .map(Airport::getAirportName)
                .toList();


        return airports;
    }

    @Override
    public AirportDTOResponse addAirport(AirportDTORequest airportDTORequest) throws FlybookException {
        Airport createdAirport = AirportMapper.INSTANCE.airportDTORequestToAirportEntity(airportDTORequest);

        if (!AirportValidationUtils.isValidAirport(createdAirport)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Optional<Airport> existingAirport = airportRepository.findByAirportName(createdAirport.getAirportName());

        if (existingAirport.isPresent()) {
            return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(existingAirport.get());
        }

        return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(airportRepository.save(createdAirport));
    }

    @Override
    public AirportDTOResponse updateAirport(Long id, AirportDTORequest airportDTORequest) throws FlybookException {
        if (id == null || airportRepository.findById(id).isEmpty()) {
            throw new FlybookException("No airport in the data base", HttpStatus.NOT_FOUND);
        }

        Airport updatedAirport = AirportMapper.INSTANCE.airportDTORequestToAirportEntity(airportDTORequest);

        if (!AirportValidationUtils.isValidAirport(updatedAirport)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        updatedAirport.setAirportId(id);
        airportRepository.save(updatedAirport);
        return AirportMapper.INSTANCE.airportEntityToAirportDTOResponse(updatedAirport);
    }

    @Override
    public void deleteAirport(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Airport airport = airportRepository.findById(id).orElse(null);
        if (airport != null) {
            airportRepository.delete(airport);
        } else {
            throw new FlybookException("No airport in the data base", HttpStatus.NOT_FOUND);
        }
    }

    public Airport findOrSaveAirport(Airport airport) {
        return airportRepository.findByAirportName(airport.getAirportName()).orElseGet(() -> airportRepository.save(airport));
    }
}
