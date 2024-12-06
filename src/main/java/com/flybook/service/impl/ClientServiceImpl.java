package com.flybook.service.impl;

import com.flybook.exception.FlybookException;
import com.flybook.mapper.ClientMapper;
import com.flybook.model.dto.request.ClientDTORequest;
import com.flybook.model.dto.request.ReservationDTORequestWithExistingClient;
import com.flybook.model.dto.response.ClientDTOResponse;
import com.flybook.model.entity.Client;
import com.flybook.repository.ClientRepository;
import com.flybook.service.ClientService;
import com.flybook.utils.ClientValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ClientDTOResponse getClient(Long id) {
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(clientRepository.findById(id).orElseThrow(() -> new FlybookException("Aucun client en bdd", HttpStatus.NOT_FOUND)));
    }

    @Override
    public ClientDTOResponse addClient(ClientDTORequest clientDTORequest) throws FlybookException {
        if (!ClientValidationUtils.isValidClientDTORequest(clientDTORequest)) {
            throw new FlybookException("missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Client createdClient = ClientMapper.INSTANCE.clientDTORequestToClientEntity(clientDTORequest);

        Optional<Client> existingClient = clientRepository.findByEmail(createdClient.getEmail());

        if (existingClient.isPresent()) {
            log.info("Client already exists with email: {}", createdClient.getEmail());
            throw new FlybookException("Client already exists with email", HttpStatus.CONFLICT);
        }

        log.info("created client: {}", createdClient);
        createdClient.setPassword(passwordEncoder.encode(createdClient.getPassword()));
        Client client = clientRepository.save(createdClient);
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(client);
    }

    @Override
    public ClientDTOResponse updateClient(Long id, ClientDTORequest clientDTORequest) throws FlybookException {
        if (id == null || clientRepository.findById(id).isEmpty()) {
            throw new FlybookException("Aucun client en bdd", HttpStatus.NOT_FOUND);
        }

        Client updatedClient = ClientMapper.INSTANCE.clientDTORequestToClientEntity(clientDTORequest);

        if (!ClientValidationUtils.isValidClient(updatedClient)) {
            throw new FlybookException("missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        updatedClient.setId(id);
        clientRepository.save(updatedClient);
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(updatedClient);
    }

    @Override
    public void deleteClient(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("missing elements in the JSON", HttpStatus.BAD_REQUEST);
        }

        Client client = clientRepository.findById(id).orElse(null);
        if (client != null) {
            clientRepository.delete(client);
        } else {
            throw new FlybookException("Aucun client en base", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Client getClientForReservation(ReservationDTORequestWithExistingClient reservationDTORequestWithExistingClient) {
        return clientRepository.findByEmail(reservationDTORequestWithExistingClient.getEmail())
                .orElseThrow(() -> {
                        log.info("Client pas trouve");
                        return new FlybookException("Client pas trouve", HttpStatus.NOT_FOUND);
                    }
                );
    }
}
