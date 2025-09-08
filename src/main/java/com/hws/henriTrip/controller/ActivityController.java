package com.hws.henritrip.controller;

import com.hws.henritrip.dto.ActivityCreateRequest;
import com.hws.henritrip.dto.ActivityDTO;
import com.hws.henritrip.security.UserPrincipal;
import com.hws.henritrip.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/guides/{guideId}/activities")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ActivityDTO>> listActivitiesForGuide(
            @PathVariable UUID guideId,
            @RequestParam(required = false) Integer dayNumber,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<ActivityDTO> activities = activityService.findByGuideIdForUser(
                guideId,
                userPrincipal.getUser().getId(),
                dayNumber);

        return ResponseEntity.ok(activities);
    }

    @PostMapping("/admin/guides/{guideId}/activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityDTO> createActivityForGuide(
            @PathVariable UUID guideId,
            @Valid @RequestBody ActivityCreateRequest createRequest) {

        ActivityDTO createdActivity = activityService.createForGuide(guideId, createRequest);

        return ResponseEntity.created(
                URI.create("/api/guides/" + guideId + "/activities/" + createdActivity.getId())).body(createdActivity);
    }

    @PutMapping("/admin/guides/{guideId}/activities/{activityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityDTO> updateActivityForGuide(
            @PathVariable UUID guideId,
            @PathVariable UUID activityId,
            @Valid @RequestBody ActivityCreateRequest updateRequest) {

        ActivityDTO updatedActivity = activityService.update(guideId, activityId, updateRequest);

        return ResponseEntity.ok(updatedActivity);
    }

    @DeleteMapping("/admin/guides/{guideId}/activities/{activityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteActivityForGuide(
            @PathVariable UUID guideId,
            @PathVariable UUID activityId) {

        activityService.delete(guideId, activityId);
        return ResponseEntity.noContent().build();
    }
}
