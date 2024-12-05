package com.flybook.mapper;

import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.model.entity.Client;
import com.flybook.model.entity.Flight;
import com.flybook.model.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "client.firstname", target = "client.firstname")
    @Mapping(source = "client.lastname", target = "client.lastname")
    @Mapping(source = "client.email", target = "client.email")
    @Mapping(source = "flight.departureAirport", target = "flight.departureAirport")
    @Mapping(source = "flight.arrivalAirport", target = "flight.arrivalAirport")
    @Mapping(source = "departureDateTime", target = "departureDateTime")
    ReservationDTOResponse reservationEntityToReservationDTOResponse(Reservation reservation);


    @Mapping(source = "client", target = "client")
    @Mapping(source = "flight", target = "flight")
    @Mapping(source = "departureDateTime", target = "departureDateTime")
    Reservation clientEntityAndFlightEntityToReservationEntity(Client client, Flight flight, LocalDateTime departureDateTime);
}
