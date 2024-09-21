package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import lombok.RequiredArgsConstructor;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomAuthenticationProviderImpl implements org.springframework.security.authentication.AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final ExpiringMap<String, LocalUser> localUserCache;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        final LocalUser user = localUserCache.getOrDefault(email, (LocalUser) userDetailsService.loadUserByUsername(email));
        return createSuccessfulAuthentication(authentication, user);
    }

    private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
