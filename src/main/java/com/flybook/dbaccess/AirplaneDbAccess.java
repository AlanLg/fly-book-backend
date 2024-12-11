package com.flybook.dbaccess;

import com.flybook.model.dto.db.AirplaneDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "Airplane", path = "/db-access/airplane", url = "localhost:8081")
public interface AirplaneDbAccess {

    @GetMapping("/id/{id}")
    Optional<AirplaneDTO> findById(@PathVariable Long id);

    @GetMapping("/brand/{brand}/model/{model}")
    Optional<AirplaneDTO> findByBrandAndModel(@PathVariable String brand, @PathVariable String model);

    @PostMapping("/")
    AirplaneDTO saveAirplane(@RequestBody AirplaneDTO airplaneDTO);

    @DeleteMapping("/id/{id}")
    AirplaneDTO deleteAirplane(@PathVariable Long id);
}
