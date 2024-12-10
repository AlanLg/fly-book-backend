package com.flybook.model.dto.response;

import com.flybook.model.dto.request.ProfilDTORequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class ProfilDTOResponse extends ProfilDTORequest {
}
