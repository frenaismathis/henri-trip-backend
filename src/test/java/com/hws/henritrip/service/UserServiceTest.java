package com.hws.henritrip.service;

import com.hws.henritrip.dto.UserCreateRequest;
import com.hws.henritrip.dto.UserDTO;
import com.hws.henritrip.entity.Role;
import com.hws.henritrip.entity.User;
import com.hws.henritrip.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Role adminRole() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("ADMIN");
        return role;
    }

    private User userWithRole(UUID id, Role role) {
        User user = new User();
        user.setId(id);
        user.setFirstname("Jane");
        user.setLastname("Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("hashed-password");
        user.setRole(role);
        return user;
    }

    @Test
    void findAll_returnsAllUsersMappedToDto() {
        Role role = adminRole();
        User user1 = userWithRole(UUID.randomUUID(), role);
        User user2 = userWithRole(UUID.randomUUID(), role);
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDTO> result = userService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserDTO::getId)
                .containsExactlyInAnyOrder(user1.getId(), user2.getId());
        assertThat(result).extracting(UserDTO::getRole).containsOnly("ADMIN");
    }

    @Test
    void findById_withExistingId_returnsMappedDto() {
        UUID id = UUID.randomUUID();
        User user = userWithRole(id, adminRole());
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO result = userService.findById(id);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(result.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void findById_withUnknownId_throwsNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(404));
    }

    @Test
    void createFromRequest_withValidData_persistsAndReturnsDto() {
        Role role = adminRole();
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstname("Jane");
        request.setLastname("Doe");
        request.setEmail("jane.doe@example.com");
        request.setPassword("plain-password");
        request.setRoleId(role.getId());

        when(roleService.getById(role.getId())).thenReturn(role);
        when(passwordEncoder.encode("plain-password")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        UserDTO result = userService.createFromRequest(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User persisted = userCaptor.getValue();
        assertThat(persisted.getPassword()).isEqualTo("encoded-password");
        assertThat(persisted.getRole()).isEqualTo(role);

        assertThat(result.getId()).isEqualTo(persisted.getId());
        assertThat(result.getFirstname()).isEqualTo("Jane");
        assertThat(result.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void createFromRequest_withUnknownRoleId_throwsNotFoundAndDoesNotPersist() {
        UUID unknownRoleId = UUID.randomUUID();
        UserCreateRequest request = new UserCreateRequest();
        request.setFirstname("Jane");
        request.setLastname("Doe");
        request.setEmail("jane.doe@example.com");
        request.setPassword("plain-password");
        request.setRoleId(unknownRoleId);

        when(roleService.getById(unknownRoleId))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,
                        "Role not found: " + unknownRoleId));

        assertThatThrownBy(() -> userService.createFromRequest(request))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(404));

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
