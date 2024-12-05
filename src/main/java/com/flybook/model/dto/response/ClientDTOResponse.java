package com.flybook.model.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter @Setter
public class ClientDTOResponse {
    private String firstname;
    private String lastname;
    private String email;
}
