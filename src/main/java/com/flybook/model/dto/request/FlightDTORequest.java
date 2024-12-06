package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FlightDTORequest {
    private AirportDTORequest departureAirport;
    private AirportDTORequest arrivalAirport;
    private int numberOfSeats;
    private double price;
}
