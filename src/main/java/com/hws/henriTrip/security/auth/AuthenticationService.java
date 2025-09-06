package com.hws.henritrip.security.auth;

import com.hws.henritrip.dto.auth.AuthenticationRequest;
import com.hws.henritrip.dto.auth.AuthenticationResponse;
import com.hws.henritrip.security.UserPrincipal;
import com.hws.henritrip.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String role = userPrincipal.getAuthorities().iterator().next().getAuthority();

        String token = jwtTokenProvider.createToken(userPrincipal.getEmail(), role);

        return AuthenticationResponse.builder()
            .token(token)
            .email(request.getEmail())
            .role(role)
            .build();
    }
}
