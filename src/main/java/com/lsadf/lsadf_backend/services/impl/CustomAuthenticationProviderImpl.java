package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.models.LocalUser;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class CustomAuthenticationProviderImpl implements org.springframework.security.authentication.AuthenticationProvider {
    private final UserDetailsService lsadfUserDetailsService;
    private final ExpiringMap<String, LocalUser> localUserCache;

    public CustomAuthenticationProviderImpl(UserDetailsService lsadfUserDetailsService,
                                            ExpiringMap<String, LocalUser> localUserCache) {
        this.lsadfUserDetailsService = lsadfUserDetailsService;
        this.localUserCache = localUserCache;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
         final String email = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        final LocalUser user = localUserCache.getOrDefault(email, (LocalUser) lsadfUserDetailsService.loadUserByUsername(email));
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
