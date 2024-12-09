package com.flybook.utils;

import com.flybook.model.entity.Airplane;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.ValidationUtils.isNotEmpty;

@UtilityClass
@Slf4j
public class AirplaneValidationUtils {
    public static boolean isValidAirplane(Airplane airplane) {
        boolean isValid = airplane != null &&
                isNotEmpty(airplane.getBrand());
        if (!isValid){
            log.info("Airplane is not valid");
        }
        return isValid;
    }
}
