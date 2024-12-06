package com.flybook.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class ClientDTORequest {
    @Schema(example = "John")
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    @Schema(example = "Doe")
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
    @Schema(example = "john.doe@gmail.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email")
    @Size(min = 3, message = "Email must be at least 3 chars")
    private String email;
    @Schema(example = "********")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 chars")
    private String password;
    @Schema(example = "********")
    @NotBlank(message = "Confirm password is mandatory")
    @Size(min = 8, message = "Confirm password must be at least 8 chars")
    private String confirmPassword;

}
