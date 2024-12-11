package com.flybook.dbaccess;

import com.flybook.model.dto.db.AirportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient("/db-access/airport")
public interface AirportDbAccess {

    @GetMapping("/id/{id}")
    Optional<AirportDTO> findById(@PathVariable Long id);

    @GetMapping("/")
    List<AirportDTO> findAll();

    @GetMapping("/name/{airportName}")
    Optional<AirportDTO> findByAirportName(@PathVariable String airportName);

    @DeleteMapping("/id/{id}")
    Void deleteAirport(@PathVariable Long id);

    @PostMapping("/")
    AirportDTO saveAirport(@RequestBody AirportDTO airportDTO);
}
