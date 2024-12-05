package com.flybook.utils;

import com.flybook.model.entity.Client;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.ValidationUtils.isNotEmpty;

@UtilityClass
@Slf4j
public class ClientValidationUtils {
    public static boolean isValidClient(Client client) {
        boolean isValid = client != null &&
                isNotEmpty(client.getFirstname()) &&
                isNotEmpty(client.getLastname()) &&
                isNotEmpty(client.getEmail());
        if (!isValid){
            log.info("Client is not valid");
        }
        return isValid;
    }
}
