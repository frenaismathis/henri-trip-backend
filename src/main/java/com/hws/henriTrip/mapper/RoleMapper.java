package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.RoleDTO;
import com.hws.henritrip.entity.Role;

public class RoleMapper {

    public static RoleDTO toDto(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public static Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        return role;
    }
}