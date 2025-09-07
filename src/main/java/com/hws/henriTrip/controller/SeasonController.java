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
}
