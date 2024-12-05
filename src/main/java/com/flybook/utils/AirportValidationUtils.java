package com.flybook.utils;

import com.flybook.model.entity.Airport;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.ValidationUtils.isNotEmpty;

@UtilityClass
@Slf4j
public class AirportValidationUtils {
    public static boolean isValidAirport(Airport airport) {
        boolean isValid = airport != null &&
                isNotEmpty(airport.getAirportName());
        if (!isValid){
            log.info("Airport is not valid");
        }
        return isValid;
    }
}
