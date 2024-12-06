package com.flybook.controller;

import com.flybook.exception.FlybookException;
import com.flybook.model.dto.request.AuthDTORequest;
import com.flybook.model.dto.request.ClientDTORequest;
import com.flybook.model.dto.response.ClientDTOResponse;
import com.flybook.service.ClientService;
import com.flybook.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final JwtService jwtService;
    private final ClientService clientService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/generate-token")
    public String authenticateAndGetToken(@RequestBody AuthDTORequest authRequest) {
        log.info("Fetching token for user {}", authRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        log.info("Authentication {}", authentication);

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ClientDTOResponse> getClient(@PathVariable Long id) throws FlybookException {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @PostMapping("/add")
    public ResponseEntity<ClientDTOResponse> addClient(@Valid @RequestBody ClientDTORequest clientDTORequest) throws FlybookException {
        log.info("Adding client: {}", clientDTORequest.toString());
        return ResponseEntity.ok(clientService.addClient(clientDTORequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClientDTOResponse> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTORequest clientDTORequest) throws FlybookException {
        log.info("Updating client: {}", clientDTORequest.toString());
        return ResponseEntity.ok(clientService.updateClient(id, clientDTORequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Deleting client {}", id);
        try {
            clientService.deleteClient(id);
            return ResponseEntity.accepted().build();
        } catch (FlybookException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
