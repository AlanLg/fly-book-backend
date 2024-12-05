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

@RestController
@Slf4j
@RequestMapping("/airport")
@OpenAPIDefinition(info = @Info(title = "Galatics Airlines API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AirportDTOResponse> getAirport(@PathVariable Long id) throws FlybookException {
        return ResponseEntity.ok(airportService.getAirport(id));
    }

    @PostMapping("/add")
    public ResponseEntity<AirportDTOResponse> addAirport(@RequestBody AirportDTORequest airportDTORequest) throws FlybookException {
        log.info("Adding airport: {}", airportDTORequest.toString());
        return ResponseEntity.ok(airportService.addAirport(airportDTORequest));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<AirportDTOResponse> updateAirport(@PathVariable Long id, @RequestBody AirportDTORequest airportDTORequest) throws FlybookException {
        log.info("Updating airport: {}", airportDTORequest.toString());
        return ResponseEntity.ok(airportService.updateAirport(id, airportDTORequest));
    }

    @DeleteMapping("/delete/{id}")
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
