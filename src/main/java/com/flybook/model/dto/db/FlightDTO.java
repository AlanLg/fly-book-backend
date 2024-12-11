package com.flybook.model.dto.db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightDTO {
    private Long flightId;
    private AirportDTO departureAirport;
    private AirportDTO arrivalAirport;
    private double price;
    private AirplaneDTO airplane;
    private int numberOfSeats;
}