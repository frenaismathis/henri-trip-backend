package com.hws.henritrip.service;

import com.hws.henritrip.dto.GuideCreateRequest;
import com.hws.henritrip.dto.GuideDTO;
import com.hws.henritrip.entity.Audience;
import com.hws.henritrip.entity.Guide;
import com.hws.henritrip.entity.Mobility;
import com.hws.henritrip.entity.Season;
import com.hws.henritrip.entity.User;
import com.hws.henritrip.repository.GuideRepository;
import com.hws.henritrip.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuideServiceTest {

    @Mock
    private GuideRepository guideRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MobilityService mobilityService;

    @Mock
    private SeasonService seasonService;

    @Mock
    private AudienceService audienceService;

    @Mock
    private AccessService accessService;

    @InjectMocks
    private GuideService guideService;

    private Guide guide(UUID id) {
        Guide guide = new Guide();
        guide.setId(id);
        guide.setTitle("Discover Kyoto");
        guide.setDescription("A three day walking tour");
        guide.setDaysCount(3);
        guide.setMobilityOptions(Set.of());
        guide.setSeasons(Set.of());
        guide.setAudiences(Set.of());
        return guide;
    }

    @Test
    void findAll_returnsAllGuidesMappedToDto() {
        Guide guide1 = guide(UUID.randomUUID());
        Guide guide2 = guide(UUID.randomUUID());
        when(guideRepository.findAll()).thenReturn(List.of(guide1, guide2));

        List<GuideDTO> result = guideService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(GuideDTO::getId)
                .containsExactlyInAnyOrder(guide1.getId(), guide2.getId());
    }

    @Test
    void findById_withExistingId_returnsMappedDto() {
        UUID id = UUID.randomUUID();
        Guide guide = guide(id);
        when(guideRepository.findById(id)).thenReturn(Optional.of(guide));

        GuideDTO result = guideService.findById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo("Discover Kyoto");
        assertThat(result.getDaysCount()).isEqualTo(3);
    }

    @Test
    void findById_withUnknownId_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(guideRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> guideService.findById(id))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(404));
    }

    @Test
    void createFromRequest_withValidDataAndAdminCreator_persistsAndReturnsDto() {
        UUID creatorId = UUID.randomUUID();
        UUID mobilityId = UUID.randomUUID();
        UUID seasonId = UUID.randomUUID();
        UUID audienceId = UUID.randomUUID();

        Mobility mobility = new Mobility();
        mobility.setId(mobilityId);
        mobility.setName("Walking");
        Season season = new Season();
        season.setId(seasonId);
        season.setName("Summer");
        Audience audience = new Audience();
        audience.setId(audienceId);
        audience.setName("Families");

        User creator = new User();
        creator.setId(creatorId);

        GuideCreateRequest request = GuideCreateRequest.builder()
                .title("Discover Kyoto")
                .description("A three day walking tour")
                .daysCount(3)
                .mobilityOptionIds(Set.of(mobilityId))
                .seasonIds(Set.of(seasonId))
                .audienceIds(Set.of(audienceId))
                .build();

        doNothing().when(accessService).checkUserIsAdmin(creatorId);
        when(mobilityService.getById(mobilityId)).thenReturn(mobility);
        when(seasonService.getById(seasonId)).thenReturn(season);
        when(audienceService.getById(audienceId)).thenReturn(audience);
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        when(guideRepository.save(any(Guide.class))).thenAnswer(invocation -> {
            Guide saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        GuideDTO result = guideService.createFromRequest(request, creatorId);

        ArgumentCaptor<Guide> guideCaptor = ArgumentCaptor.forClass(Guide.class);
        verify(guideRepository).save(guideCaptor.capture());
        Guide persisted = guideCaptor.getValue();
        assertThat(persisted.getCreatedBy()).isEqualTo(creator);
        assertThat(persisted.getMobilityOptions()).containsExactly(mobility);
        assertThat(persisted.getSeasons()).containsExactly(season);
        assertThat(persisted.getAudiences()).containsExactly(audience);

        assertThat(result.getId()).isEqualTo(persisted.getId());
        assertThat(result.getTitle()).isEqualTo("Discover Kyoto");
        assertThat(result.getMobilityOptions()).containsExactly("Walking");
    }

    @Test
    void createFromRequest_withNonAdminCreator_throwsForbiddenAndDoesNotPersist() {
        UUID creatorId = UUID.randomUUID();
        GuideCreateRequest request = GuideCreateRequest.builder()
                .title("Discover Kyoto")
                .description("A three day walking tour")
                .daysCount(3)
                .mobilityOptionIds(Set.of())
                .seasonIds(Set.of())
                .audienceIds(Set.of())
                .build();

        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,
                "Only admin users can perform this action"))
                .when(accessService).checkUserIsAdmin(creatorId);

        assertThatThrownBy(() -> guideService.createFromRequest(request, creatorId))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(403));

        verify(guideRepository, never()).save(any());
    }
}
