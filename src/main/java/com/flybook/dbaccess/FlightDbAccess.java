package com.flybook.dbaccess;

import com.flybook.model.dto.db.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient("/db-access/flight")
public interface FlightDbAccess {

    @GetMapping("/id/{id}")
    Optional<FlightDTO> findById(@PathVariable Long id);

    @GetMapping("/departure/{departureAirport}/arrival/{arrivalAirport}")
    Optional<FlightDTO> findByDepartureAndArrivalAirport(@PathVariable String departureAirport, @PathVariable String arrivalAirport);

    @GetMapping("/")
    List<FlightDTO> findAll();

    @DeleteMapping("/id/{id}")
    void deleteFlight(@PathVariable Long id);

    @PostMapping("/")
    FlightDTO saveFlight(@RequestBody FlightDTO flightDTO);

}
