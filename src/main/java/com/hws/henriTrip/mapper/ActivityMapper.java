package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.ActivityDTO;
import com.hws.henritrip.entity.Activity;

public class ActivityMapper {

    private ActivityMapper() {
    }

    public static ActivityDTO toDto(Activity activity) {
        if (activity == null)
            return null;
        return ActivityDTO.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .category(activity.getCategory())
                .address(activity.getAddress())
                .phone(activity.getPhone())
                .openingHours(activity.getOpeningHours())
                .website(activity.getWebsite())
                .orderInDay(activity.getOrderInDay())
                .guideId(activity.getGuide() != null ? activity.getGuide().getId() : null)
                .build();
    }

    public static void updateEntity(Activity activity, ActivityDTO dto) {
        if (dto.getTitle() != null)
            activity.setTitle(dto.getTitle());
        if (dto.getDescription() != null)
            activity.setDescription(dto.getDescription());
        if (dto.getCategory() != null)
            activity.setCategory(dto.getCategory());
        if (dto.getAddress() != null)
            activity.setAddress(dto.getAddress());
        if (dto.getPhone() != null)
            activity.setPhone(dto.getPhone());
        if (dto.getOpeningHours() != null)
            activity.setOpeningHours(dto.getOpeningHours());
        if (dto.getWebsite() != null)
            activity.setWebsite(dto.getWebsite());
        if (dto.getOrderInDay() > 0)
            activity.setOrderInDay(dto.getOrderInDay());
    }
}
