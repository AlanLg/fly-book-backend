package com.flybook.mapper;

import com.flybook.model.dto.db.AirplaneDTO;
import com.flybook.model.dto.request.AirplaneDTORequest;
import com.flybook.model.dto.response.AirplaneDTOResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirplaneMapper {
    AirplaneMapper INSTANCE = Mappers.getMapper(AirplaneMapper.class);

    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "model", target = "model")
    AirplaneDTO airplaneDTORequestToAirplaneEntity(AirplaneDTORequest airplaneDTORequest);

    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "model", target = "model")
    AirplaneDTOResponse airplaneEntityToAirplaneDTOResponse(AirplaneDTO airplaneDTO);
}
