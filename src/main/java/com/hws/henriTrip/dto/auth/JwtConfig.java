package com.hws.henritrip.dto.auth;

import lombok.Getter;

/**
 * Simple DTO used only for tests or documentation; the runtime JWT configuration is provided via security.JwtProperties.
 */
@Getter
public class JwtConfig {
    private String secret;
    private long expiration;
}
