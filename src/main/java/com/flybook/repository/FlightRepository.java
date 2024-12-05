package com.flybook.repository;

import com.flybook.model.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByDepartureAirport_AirportNameAndArrivalAirport_AirportName(
            String departureAirportName,
            String arrivalAirportname
    );
}