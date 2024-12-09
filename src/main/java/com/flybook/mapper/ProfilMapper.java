package com.flybook.mapper;

import com.flybook.model.dto.request.AirplaneDTORequest;
import com.flybook.model.dto.request.ProfilDTORequest;
import com.flybook.model.dto.response.AirplaneDTOResponse;
import com.flybook.model.dto.response.ProfilDTOResponse;
import com.flybook.model.entity.Airplane;
import com.flybook.model.entity.Profil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProfilMapper {
    ProfilMapper INSTANCE = Mappers.getMapper(ProfilMapper.class);

    Profil profilDTORequestToProfilEntity(ProfilDTORequest profilDTORequest);

    ProfilDTOResponse profilEntityToProfilDTOResponse(Profil profil);

    List<Profil> profilDTORequestListToProfilListEntity(List<ProfilDTORequest> profilDTORequestList);
}
