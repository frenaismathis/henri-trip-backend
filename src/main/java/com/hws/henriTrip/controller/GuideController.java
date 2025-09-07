package com.hws.henritrip.controller;

import com.hws.henritrip.dto.GuideCreateRequest;
import com.hws.henritrip.dto.GuideDTO;
import com.hws.henritrip.service.GuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class GuideController {

    private final GuideService guideService;

    @GetMapping("/api/guides")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<GuideDTO>> getAllGuides() {
        return ResponseEntity.ok(guideService.findAll());
    }

    @GetMapping("/api/guides/visible/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<GuideDTO>> getGuidesVisibleToUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(guideService.findAllVisibleToUser(userId));
    }

    @GetMapping("/api/guides/{guideId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<GuideDTO> getGuideById(@PathVariable UUID guideId) {
        return ResponseEntity.ok(guideService.findById(guideId));
    }

    @GetMapping("/api/guides/{guideId}/forUser/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<GuideDTO> getGuideForUser(@PathVariable UUID guideId,
            @PathVariable UUID userId) {
        return ResponseEntity.ok(guideService.findByIdForUser(guideId, userId));
    }

    @PostMapping("/api/admin/guides")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GuideDTO> createGuide(@Valid @RequestBody GuideCreateRequest request,
            @RequestParam UUID creatorUserId) {
        GuideDTO dto = guideService.createFromRequest(request, creatorUserId);
        return ResponseEntity.created(
                URI.create("/api/admin/guides/" + dto.getId()))
                .body(dto);
    }

    @PutMapping("/api/admin/guides/{guideId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GuideDTO> updateGuide(@PathVariable UUID guideId,
            @Valid @RequestBody GuideCreateRequest request) {
        GuideDTO dto = guideService.updateFromRequest(guideId, request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/api/admin/guides/{guideId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGuide(@PathVariable UUID guideId) {
        guideService.delete(guideId);
        return ResponseEntity.noContent().build();
    }
}
