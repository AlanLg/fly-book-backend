package com.flybook.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationDTOResponse {
    private FlightDTOResponse flight;
    private ClientDTOResponse client;
    private LocalDate departureDate;
    private int nbLuggage;
}
