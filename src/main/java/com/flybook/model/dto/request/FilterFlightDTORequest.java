package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class FilterFlightDTORequest {
    @Schema(example = "CDG")
    private Optional<String> departureAirport;
    @Schema(example = "JFK Airport")
    private Optional<String> arrivalAirport;
}
