package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Schema(example = "300")
    @NotNull(message = "Number of seats is mandatory")
    private Integer numberOfSeats;
    @Schema(example = "750.0")
    @NotNull(message = "Price is mandatory")
    private Double price;
}
