package com.hws.henritrip.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * DTO to manage access permissions for a guide.
 * - userIdsAllowed: explicit user ids allowed
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessDTO {
    @NotNull(message = "Guide id is required")
    private UUID guideId;

    private Set<@NotNull(message = "User id cannot be null") UUID> userIdsAllowed;

}
