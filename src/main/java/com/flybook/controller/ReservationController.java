package com.flybook.controller;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Sinks;

import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/")
    public ResponseEntity<ReservationDTOResponse> addReservationWithExistingClient(@RequestBody ReservationDTORequest reservationDTORequest, Principal principal) throws FlybookException {

        return ResponseEntity.ok(reservationService.createReservation(reservationDTORequest, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTOResponse>> getAllReservationForClient(Principal principal) throws FlybookException {
        return ResponseEntity.ok(reservationService.getAllReservationsForClient(principal.getName()));
    }

    @GetMapping("test/success")
    public void test() {
        Sinks.EmitResult result = reservationService.getSink().tryEmitNext("success");
        if (result.isFailure()) {
            log.error("Failed to push event");
        }
    }

    @GetMapping("test/failed")
    public void testFailed() {
        Sinks.EmitResult result = reservationService.getSink().tryEmitNext("failed");
        if (result.isFailure()) {
            log.error("Failed to push event");
        }
    }
}
