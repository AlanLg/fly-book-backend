package com.flybook.mapper;

import com.flybook.model.dto.request.ReservationDTORequest;
import com.flybook.model.dto.response.ReservationDTOResponse;
import com.flybook.model.entity.Client;
import com.flybook.model.entity.Flight;
import com.flybook.model.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mapping(source = "client.firstname", target = "client.firstname")
    @Mapping(source = "client.lastname", target = "client.lastname")
    @Mapping(source = "client.email", target = "client.email")
    @Mapping(source = "flight.departureAirport", target = "flight.departureAirport")
    @Mapping(source = "flight.arrivalAirport", target = "flight.arrivalAirport")
    @Mapping(source = "departureDate", target = "departureDate")
    @Mapping(source = "nbLuggage", target = "nbLuggage")
    @Mapping(source = "profiles", target = "profilDTOResponseList")
    ReservationDTOResponse reservationEntityToReservationDTOResponse(Reservation reservation);
    List<ReservationDTOResponse> reservationEntityToReservationDTOResponse(List<Reservation> reservation);


    @Mapping(source = "client", target = "client")
    @Mapping(source = "flight", target = "flight")
    @Mapping(source = "reservationDTORequest.departureDate", target = "departureDate")
    Reservation clientEntityAndFlightEntityAndReservationDTORequestToReservationEntity(Client client, Flight flight, ReservationDTORequest reservationDTORequest);
}
