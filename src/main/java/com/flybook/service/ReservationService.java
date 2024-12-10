package com.flybook.service;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@Service
public interface ReservationService {
    ReservationDTOResponse createReservation(ReservationDTORequest reservationDTORequest, String email);
    List<ReservationDTOResponse> getAllReservationsForClient(String clientEmail);
    Long getNumberOfReservationsInRealTime() throws FlybookException;
    Sinks.Many<String> getSink();
}
