package com.flybook.service;

import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {
    ReservationDTOResponse createReservation(ReservationDTORequest reservationDTORequest);
}
