package com.hws.henritrip.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotNull(message = "Id is required for this operation")
    private UUID id;

    @NotBlank(message = "First name is required")
    private String firstname;

    @NotBlank(message = "Last name is required")
    private String lastname;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

}
