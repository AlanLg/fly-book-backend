package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AirportDTORequest {
    @NotBlank(message = "Airport name is mandatory")
    @Schema(example = "CDG")
    private String airportName;
}
