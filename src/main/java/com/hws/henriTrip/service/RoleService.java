package com.hws.henritrip.service;

import com.hws.henritrip.dto.RoleDTO;
import com.hws.henritrip.entity.Role;
import com.hws.henritrip.mapper.RoleMapper;
import com.hws.henritrip.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    public RoleDTO findById(UUID roleId) {
        return roleRepository.findById(roleId)
                .map(RoleMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleId));
    }

    public Role getById(UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleId));
    }

    public RoleDTO create(RoleDTO dto) {
        Role role = RoleMapper.toEntity(dto);
        return RoleMapper.toDto(roleRepository.save(role));
    }

    public RoleDTO update(UUID roleId, RoleDTO dto) {
        Role role = getById(roleId);
        role.setName(dto.getName());
        return RoleMapper.toDto(roleRepository.save(role));
    }

    public void delete(UUID roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleId);
        }
        roleRepository.deleteById(roleId);
    }
}
