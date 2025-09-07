package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.GuideDTO;
import com.hws.henritrip.entity.Guide;

import java.util.stream.Collectors;

public class GuideMapper {

    private GuideMapper() {
    }

    public static GuideDTO toDto(Guide guide) {
        if (guide == null)
            return null;
        return GuideDTO.builder()
                .id(guide.getId())
                .title(guide.getTitle())
                .description(guide.getDescription())
                .daysCount(guide.getDaysCount())
                .mobilityOptionIds(guide.getMobilityOptions() != null
                        ? guide.getMobilityOptions().stream().map(m -> m.getId()).collect(Collectors.toSet())
                        : java.util.Set.of())
                .seasonIds(guide.getSeasons() != null
                        ? guide.getSeasons().stream().map(s -> s.getId()).collect(Collectors.toSet())
                        : java.util.Set.of())
                .audienceIds(guide.getAudiences() != null
                        ? guide.getAudiences().stream().map(a -> a.getId()).collect(Collectors.toSet())
                        : java.util.Set.of())
                .build();
    }

    public static void updateEntity(Guide guide, GuideDTO dto) {
        if (dto.getTitle() != null)
            guide.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            guide.setDescription(dto.getDescription());
        if (dto.getDaysCount() > 0)
            guide.setDaysCount(dto.getDaysCount());
    }
}
