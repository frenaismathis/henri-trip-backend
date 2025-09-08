package com.hws.henritrip.mapper;

import com.hws.henritrip.dto.UserDTO;
import com.hws.henritrip.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toDto(User user) {
        if (user == null)
            return null;
        return UserDTO.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    public static void updateEntity(User user, UserDTO dto) {
        if (dto.getFirstname() != null)
            user.setFirstname(dto.getFirstname());
        if (dto.getLastname() != null)
            user.setLastname(dto.getLastname());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());
    }
}