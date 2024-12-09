package com.flybook.controller;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.FilterFlightDTORequest;
import com.flybook.model.dto.request.FlightDTORequest;
import com.flybook.model.dto.response.FlightDTOResponse;
import com.flybook.service.FlightService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/flight")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FlightDTOResponse> getFlight(@PathVariable Long id) throws FlybookException {
        return ResponseEntity.ok(flightService.getFlight(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FlightDTOResponse>> getAllFlight() throws FlybookException {
        return ResponseEntity.ok(flightService.getAllFlight());
    }

    @PostMapping("/add")
    public ResponseEntity<FlightDTOResponse> addFlight(@Valid @RequestBody FlightDTORequest flightDTORequest) throws FlybookException {
        log.info("Adding flight: {}", flightDTORequest.toString());
        return ResponseEntity.ok(flightService.addFlight(flightDTORequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FlightDTOResponse> updateFlight(@PathVariable Long id, @RequestBody FlightDTORequest flightDTORequest) throws FlybookException {
        log.info("Updating flight: {}", flightDTORequest.toString());
        return ResponseEntity.ok(flightService.updateFlight(id, flightDTORequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        try {
            flightService.deleteFlight(id);
            log.info("Flight Deleted");
            return ResponseEntity.accepted().build();
        } catch (FlybookException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<FlightDTOResponse>> searchFlight (@RequestBody FilterFlightDTORequest filterFlightDTORequest) {
        log.info("Searching flight with filter: {}", filterFlightDTORequest.toString());
        return ResponseEntity.ok(flightService.searchFlight(filterFlightDTORequest));
    }
}
