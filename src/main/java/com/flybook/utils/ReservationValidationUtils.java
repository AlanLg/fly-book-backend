package com.flybook.utils;

import com.flybook.model.dto.db.ReservationDTO;

import java.util.Objects;

import static com.flybook.utils.ClientValidationUtils.isValidClient;

public class ReservationValidationUtils {
    public static boolean isValidReservation(ReservationDTO reservationDTO) {
        return reservationDTO != null &&
                isValidClient(reservationDTO.getClient()) &&
                Objects.nonNull(reservationDTO.getFlight()) &&
                reservationDTO.getNbLuggage() >= 0;
    }
}
