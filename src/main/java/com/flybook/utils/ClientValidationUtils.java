package com.flybook.utils;

import com.flybook.model.dto.db.ClientDTO;
import com.flybook.model.dto.request.ClientDTORequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static com.flybook.utils.ValidationUtils.isNotEmpty;

@UtilityClass
@Slf4j
public class ClientValidationUtils {
    public static boolean isValidClient(ClientDTO clientDTO) {
        boolean isValid = clientDTO != null &&
                isNotEmpty(clientDTO.getFirstname()) &&
                isNotEmpty(clientDTO.getLastname()) &&
                isNotEmpty(clientDTO.getEmail());
        if (!isValid){
            log.info("Client is not valid");
        }
        return isValid;
    }

    public static boolean isValidClientDTORequest(ClientDTORequest clientDTORequest) {
        boolean isValid = clientDTORequest != null &&
                isNotEmpty(clientDTORequest.getFirstname()) &&
                isNotEmpty(clientDTORequest.getLastname()) &&
                isNotEmpty(clientDTORequest.getEmail()) &&
                isNotEmpty(clientDTORequest.getPassword()) &&
                isNotEmpty(clientDTORequest.getConfirmPassword()) &&
                clientDTORequest.getPassword().equals(clientDTORequest.getConfirmPassword());
        if (!isValid){
            log.info("Client is not valid");
        }
        return isValid;
    }
}
