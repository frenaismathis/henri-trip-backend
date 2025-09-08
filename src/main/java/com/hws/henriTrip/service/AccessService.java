package com.hws.henritrip.service;

import com.hws.henritrip.dto.AccessDTO;
import com.hws.henritrip.entity.Guide;
import com.hws.henritrip.entity.User;
import com.hws.henritrip.repository.GuideRepository;
import com.hws.henritrip.repository.UserRepository;
import com.hws.henritrip.security.RoleName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final GuideRepository guideRepository;
    private final UserRepository userRepository;

    public void checkUserAccess(UUID guideId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole() != null && RoleName.ADMIN.name().equalsIgnoreCase(user.getRole().getName())) {
            return;
        }

        boolean hasAccess = user.getGuides().stream()
                .anyMatch(g -> g.getId().equals(guideId));

        if (!hasAccess) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have access to this guide");
        }
    }

    public void checkUserIsAdmin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean isAdmin = user.getRole() != null &&
                RoleName.ADMIN.name().equalsIgnoreCase(user.getRole().getName());

        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admin users can perform this action");
        }
    }

    @Transactional
    public void applyAccess(AccessDTO dto) {
        if (dto.getUserIdsAllowed() == null || dto.getUserIdsAllowed().isEmpty())
            return;

        Guide guideRef = guideRepository.getReferenceById(dto.getGuideId());

        List<User> users = userRepository.findAllById(dto.getUserIdsAllowed());

        for (User user : users) {
            if (user.getGuides() == null)
                user.setGuides(new HashSet<>());

            boolean alreadyAdded = user.getGuides().stream()
                    .anyMatch(g -> g.getId().equals(guideRef.getId()));

            if (!alreadyAdded) {
                user.getGuides().add(guideRef);
            }
        }

        userRepository.saveAll(users);
    }

    @Transactional
    public boolean revokeUserAccess(UUID guideId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getGuides() == null)
            user.setGuides(new HashSet<>());

        boolean removed = user.getGuides().removeIf(g -> g.getId().equals(guideId));

        if (removed) {
            userRepository.save(user);
        }

        return removed;
    }

    public Set<UUID> getUserIdsWithAccess(UUID guideId) {
        List<User> users = userRepository.findAllByGuides_Id(guideId);
        return users.stream().map(User::getId).collect(Collectors.toSet());
    }
}
