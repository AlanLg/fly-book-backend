package com.flybook.utils;

import com.flybook.model.dto.db.AirportDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.ValidationUtils.isNotEmpty;

@UtilityClass
@Slf4j
public class AirportValidationUtils {
    public static boolean isValidAirport(AirportDTO airportDTO) {
        boolean isValid = airportDTO != null &&
                isNotEmpty(airportDTO.getAirportName());
        if (!isValid){
            log.info("Airport is not valid");
        }
        return isValid;
    }
}
