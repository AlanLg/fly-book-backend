package com.flybook.dbaccess;

import com.flybook.model.dto.db.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "Client", path = "/db-access/client", url = "localhost:8081")
public interface ClientDbAccess {

    @GetMapping("/id/{id}")
    Optional<ClientDTO> findById(@PathVariable Long id);

    @GetMapping("/email/{email}")
    Optional<ClientDTO> findByEmail(@PathVariable String email);

    @DeleteMapping("/id/{id}")
    void deleteClient(@PathVariable Long id);

    @PostMapping("")
    ClientDTO saveClient(@RequestBody ClientDTO clientDTO);
}
