package com.flybook.service.impl;

import com.flybook.model.ClientInfoDetails;
import com.flybook.model.entity.Client;
import com.flybook.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientAuthServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.findByEmail(email);

        return client.map(ClientInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found" + email));
    }
}
