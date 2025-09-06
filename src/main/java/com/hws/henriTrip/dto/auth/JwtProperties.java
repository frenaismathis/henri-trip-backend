package com.hws.henritrip.dto.auth;

import lombok.Getter;

@Getter
public class JwtProperties {
    private String secret;
    private long expiration;
}