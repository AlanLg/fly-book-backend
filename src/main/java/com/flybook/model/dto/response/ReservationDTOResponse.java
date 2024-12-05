package com.flybook.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDTOResponse {
    private FlightDTOResponse flight;
    private ClientDTOResponse client;
    private LocalDateTime departureDateTime;
}
