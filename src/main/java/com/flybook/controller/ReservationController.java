package com.flybook.controller;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<ReservationDTOResponse> addReservationWithExistingClient(@RequestBody ReservationDTORequest reservationDTORequest, Principal principal) throws FlybookException {

        return ResponseEntity.ok(reservationService.createReservation(reservationDTORequest, principal.getName()));
    }

    @PostMapping("/get")
    public ResponseEntity<List<ReservationDTOResponse>> getAllReservationForClient(Principal principal) throws FlybookException {
        return ResponseEntity.ok(reservationService.getAllReservationsForClient(principal.getName()));
    }
}
