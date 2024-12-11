package com.flybook.mapper;

import com.flybook.model.dto.db.ProfileDTO;
import com.flybook.model.dto.request.ProfilDTORequest;
import com.flybook.model.dto.response.ProfilDTOResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProfilMapper {
    ProfilMapper INSTANCE = Mappers.getMapper(ProfilMapper.class);

    ProfileDTO profilDTORequestToProfilEntity(ProfilDTORequest profilDTORequest);

    ProfilDTOResponse profilEntityToProfilDTOResponse(ProfileDTO profileDTO);

    List<ProfileDTO> profilDTORequestListToProfilListEntity(List<ProfilDTORequest> profilDTORequestList);
}
