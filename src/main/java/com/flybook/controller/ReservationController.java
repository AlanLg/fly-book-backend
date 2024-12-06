package com.flybook.controller;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.ReservationDTORequestWithExistingClient;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.service.ReservationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<ReservationDTOResponse> addReservationWithExistingClient(@RequestBody ReservationDTORequestWithExistingClient reservationDTORequestWithExistingClient) throws FlybookException {
        return ResponseEntity.ok(reservationService.createReservation(reservationDTORequestWithExistingClient));
    }
}
