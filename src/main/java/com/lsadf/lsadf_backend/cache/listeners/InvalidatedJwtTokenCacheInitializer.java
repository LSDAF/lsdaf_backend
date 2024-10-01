package com.lsadf.lsadf_backend.cache.listeners;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.ClockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class InvalidatedJwtTokenCacheInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final AtomicBoolean isAlreadySetup = new AtomicBoolean(false);

    private final ClockService clockService;
    private final TokenProvider jwtTokenProvider;
    private final Cache<String> invalidatedJwtTokenCache;

    public InvalidatedJwtTokenCacheInitializer(ClockService clockService,
                                               TokenProvider jwtTokenProvider,
                                               Cache<String> invalidatedJwtTokenCache) {
        this.clockService = clockService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.invalidatedJwtTokenCache = invalidatedJwtTokenCache;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (isAlreadySetup.getAndSet(true)) {
            return;
        }

        log.info("Adding invalidated tokens to cache");
        int count = 0;
        List<JwtTokenEntity> invalidatedTokens = jwtTokenProvider.getInvalidatedTokens();
        for (JwtTokenEntity token : invalidatedTokens) {
            Date now = clockService.nowDate();
            if (token.getExpirationDate().before(now)) {
                continue;
            }
            int expirationSeconds = (int) ((token.getExpirationDate().getTime() - now.getTime()) / 1000);
            invalidatedJwtTokenCache.set(token.getToken(), token.getUser().getEmail(), expirationSeconds);
            count++;
        }

        log.info("Added {} invalidated tokens to cache", count);
    }
}
