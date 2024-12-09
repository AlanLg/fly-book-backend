package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AirplaneDTORequest {
    @NotBlank(message = "Brand is mandatory")
    @Schema(example = "Boeing")
    private String brand;
    @NotBlank(message = "Model is mandatory")
    @Schema(example = "747")
    private String model;
}


