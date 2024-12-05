package com.flybook.service.impl;

import com.flybook.exception.ClientAlreadyExistsException;
import com.flybook.exception.ClientNotFoundException;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDTOResponse getClient(Long id) {
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(clientRepository.findById(id).orElseThrow(ClientNotFoundException::new));
    }

    @Override
    public ClientDTOResponse addClient(ClientDTORequest clientDTORequest) throws FlybookException {
        Client createdClient = ClientMapper.INSTANCE.clientDTORequestToClientEntity(clientDTORequest);

        if (!ClientValidationUtils.isValidClient(createdClient)) {
            throw new FlybookException("missing elements in the JSON");
        }

        Optional<Client> existingClient = clientRepository.findByEmail(createdClient.getEmail());

        if (existingClient.isPresent()) {
            log.info("Client already exists with email: {}", createdClient.getEmail());
            throw new ClientAlreadyExistsException();
        }

        log.info("created client: {}", createdClient);
        Client client = clientRepository.save(createdClient);
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(client);
    }

    @Override
    public ClientDTOResponse updateClient(Long id, ClientDTORequest clientDTORequest) throws FlybookException {
        if (id == null || clientRepository.findById(id).isEmpty()) {
            throw new FlybookException("Aucun client en bdd");
        }

        Client updatedClient = ClientMapper.INSTANCE.clientDTORequestToClientEntity(clientDTORequest);

        if (!ClientValidationUtils.isValidClient(updatedClient)) {
            throw new FlybookException("missing elements in the JSON");
        }

        updatedClient.setId(id);
        clientRepository.save(updatedClient);
        return ClientMapper.INSTANCE.clientEntityToClientDTOResponse(updatedClient);
    }

    @Override
    public void deleteClient(Long id) throws FlybookException {
        if (id == null) {
            throw new FlybookException("missing elements in the JSON");
        }

        Client client = clientRepository.findById(id).orElse(null);
        if (client != null) {
            clientRepository.delete(client);
        } else {
            throw new FlybookException("Aucun client en base");
        }
    }

    @Override
    public Client getClientForReservation(ReservationDTORequestWithExistingClient reservationDTORequestWithExistingClient) {
        return clientRepository.findByEmail(reservationDTORequestWithExistingClient.getEmail())
                .orElseThrow(() -> {
                        log.info("Client pas trouve");
                        return new ClientNotFoundException();
                    }
                );
    }
}
