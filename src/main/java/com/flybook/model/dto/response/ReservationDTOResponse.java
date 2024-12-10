package com.flybook.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservationDTOResponse {
    private FlightDTOResponse flight;
    private ClientDTOResponse client;
    private List<ProfilDTOResponse> profilDTOResponseList;
    private LocalDate departureDate;
    private double priceOfReservation;
    private int nbLuggage;
}
