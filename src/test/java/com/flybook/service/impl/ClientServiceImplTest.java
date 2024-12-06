package com.flybook.service.impl;

import com.flybook.exception.ClientAlreadyExistsException;
import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.ClientDTORequest;
import com.flybook.model.entity.Client;
import com.flybook.repository.ClientRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceImplTest {

    private final static EasyRandom generator = new EasyRandom();
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addClientSuccessfully() {
        ClientDTORequest clientDTORequest = generator.nextObject(ClientDTORequest.class);
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> clientService.addClient(clientDTORequest));
    }

    @Test
    public void addClientThrowsExceptionWhenClientExists() {
        ClientDTORequest clientDTORequest = generator.nextObject(ClientDTORequest.class);
        clientDTORequest.setEmail("test@test.com");

        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.of(new Client()));

        assertThrows(ClientAlreadyExistsException.class, () -> clientService.addClient(clientDTORequest));
    }

    @Test
    public void addClientThrowsExceptionWhenPasswordsDoNotMatch() {
        ClientDTORequest clientDTORequest = generator.nextObject(ClientDTORequest.class);
        clientDTORequest.setPassword("password123");
        clientDTORequest.setConfirmPassword("differentPassword");

        assertThrows(FlybookException.class, () -> clientService.addClient(clientDTORequest));
    }


    @Test
    public void updateClientSuccessfully() {
        ClientDTORequest clientDTORequest = generator.nextObject(ClientDTORequest.class);
        clientDTORequest.setEmail("test@test.com");

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(new Client()));

        assertDoesNotThrow(() -> clientService.updateClient(1L, clientDTORequest));
    }

    @Test
    public void updateClientThrowsExceptionWhenClientNotFound() {
        ClientDTORequest clientDTORequest = new ClientDTORequest();
        clientDTORequest.setEmail("test@test.com");

        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FlybookException.class, () -> clientService.updateClient(1L, clientDTORequest));
    }

    @Test
    public void deleteClientSuccessfully() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(new Client()));

        assertDoesNotThrow(() -> clientService.deleteClient(1L));
    }

    @Test
    public void deleteClientThrowsExceptionWhenClientNotFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FlybookException.class, () -> clientService.deleteClient(1L));
    }
}