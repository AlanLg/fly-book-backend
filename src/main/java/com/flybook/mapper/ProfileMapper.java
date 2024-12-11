package com.flybook.mapper;

import com.flybook.model.dto.db.ProfileDTO;
import com.flybook.model.dto.request.ProfileDTORequest;
import com.flybook.model.dto.response.ProfileDTOResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileDTO profileDTORequestToProfileEntity(ProfileDTORequest profileDTORequest);

    ProfileDTOResponse profileEntityToProfileDTOResponse(ProfileDTO profileDTO);

    List<ProfileDTO> profileDTORequestListToProfileListEntity(List<ProfileDTORequest> profileDTORequestList);
}
