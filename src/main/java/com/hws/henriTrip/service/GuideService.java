package com.hws.henritrip.service;

import com.hws.henritrip.dto.GuideCreateRequest;
import com.hws.henritrip.dto.GuideDTO;
import com.hws.henritrip.entity.*;
import com.hws.henritrip.mapper.GuideMapper;
import com.hws.henritrip.repository.GuideRepository;
import com.hws.henritrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuideService {

    private final GuideRepository guideRepository;
    private final UserRepository userRepository;
    private final MobilityService mobilityService;
    private final SeasonService seasonService;
    private final AudienceService audienceService;
    private final AccessService accessService;

    public List<GuideDTO> findAll() {
        return guideRepository.findAll().stream()
                .map(GuideMapper::toDto)
                .collect(Collectors.toList());
    }

    public GuideDTO findById(UUID guideId) {
        return guideRepository.findById(guideId)
                .map(GuideMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found: " + guideId));
    }

    public List<GuideDTO> findAllVisibleToUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean isAdmin = user.getRole() != null &&
                "ADMIN".equalsIgnoreCase(user.getRole().getName());

        if (isAdmin)
            return findAll();

        return guideRepository.findAllByUsers_Id(userId).stream()
                .map(GuideMapper::toDto)
                .collect(Collectors.toList());
    }

    public GuideDTO findByIdForUser(UUID guideId, UUID requestingUserId) {
        accessService.checkUserAccess(guideId, requestingUserId);
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));
        return GuideMapper.toDto(guide);
    }

    @Transactional
    public GuideDTO createFromRequest(GuideCreateRequest request, UUID creatorUserId) {
        accessService.checkUserIsAdmin(creatorUserId);

        Guide guide = new Guide();
        guide.setTitle(request.getTitle());
        guide.setDescription(request.getDescription());
        guide.setDaysCount(request.getDaysCount());
        guide.setMobilityOptions(resolveMobilityIds(request.getMobilityOptionIds()));
        guide.setSeasons(resolveSeasonIds(request.getSeasonIds()));
        guide.setAudiences(resolveAudienceIds(request.getAudienceIds()));

        userRepository.findById(creatorUserId).ifPresent(guide::setCreatedBy);

        return GuideMapper.toDto(guideRepository.save(guide));
    }

    @Transactional
    public GuideDTO updateFromRequest(UUID guideId, GuideCreateRequest request) {
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found: " + guideId));

        guide.setTitle(request.getTitle());
        guide.setDescription(request.getDescription());
        guide.setDaysCount(request.getDaysCount());
        guide.setMobilityOptions(resolveMobilityIds(request.getMobilityOptionIds()));
        guide.setSeasons(resolveSeasonIds(request.getSeasonIds()));
        guide.setAudiences(resolveAudienceIds(request.getAudienceIds()));

        return GuideMapper.toDto(guideRepository.save(guide));
    }

    @Transactional
    public void delete(UUID guideId) {
        if (!guideRepository.existsById(guideId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found");
        }
        guideRepository.deleteById(guideId);
    }

    private Set<Mobility> resolveMobilityIds(Set<UUID> ids) {
        if (ids == null)
            return Set.of();
        return ids.stream()
                .map(mobilityService::getById)
                .collect(Collectors.toSet());
    }

    private Set<Season> resolveSeasonIds(Set<UUID> ids) {
        if (ids == null)
            return Set.of();
        return ids.stream()
                .map(seasonService::getById)
                .collect(Collectors.toSet());
    }

    private Set<Audience> resolveAudienceIds(Set<UUID> ids) {
        if (ids == null)
            return Set.of();
        return ids.stream()
                .map(audienceService::getById)
                .collect(Collectors.toSet());
    }
}
