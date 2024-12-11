package com.flybook.mapper;

import com.flybook.model.dto.db.AirportDTO;
import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AirportMapper {
    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    @Mapping(source = "airportName", target = "airportName")
    AirportDTO airportDTORequestToAirportEntity(AirportDTORequest airportDTORequest);

    @Mapping(source = "airportName", target = "airportName")
    AirportDTOResponse airportEntityToAirportDTOResponse(AirportDTO airportDTO);

    List<AirportDTOResponse> airportEntitiesToAirportDTOResponses(List<AirportDTO> airportDTOS);
}
