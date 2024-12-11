package com.flybook.dbaccess;

import com.flybook.model.dto.db.FlightAndDepartureDateDTO;
import com.flybook.model.dto.db.ReservationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient("/db-access/reservation")
public interface ReservationDbAccess {

    @GetMapping("/flight/{flightId}/client/{clientId}")
    Optional<ReservationDTO> findByFlightIdAndClientId(@PathVariable Long clientId, @PathVariable Long flightId);

    @GetMapping("/client-email/{clientEmail}")
    List<ReservationDTO> findByClientEmail(@PathVariable String clientEmail);

    @GetMapping("/flight/{flightId}")
    List<ReservationDTO> findByFlightId(@PathVariable Long flightId);

    @PostMapping("/")
    ReservationDTO saveReservation(@RequestBody ReservationDTO reservationDTO);

    @PostMapping("/count")
    Integer countDistinctByFlightAndDepartureDate(@RequestBody FlightAndDepartureDateDTO flightAndDepartureDateDTO);

}
