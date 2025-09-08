package com.hws.henritrip.controller;

import com.hws.henritrip.dto.AccessDTO;
import com.hws.henritrip.service.AccessService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Set<UUID>> getUsersWithAccess(@PathVariable UUID guideId) {
        return ResponseEntity.ok(accessService.getUserIdsWithAccess(guideId));
    }

    @PostMapping
    public ResponseEntity<String> addAccess(@PathVariable UUID guideId, @RequestBody AccessDTO dto) {
        dto.setGuideId(guideId);
        accessService.applyAccess(dto);
        return ResponseEntity.ok("Access granted successfully");
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> revokeAccess(@PathVariable UUID guideId, @PathVariable UUID userId) {
        boolean removed = accessService.revokeUserAccess(guideId, userId);
        if (removed) {
            return ResponseEntity.ok("Access revoked successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User did not have access to this guide");
        }
    }
}
