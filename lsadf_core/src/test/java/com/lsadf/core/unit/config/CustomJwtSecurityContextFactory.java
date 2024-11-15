package com.lsadf.core.unit.config;

import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class CustomJwtSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser mockUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Create a mock Jwt object with the desired claims
        Jwt mockJwt = Mockito.mock(Jwt.class);
        String username = mockUser.username();
        String name = mockUser.name();
        boolean emailVerified = mockUser.emailVerified();
        boolean enabled = mockUser.enabled();


        Mockito.when(mockJwt.getClaimAsString("preferred_username")).thenReturn(username);
        Mockito.when(mockJwt.getClaimAsString("name")).thenReturn(name);
        Mockito.when(mockJwt.getClaimAsBoolean("email_verified")).thenReturn(emailVerified);
        Mockito.when(mockJwt.getClaimAsBoolean("enabled")).thenReturn(enabled);
        // Create the authentication token
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(mockJwt,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + mockUser.roles()[0])));

        context.setAuthentication(authentication);
        return context;
    }
}
