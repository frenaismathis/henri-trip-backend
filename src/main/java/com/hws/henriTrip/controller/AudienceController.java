package com.hws.henritrip.controller;

import com.hws.henritrip.dto.AudienceDTO;
import com.hws.henritrip.service.AudienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/audiences")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AudienceController {

    private final AudienceService audienceService;

    @GetMapping
    public ResponseEntity<List<AudienceDTO>> getAllAudiences() {
        return ResponseEntity.ok(audienceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudienceDTO> getAudience(@PathVariable UUID id) {
        return ResponseEntity.ok(audienceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AudienceDTO> createAudience(@RequestBody AudienceDTO dto) {
        return ResponseEntity.ok(audienceService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AudienceDTO> updateAudience(@PathVariable UUID id, @RequestBody AudienceDTO dto) {
        return ResponseEntity.ok(audienceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAudience(@PathVariable UUID id) {
        audienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
