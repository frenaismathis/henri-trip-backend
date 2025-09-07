package com.hws.henritrip.controller;

import com.hws.henritrip.dto.RoleDTO;
import com.hws.henritrip.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> listRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleService.findById(roleId));
    }
}
