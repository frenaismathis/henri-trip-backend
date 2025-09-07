package com.hws.henritrip.controller;

import com.hws.henritrip.dto.AccessDTO;
import com.hws.henritrip.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/guides/{guideId}/access")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AccessController {

    private final AccessService accessService;

    @GetMapping
    public ResponseEntity<Set<UUID>> listUsersWithAccess(@PathVariable UUID guideId) {
        return ResponseEntity.ok(accessService.getUserIdsWithAccess(guideId));
    }

    @PostMapping
    public ResponseEntity<Void> grantAccess(
            @PathVariable UUID guideId,
            @RequestBody AccessDTO dto) {

        dto.setGuideId(guideId);
        accessService.applyAccess(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> revokeAccess(
            @PathVariable UUID guideId,
            @PathVariable UUID userId) {

        accessService.revokeUserAccess(guideId, userId);
        return ResponseEntity.noContent().build();
    }
}
