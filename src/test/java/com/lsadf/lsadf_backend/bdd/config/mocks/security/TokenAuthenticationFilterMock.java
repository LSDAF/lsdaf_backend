package com.lsadf.lsadf_backend.bdd.config.mocks.security;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.security.jwt.TokenAuthenticationFilter;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Slf4j
public class TokenAuthenticationFilterMock extends TokenAuthenticationFilter {

    private final Map<String, Pair<Date, LocalUser>> localUserMap;

    public TokenAuthenticationFilterMock(TokenProvider tokenProvider,
                                         UserDetailsService userDetailsService,
                                         Map<String, Pair<Date, LocalUser>> localUserMap,
                                         Cache<LocalUser> localUserCache) {
        super(tokenProvider, userDetailsService, localUserCache);
        this.localUserMap = localUserMap;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && localUserMap.containsKey(jwt)) {
                LocalUser localUser = localUserMap.get(jwt).getRight();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(localUser, null, localUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }
}
