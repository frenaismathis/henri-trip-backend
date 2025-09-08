package com.hws.henritrip.service;

import com.hws.henritrip.dto.UserCreateRequest;
import com.hws.henritrip.dto.UserDTO;
import com.hws.henritrip.dto.UserUpdateRequest;
import com.hws.henritrip.entity.Role;
import com.hws.henritrip.entity.User;
import com.hws.henritrip.mapper.UserMapper;
import com.hws.henritrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO findById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    @Transactional
    public UserDTO createFromRequest(UserCreateRequest request) {
        Role role = roleService.getById(request.getRoleId());

        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        return UserMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserDTO update(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Role role = roleService.getById(request.getRoleId());

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setRole(role);

        return UserMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
