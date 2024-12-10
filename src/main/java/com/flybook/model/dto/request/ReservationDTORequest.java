package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservationDTORequest {
    @NotBlank(message = "Airport name is mandatory")
    @Schema(example = "CDG")
    private String departureAirport;
    @NotBlank(message = "Airport name is mandatory")
    @Schema(example = "JDK")
    private String arrivalAirport;
    @NotBlank(message = "Departure date is mandatory")
    @Schema(example = "2024-04-18")
    private LocalDate departureDate;
    @Schema(example = "$")
    private String currency;
    @NotEmpty(message = "Profil is mandatory")
    @NotBlank(message = "Profil is mandatory")
    private List<ProfilDTORequest> profilDTORequestList;
}
