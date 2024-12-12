package com.flybook.service.impl;

import com.flybook.dbaccess.ClientDbAccess;
import com.flybook.exception.FlybookException;
import com.flybook.mapper.ClientMapper;
import com.flybook.model.dto.db.ClientDTO;
import com.flybook.model.dto.request.ClientDTORequest;
import com.flybook.model.dto.response.ClientDTOResponse;
import com.flybook.service.ClientService;
import com.flybook.utils.ClientValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientDbAccess clientDbAccess;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ClientDTOResponse getClient(Long id) {
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(clientDbAccess.findById(id)
                .orElseThrow(() -> new FlybookException("Client with id: " + id + " not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public ClientDTOResponse addClient(ClientDTORequest clientDTORequest) throws FlybookException {
        if (!ClientValidationUtils.isValidClientDTORequest(clientDTORequest)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        ClientDTO createdClientDTO = ClientMapper.INSTANCE.clientDTORequestToClientEntity(clientDTORequest);

        Optional<ClientDTO> existingClient = clientDbAccess.findByEmail(createdClientDTO.getEmail());

        if (existingClient.isPresent()) {
            log.info("Client already exists with email: {}", createdClientDTO.getEmail());
            throw new FlybookException("Client already exists with email", HttpStatus.CONFLICT);
        }

        log.info("created client: {}", createdClientDTO);
        createdClientDTO.setPassword(passwordEncoder.encode(createdClientDTO.getPassword()));
        ClientDTO clientDTO = clientDbAccess.saveClient(createdClientDTO);
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(clientDTO);
    }

    @Override
    public ClientDTOResponse updateClient(Long id, ClientDTORequest clientDTORequest) throws FlybookException {
        if (id == null || clientDbAccess.findById(id).isEmpty()) {
            throw new FlybookException("No client in the data base", HttpStatus.NOT_FOUND);
        }

        ClientDTO updatedClientDTO = ClientMapper.INSTANCE.clientDTORequestToClientEntity(clientDTORequest);

        if (!ClientValidationUtils.isValidClient(updatedClientDTO)) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        updatedClientDTO.setId(id);
        clientDbAccess.saveClient(updatedClientDTO);
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(updatedClientDTO);
    }

    @Override
    public void deleteClient(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("Missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        ClientDTO clientDTO = clientDbAccess.findById(id).orElse(null);
        if (clientDTO != null) {
            clientDbAccess.deleteClient(clientDTO.getId());
        } else {
            throw new FlybookException("No client in the data base", HttpStatus.NOT_FOUND);
        }
    }

    public ClientDTO getClientForReservation(String email) {
        return clientDbAccess.findByEmail(email)
                .orElseThrow(() -> new FlybookException("Client with email: " + email + " not found", HttpStatus.NOT_FOUND)
                );
    }
}
