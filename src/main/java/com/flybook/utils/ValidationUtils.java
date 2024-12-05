package com.flybook.utils;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class ValidationUtils {
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isPresentAndNotEmpty(Optional<String> optional) {
        return optional.isPresent() && isNotEmpty(optional.get());
    }
}
