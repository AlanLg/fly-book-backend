package com.flybook.mapper;

import com.flybook.model.dto.request.AirportDTORequest;
import com.flybook.model.dto.response.AirportDTOResponse;
import com.flybook.model.dto.response.FlightDTOResponse;
import com.flybook.model.entity.Airport;
import com.flybook.model.entity.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AirportMapper {
    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    @Mapping(source = "airportName", target = "airportName")
    Airport airportDTORequestToAirportEntity(AirportDTORequest airportDTORequest);

    @Mapping(source = "airportName", target = "airportName")
    AirportDTOResponse airportEntityToAirportDTOResponse(Airport airport);

    List<AirportDTOResponse> airportEntitiesToAirportDTOResponses(List<Airport> airports);
}
