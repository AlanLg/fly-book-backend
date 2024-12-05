package com.flybook.service;

import com.flybook.model.dto.request.ReservationDTORequestWithExistingClient;
import com.flybook.model.dto.response.ReservationDTOResponse;
import org.springframework.stereotype.Service;

@Service
public interface ReservationService {
    ReservationDTOResponse createReservation(ReservationDTORequestWithExistingClient reservationDTORequestWithExistingClient);
}
