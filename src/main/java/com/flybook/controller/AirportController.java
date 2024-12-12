package com.flybook.controller;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import com.flybook.service.AirportService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/v1/airport")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AirportDTOResponse> getAirport(@PathVariable Long id) throws FlybookException {
        return ResponseEntity.ok(airportService.getAirport(id));
    }

    @GetMapping("")
    public ResponseEntity<List<String>> getAllAirport() throws FlybookException {
        return ResponseEntity.ok(airportService.getAllAirport());
    }

    @PostMapping("")
    public ResponseEntity<AirportDTOResponse> addAirport(@RequestBody AirportDTORequest airportDTORequest) throws FlybookException {
        log.info("Adding airport: {}", airportDTORequest.toString());
        return ResponseEntity.ok(airportService.addAirport(airportDTORequest));
    }


    @PutMapping("/id/{id}")
    public ResponseEntity<AirportDTOResponse> updateAirport(@PathVariable Long id, @RequestBody AirportDTORequest airportDTORequest) throws FlybookException {
        log.info("Updating airport: {}", airportDTORequest.toString());
        return ResponseEntity.ok(airportService.updateAirport(id, airportDTORequest));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        try {
            airportService.deleteAirport(id);
            log.info("Airport Deleted");
            return ResponseEntity.accepted().build();
        } catch (FlybookException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
