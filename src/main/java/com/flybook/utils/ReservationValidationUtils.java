package com.flybook.utils;

import com.flybook.model.entity.Reservation;

import java.util.Objects;

import static com.flybook.utils.ClientValidationUtils.isValidClient;

public class ReservationValidationUtils {
    public static boolean isValidReservation(Reservation reservation) {
        return reservation != null &&
                isValidClient(reservation.getClient()) &&
                Objects.nonNull(reservation.getFlight()
                );
    }
}
