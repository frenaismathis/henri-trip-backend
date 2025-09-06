package com.hws.henritrip.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {
    
    @Value("${JWT_SECRET}")
    private String secret;
    
    @Value("${JWT_EXPIRATION}")
    private long expiration;

    public String getSecret() {
        return secret;
    }

    public long getExpiration() {
        return expiration;
    }
}
