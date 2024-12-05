package com.flybook.mapper;

import com.flybook.model.dto.request.ClientDTORequest;
import com.flybook.model.dto.response.ClientDTOResponse;
import com.flybook.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "email", target = "email")
    Client clientDTORequestToClientEntity(ClientDTORequest clientDTORequest);

    @Mapping(source = "firstname", target = "firstname")
    @Mapping(source = "lastname", target = "lastname")
    @Mapping(source = "email", target = "email")
    ClientDTOResponse clientEntityToClientDTOResponse(Client client);
}
