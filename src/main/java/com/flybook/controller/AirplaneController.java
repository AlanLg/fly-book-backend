package com.flybook.controller;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.AirplaneDTORequest;
import com.flybook.model.dto.response.AirplaneDTOResponse;
import com.flybook.service.AirplaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/airplane")
public class AirplaneController {
    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AirplaneDTOResponse> getAirplane(@PathVariable Long id) throws FlybookException {
        return ResponseEntity.ok(airplaneService.getAirplane(id));
    }

    @PostMapping("/add")
    public ResponseEntity<AirplaneDTOResponse> addAirplane(@RequestBody AirplaneDTORequest airplaneDTORequest) throws FlybookException {
        log.info("Adding airplane: {}", airplaneDTORequest.toString());
        return ResponseEntity.ok(airplaneService.addAirplane(airplaneDTORequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AirplaneDTOResponse> updateAirplane(@PathVariable Long id, @RequestBody AirplaneDTORequest airplaneDTORequest) throws FlybookException {
        log.info("Updating airplane: {}", airplaneDTORequest.toString());
        return ResponseEntity.ok(airplaneService.updateAirplane(id, airplaneDTORequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAirplane(@PathVariable Long id) {
        try {
            airplaneService.deleteAirplane(id);
            log.info("Airplane deleted");
            return ResponseEntity.accepted().build();
        } catch (FlybookException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
