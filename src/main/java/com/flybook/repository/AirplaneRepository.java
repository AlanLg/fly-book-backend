package com.flybook.repository;

import com.flybook.model.entity.Airplane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirplaneRepository extends JpaRepository<Airplane, Long> {
    Optional<Airplane> findByBrandAndModel(String brand, String model);

}
