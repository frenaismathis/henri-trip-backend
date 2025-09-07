package com.hws.henritrip.service;

import com.hws.henritrip.dto.ActivityCreateRequest;
import com.hws.henritrip.dto.ActivityDTO;
import com.hws.henritrip.entity.Activity;
import com.hws.henritrip.entity.Guide;
import com.hws.henritrip.entity.User;
import com.hws.henritrip.mapper.ActivityMapper;
import com.hws.henritrip.repository.ActivityRepository;
import com.hws.henritrip.repository.GuideRepository;
import com.hws.henritrip.repository.UserRepository;
import com.hws.henritrip.security.RoleName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final GuideRepository guideRepository;
    private final UserRepository userRepository;

    /**
     * Récupère les activités pour un guide et un utilisateur donné.
     * Si dayNumber != null, filtre sur ce jour.
     * Tri automatique par dayNumber puis orderInDay.
     */
    public List<ActivityDTO> findByGuideIdForUser(UUID guideId, UUID requestingUserId, Integer dayNumber) {
        User user = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));

        boolean userIsAdmin = user.getRole() != null &&
                RoleName.ADMIN.name().equalsIgnoreCase(user.getRole().getName());

        boolean userHasAccess = userIsAdmin || (guide.getUsers() != null &&
                guide.getUsers().stream().anyMatch(u -> u.getId().equals(requestingUserId)));

        if (!userHasAccess) {
            throw new AccessDeniedException("User does not have access to this guide");
        }

        Stream<Activity> activityStream = activityRepository.findByGuide_IdOrderByDayNumberAscOrderInDayAsc(guideId)
                .stream();
        if (dayNumber != null) {
            activityStream = activityStream.filter(a -> a.getDayNumber() == dayNumber);
        }

        return activityStream
                .map(ActivityMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ActivityDTO createForGuide(UUID guideId, ActivityCreateRequest request) {
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guide not found: " + guideId));

        Activity activity = new Activity();
        activity.setGuide(guide);
        activity.setDayNumber(request.getDayNumber());

        ActivityMapper.updateEntity(activity, ActivityDTO.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .address(request.getAddress())
                .phone(request.getPhone())
                .openingHours(request.getOpeningHours())
                .website(request.getWebsite())
                .orderInDay(request.getOrderInDay())
                .build());

        return ActivityMapper.toDto(activityRepository.save(activity));
    }

    @Transactional
    public ActivityDTO update(UUID guideId, UUID activityId, ActivityCreateRequest request) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));

        if (!activity.getGuide().getId().equals(guideId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not belong to this guide");
        }

        ActivityMapper.updateEntity(activity, ActivityDTO.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .address(request.getAddress())
                .phone(request.getPhone())
                .openingHours(request.getOpeningHours())
                .website(request.getWebsite())
                .orderInDay(request.getOrderInDay())
                .build());

        activity.setDayNumber(request.getDayNumber());

        return ActivityMapper.toDto(activityRepository.save(activity));
    }

    @Transactional
    public void delete(UUID guideId, UUID activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));

        if (!activity.getGuide().getId().equals(guideId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not belong to this guide");
        }

        activityRepository.delete(activity);
    }
}
