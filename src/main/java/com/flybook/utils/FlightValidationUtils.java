package com.flybook.utils;

import com.flybook.model.entity.Flight;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.AirportValidationUtils.isValidAirport;

@UtilityClass
@Slf4j
public class FlightValidationUtils {
    public static boolean verifyElementInEntityToSave(Flight flight) {
        boolean isValid = isValidAirport(flight.getDepartureAirport()) || isValidAirport(flight.getArrivalAirport());
        if (!isValid){
            log.info("Flight is not valid");
        }
        return isValid;
    }
}
