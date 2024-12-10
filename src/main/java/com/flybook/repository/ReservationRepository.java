package com.flybook.repository;

import com.flybook.model.entity.Flight;
import com.flybook.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByFlight_FlightIdAndClient_Id(Long flightId, Long clientId);
    Optional<List<Reservation>> findByFlight_FlightId(Long flightId);
    int countDistinctByFlightAndDepartureDate(Flight flight, LocalDate departureDate);
    List<Reservation> findByClient_Email(String email);
    Long countAllByCreationDateIsAfter(LocalDateTime localDateTime);

}