package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTORequest {

    @Schema(example = "john.doe@gmail.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email")
    @Size(min = 3, message = "Email must be at least 3 chars")
    private String email;
    @Schema(example = "secretpassword")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 chars")
    private String password;
}
