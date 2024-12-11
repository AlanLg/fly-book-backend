package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class ProfileDTORequest {
    @Schema(example = "John")
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    @Schema(example = "Doe")
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
    @Schema(example = "2001-04-17")
    @NotBlank(message = "Birthday is mandatory")
    private LocalDate birthday;
    @Schema(example = "1")
    private int nbLuggage;
}
