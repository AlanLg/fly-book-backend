package com.flybook.service;

import com.flybook.model.dto.request.ClientDTORequest;
import com.flybook.model.dto.request.ReservationDTORequestWithExistingClient;
import com.flybook.model.dto.response.ClientDTOResponse;
import com.flybook.model.entity.Client;
import org.springframework.stereotype.Service;

@Service
public interface ClientService {
    ClientDTOResponse getClient(Long id);
    ClientDTOResponse addClient(ClientDTORequest clientDTORequest);
    ClientDTOResponse updateClient(Long id, ClientDTORequest clientDTORequest);
    void deleteClient(Long id);
    Client getClientForReservation(ReservationDTORequestWithExistingClient reservationDTORequestWithExistingClient);
}
