package com.flybook.mapper;

import com.flybook.model.dto.request.ProfilDTORequest;
import com.flybook.model.dto.response.ProfilDTOResponse;
import com.flybook.model.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProfilMapper {
    ProfilMapper INSTANCE = Mappers.getMapper(ProfilMapper.class);

    Profile profilDTORequestToProfilEntity(ProfilDTORequest profilDTORequest);

    ProfilDTOResponse profilEntityToProfilDTOResponse(Profile profile);

    List<Profile> profilDTORequestListToProfilListEntity(List<ProfilDTORequest> profilDTORequestList);
}
