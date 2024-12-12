package com.flybook.model.dto.db;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FlightAndDepartureDateDTO {
    private FlightDTO flight;
    private LocalDate departureDate;
}
