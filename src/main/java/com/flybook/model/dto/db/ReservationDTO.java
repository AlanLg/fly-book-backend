package com.flybook.model.dto.db;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservationDTO {
    private Long id;
    private FlightDTO flight;
    private ClientDTO client;
    private List<ProfileDTO> profiles;
    private LocalDate departureDate;
    private int nbLuggage;
    private double priceOfReservation;
    private LocalDateTime creationDate = LocalDateTime.now();
}