package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class ReservationDTORequest {
    @Schema(example = "john.doe@gmail.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email")
    @Size(min = 3, message = "Email must be at least 3 chars")
    private String email;
    @NotBlank(message = "Airport name is mandatory")
    @Schema(example = "CDG")
    private String departureAirport;
    @NotBlank(message = "Airport name is mandatory")
    @Schema(example = "JDK")
    private String arrivalAirport;
    @Schema(example = "2024-04-18")
    private LocalDate departureDate;
}
