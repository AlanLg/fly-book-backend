package com.flybook.model.dto.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightDTO {
    private Long flightId;
    private AirportDTO departureAirportDTO;
    private AirportDTO arrivalAirportDTO;
    private double price;
    private AirplaneDTO airplaneDTO;
    private int numberOfSeats;
}