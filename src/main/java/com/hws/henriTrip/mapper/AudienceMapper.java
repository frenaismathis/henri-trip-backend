package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.AudienceDTO;
import com.hws.henritrip.entity.Audience;

public class AudienceMapper {

    public static AudienceDTO toDto(Audience audience) {
        return AudienceDTO.builder()
                .id(audience.getId())
                .name(audience.getName())
                .build();
    }

    public static Audience toEntity(AudienceDTO dto) {
        Audience audience = new Audience();
        audience.setId(dto.getId());
        audience.setName(dto.getName());
        return audience;
    }
}
