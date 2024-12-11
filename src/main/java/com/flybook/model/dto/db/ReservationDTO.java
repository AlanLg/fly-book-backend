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
    private FlightDTO flightDTO;
    private ClientDTO clientDTO;
    private List<ProfileDTO> profileDTOS;
    private LocalDate departureDate;
    private int nbLuggage;
    private double priceOfReservation;
    private LocalDateTime creationDate = LocalDateTime.now();
}