package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.ActivityDTO;
import com.hws.henritrip.dto.ActivityCreateRequest;
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
                .dayNumber(activity.getDayNumber())
                .orderInDay(activity.getOrderInDay())
                .guideId(activity.getGuide() != null ? activity.getGuide().getId() : null)
                .build();
    }

    public static void updateEntity(Activity activity, ActivityCreateRequest request) {
        if (request.getTitle() != null)
            activity.setTitle(request.getTitle());
        if (request.getDescription() != null)
            activity.setDescription(request.getDescription());
        if (request.getCategory() != null)
            activity.setCategory(request.getCategory());
        if (request.getAddress() != null)
            activity.setAddress(request.getAddress());
        if (request.getPhone() != null)
            activity.setPhone(request.getPhone());
        if (request.getOpeningHours() != null)
            activity.setOpeningHours(request.getOpeningHours());
        if (request.getWebsite() != null)
            activity.setWebsite(request.getWebsite());
        if (request.getDayNumber() > 0)
            activity.setDayNumber(request.getDayNumber());
        if (request.getOrderInDay() > 0)
            activity.setOrderInDay(request.getOrderInDay());
    }
}
