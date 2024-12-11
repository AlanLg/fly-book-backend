package com.flybook.service;

import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.List;

public interface ReservationService {
    ReservationDTOResponse createReservation(ReservationDTORequest reservationDTORequest, String email);
    List<ReservationDTOResponse> getAllReservationsForClient(String clientEmail);
    Sinks.Many<String> getSink();
}
