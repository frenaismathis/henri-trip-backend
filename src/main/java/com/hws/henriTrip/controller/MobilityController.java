package com.hws.henritrip.controller;

import com.hws.henritrip.dto.MobilityDTO;
import com.hws.henritrip.service.MobilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/mobilities")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class MobilityController {

    private final MobilityService mobilityService;

    @GetMapping
    public ResponseEntity<List<MobilityDTO>> getAllMobilities() {
        return ResponseEntity.ok(mobilityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MobilityDTO> getMobility(@PathVariable UUID id) {
        return ResponseEntity.ok(mobilityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MobilityDTO> createMobility(@RequestBody MobilityDTO dto) {
        return ResponseEntity.ok(mobilityService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MobilityDTO> updateMobility(@PathVariable UUID id, @RequestBody MobilityDTO dto) {
        return ResponseEntity.ok(mobilityService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMobility(@PathVariable UUID id) {
        mobilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
