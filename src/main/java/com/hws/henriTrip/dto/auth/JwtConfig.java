package com.hws.henritrip.dto.auth;

import lombok.Getter;

@Getter
public class JwtConfig {
    private String secret;
    private long expiration;
}
