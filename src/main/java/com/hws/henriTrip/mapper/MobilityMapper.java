package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.MobilityDTO;
import com.hws.henritrip.entity.Mobility;

public class MobilityMapper {

    public static MobilityDTO toDto(Mobility mobility) {
        return MobilityDTO.builder()
                .id(mobility.getId())
                .name(mobility.getName())
                .build();
    }

    public static Mobility toEntity(MobilityDTO dto) {
        Mobility mobility = new Mobility();
        mobility.setId(dto.getId());
        mobility.setName(dto.getName());
        return mobility;
    }
}
