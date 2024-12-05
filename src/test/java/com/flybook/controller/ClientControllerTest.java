package com.flybook.controller;
import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.ClientDTORequest;
import com.flybook.model.dto.response.ClientDTOResponse;
import com.flybook.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addClientReturnsClientResponseSuccessfully() throws FlybookException {
        ClientDTORequest request = new ClientDTORequest();
        ClientDTOResponse expectedResponse = new ClientDTOResponse();
        when(clientService.addClient(request)).thenReturn(expectedResponse);

        ResponseEntity<ClientDTOResponse> response = clientController.addClient(request);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(clientService, times(1)).addClient(request);
    }

    @Test
    void updateClientReturnsClientResponseSuccessfully() throws FlybookException {
        Long id = 1L;
        ClientDTORequest request = new ClientDTORequest();
        ClientDTOResponse expectedResponse = new ClientDTOResponse();
        when(clientService.updateClient(id, request)).thenReturn(expectedResponse);

        ResponseEntity<ClientDTOResponse> response = clientController.updateClient(id, request);

        assertEquals(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(clientService, times(1)).updateClient(id, request);
    }

    @Test
    void deleteClientReturnsAcceptedStatusSuccessfully() {
        Long id = 1L;

        ResponseEntity<Void> response = clientController.deleteClient(id);

        assertEquals(202, response.getStatusCodeValue());
        verify(clientService, times(1)).deleteClient(id);
    }

    @Test
    void deleteClientReturnsBadRequestWhenExceptionThrown() {
        Long id = 1L;
        doThrow(new FlybookException("")).when(clientService).deleteClient(id);

        ResponseEntity<Void> response = clientController.deleteClient(id);

        assertEquals(400, response.getStatusCodeValue());
        verify(clientService, times(1)).deleteClient(id);
    }
}