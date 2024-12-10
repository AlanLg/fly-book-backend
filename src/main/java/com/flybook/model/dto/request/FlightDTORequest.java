package com.flybook.model.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FlightDTORequest {
    private AirportDTORequest departureAirport;
    private AirportDTORequest arrivalAirport;
    private AirplaneDTORequest airplane;
    private int numberOfSeats;
    private double price;
}
