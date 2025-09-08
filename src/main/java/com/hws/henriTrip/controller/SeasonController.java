package com.hws.henritrip.controller;

import com.hws.henritrip.dto.SeasonDTO;
import com.hws.henritrip.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/seasons")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SeasonController {

    private final SeasonService seasonService;

    @GetMapping
    public ResponseEntity<List<SeasonDTO>> getAllSeasons() {
        return ResponseEntity.ok(seasonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeasonDTO> getSeason(@PathVariable UUID id) {
        return ResponseEntity.ok(seasonService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SeasonDTO> createSeason(@RequestBody SeasonDTO dto) {
        return ResponseEntity.ok(seasonService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeasonDTO> updateSeason(@PathVariable UUID id, @RequestBody SeasonDTO dto) {
        return ResponseEntity.ok(seasonService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeason(@PathVariable UUID id) {
        seasonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
