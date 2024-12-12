package com.flybook.mapper;

import com.flybook.model.dto.request.FlightDTORequest;
import com.flybook.model.dto.response.FlightDTOResponse;
import com.flybook.model.dto.db.FlightDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FlightMapper {
    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    @Mapping(source = "departureAirport", target = "departureAirport")
    @Mapping(source = "arrivalAirport", target = "arrivalAirport")
    @Mapping(source = "numberOfSeats", target = "numberOfSeats")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "airplane", target = "airplane")
    FlightDTO flightDTORequestToFlightEntity(FlightDTORequest flightDTORequest);

    @Mapping(source = "departureAirport", target = "departureAirport")
    @Mapping(source = "arrivalAirport", target = "arrivalAirport")
    @Mapping(source = "numberOfSeats", target = "numberOfSeats")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "airplane", target = "airplane")
    FlightDTOResponse flightEntityToFlightDTOResponse(FlightDTO flightDTO);

    List<FlightDTOResponse> flightEntitiesToFlightDTOResponses(List<FlightDTO> flightDTOS);

}
