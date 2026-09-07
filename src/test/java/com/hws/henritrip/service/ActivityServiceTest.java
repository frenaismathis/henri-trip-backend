package com.hws.henritrip.service;

import com.hws.henritrip.dto.ActivityDTO;
import com.hws.henritrip.entity.Activity;
import com.hws.henritrip.entity.Guide;
import com.hws.henritrip.entity.Role;
import com.hws.henritrip.entity.User;
import com.hws.henritrip.repository.ActivityRepository;
import com.hws.henritrip.repository.GuideRepository;
import com.hws.henritrip.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private GuideRepository guideRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ActivityService activityService;

    private Role userRole() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("USER");
        return role;
    }

    @Test
    void findByIdForUser_withExistingActivityAndAccess_returnsMappedDto() {
        UUID guideId = UUID.randomUUID();
        UUID activityId = UUID.randomUUID();
        UUID requestingUserId = UUID.randomUUID();

        User requestingUser = new User();
        requestingUser.setId(requestingUserId);
        requestingUser.setRole(userRole());

        Guide guide = new Guide();
        guide.setId(guideId);
        guide.setUsers(Set.of(requestingUser));

        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setGuide(guide);
        activity.setTitle("Visit the old town");
        activity.setDescription("Walking tour through the historic center");
        activity.setCategory("Sightseeing");
        activity.setAddress("Main square");
        activity.setDayNumber(1);
        activity.setOrderInDay(1);

        when(userRepository.findById(requestingUserId)).thenReturn(Optional.of(requestingUser));
        when(guideRepository.findById(guideId)).thenReturn(Optional.of(guide));
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activity));

        ActivityDTO result = activityService.findByIdForUser(guideId, activityId, requestingUserId);

        assertThat(result.getId()).isEqualTo(activityId);
        assertThat(result.getTitle()).isEqualTo("Visit the old town");
        assertThat(result.getGuideId()).isEqualTo(guideId);
    }

    @Test
    void findByIdForUser_withUnknownActivityId_throwsNotFound() {
        UUID guideId = UUID.randomUUID();
        UUID activityId = UUID.randomUUID();
        UUID requestingUserId = UUID.randomUUID();

        User requestingUser = new User();
        requestingUser.setId(requestingUserId);
        requestingUser.setRole(userRole());

        Guide guide = new Guide();
        guide.setId(guideId);
        guide.setUsers(Set.of(requestingUser));

        when(userRepository.findById(requestingUserId)).thenReturn(Optional.of(requestingUser));
        when(guideRepository.findById(guideId)).thenReturn(Optional.of(guide));
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.findByIdForUser(guideId, activityId, requestingUserId))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(404));
    }
}
