package com.flybook.utils;

import com.flybook.model.dto.db.FlightDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.AirportValidationUtils.isValidAirport;

@UtilityClass
@Slf4j
public class FlightValidationUtils {
    public static boolean verifyElementInEntityToSave(FlightDTO flightDTO) {
        boolean isValid = isValidAirport(flightDTO.getDepartureAirportDTO()) || isValidAirport(flightDTO.getArrivalAirportDTO());
        if (!isValid){
            log.info("Flight is not valid");
        }
        return isValid;
    }
}
