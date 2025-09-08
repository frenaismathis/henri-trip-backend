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

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final GuideRepository guideRepository;
    private final UserRepository userRepository;

    public List<ActivityDTO> findByGuideIdForUser(UUID guideId, UUID requestingUserId, Integer dayNumber) {
        User user = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));

        boolean isAdmin = user.getRole() != null &&
                RoleName.ADMIN.name().equalsIgnoreCase(user.getRole().getName());
        boolean hasAccess = isAdmin ||
                guide.getUsers().stream().anyMatch(u -> u.getId().equals(requestingUserId));

        if (!hasAccess) {
            throw new AccessDeniedException("User does not have access to this guide");
        }

        Stream<Activity> activityStream = guide.getActivities().stream()
                .sorted(Comparator
                        .comparingInt(Activity::getDayNumber)
                        .thenComparingInt(Activity::getOrderInDay));

        if (dayNumber != null) {
            activityStream = activityStream.filter(a -> a.getDayNumber() == dayNumber);
        }

        return activityStream.map(ActivityMapper::toDto).toList();
    }

    @Transactional
    public ActivityDTO createForGuide(UUID guideId, ActivityCreateRequest request) {
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found: " + guideId));

        Activity activity = new Activity();
        activity.setGuide(guide);

        ActivityMapper.updateEntity(activity, request);

        return ActivityMapper.toDto(activityRepository.save(activity));
    }

    @Transactional
    public ActivityDTO update(UUID guideId, UUID activityId, ActivityCreateRequest request) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));

        if (!activity.getGuide().getId().equals(guideId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activity does not belong to this guide");
        }

        ActivityMapper.updateEntity(activity, request);

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
