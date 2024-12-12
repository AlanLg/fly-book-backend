package com.flybook.model.dto.db;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate birthday;
    private ReservationDTO reservation;
    private int nbLuggage;
}
