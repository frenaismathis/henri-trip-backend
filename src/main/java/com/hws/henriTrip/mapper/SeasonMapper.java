package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.SeasonDTO;
import com.hws.henritrip.entity.Season;

public class SeasonMapper {

    public static SeasonDTO toDto(Season season) {
        return SeasonDTO.builder()
                .id(season.getId())
                .name(season.getName())
                .build();
    }

    public static Season toEntity(SeasonDTO dto) {
        Season season = new Season();
        season.setId(dto.getId());
        season.setName(dto.getName());
        return season;
    }
}
