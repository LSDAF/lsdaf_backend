package com.lsadf.lsadf_backend.security.jwt;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    protected final TokenProvider<JwtTokenEntity> tokenProvider;
    protected final UserDetailsService userDetailsService;
    protected final Cache<LocalUser> localUserCache;

    public TokenAuthenticationFilter(TokenProvider<JwtTokenEntity> tokenProvider,
                                     UserDetailsService userDetailsService,
                                     Cache<LocalUser> localUserCache) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.localUserCache = localUserCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, null)) {
                String userId = tokenProvider.getUserEmailFromToken(jwt);
                LocalUser localUser = localUserCache.get(userId).orElseGet(() -> {
                    UserDetails user = userDetailsService.loadUserByUsername(userId);
                    LocalUser dbLocalUser = (LocalUser) user;
                    localUserCache.set(userId, dbLocalUser);
                    return dbLocalUser;
                });
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(localUser, null, localUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    protected String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return TokenUtils.extractTokenFromHeader(bearerToken);
        }
        return null;
    }
}
