package com.flybook.service.impl;

import com.flybook.dbaccess.ClientDbAccess;
import com.flybook.model.ClientInfoDetails;
import com.flybook.model.dto.db.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientAuthServiceImpl implements UserDetailsService {

    @Autowired
    private ClientDbAccess clientDbAccess;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<ClientDTO> client = clientDbAccess.findByEmail(email);

        return client.map(ClientInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("No client in the data base" + email));
    }
}
