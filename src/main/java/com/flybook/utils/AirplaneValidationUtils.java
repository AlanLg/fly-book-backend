package com.flybook.utils;

import com.flybook.model.dto.db.AirplaneDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.ValidationUtils.isNotEmpty;

@UtilityClass
@Slf4j
public class AirplaneValidationUtils {
    public static boolean isValidAirplane(AirplaneDTO airplaneDTO) {
        boolean isValid = airplaneDTO != null &&
                isNotEmpty(airplaneDTO.getBrand());
        if (!isValid){
            log.info("Airplane is not valid");
        }
        return isValid;
    }
}
