package com.hws.henritrip.service;

import com.hws.henritrip.dto.AccessDTO;
import com.hws.henritrip.entity.Guide;
import com.hws.henritrip.entity.User;
import com.hws.henritrip.repository.GuideRepository;
import com.hws.henritrip.repository.UserRepository;
import com.hws.henritrip.security.RoleName;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final GuideRepository guideRepository;
    private final UserRepository userRepository;

    /**
     * Vérifie si un utilisateur a accès à un guide.
     * Admins ont accès à tout.
     */
    public void checkUserAccess(UUID guideId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));

        if (user.getRole() != null && RoleName.ADMIN.name().equalsIgnoreCase(user.getRole().getName())) {
            return;
        }

        if (guide.getUsers() == null || guide.getUsers().stream().noneMatch(u -> u.getId().equals(userId))) {
            throw new AccessDeniedException("User does not have access to this guide");
        }
    }

    /**
     * Vérifie que l'utilisateur est admin.
     */
    public void checkUserIsAdmin(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean isAdmin = user.getRole() != null &&
                RoleName.ADMIN.name().equalsIgnoreCase(user.getRole().getName());

        if (!isAdmin) {
            throw new AccessDeniedException("Only admin users can perform this action");
        }
    }

    /**
     * Retourne les userIds invités sur un guide
     */
    public Set<UUID> getUserIdsWithAccess(UUID guideId) {
        return guideRepository.findById(guideId)
                .map(Guide::getUsers)
                .map(set -> set.stream().map(User::getId).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    /**
     * Invite des utilisateurs sur un guide
     */
    @Transactional
    public void applyAccess(AccessDTO dto) {
        if (dto.getUserIdsAllowed() != null) {
            dto.getUserIdsAllowed().forEach(userId -> grantUserAccess(dto.getGuideId(), userId));
        }
    }

    @Transactional
    public void grantUserAccess(UUID guideId, UUID userId) {
        Guide guide = guideRepository.findById(guideId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guide not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (guide.getUsers() == null) {
            guide.setUsers(Collections.singleton(user));
        } else {
            guide.getUsers().add(user);
        }
        guideRepository.save(guide);
    }

    @Transactional
    public void revokeUserAccess(UUID guideId, UUID userId) {
        guideRepository.findById(guideId).ifPresent(guide -> {
            if (guide.getUsers() != null) {
                guide.getUsers().removeIf(u -> u.getId().equals(userId));
                guideRepository.save(guide);
            }
        });
    }
}
