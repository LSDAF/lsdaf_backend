package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.listeners.InvalidatedJwtTokenCacheInitializer;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.properties.DbInitProperties;
import com.lsadf.lsadf_backend.properties.ShutdownProperties;
import com.lsadf.lsadf_backend.services.ClockService;
import com.lsadf.lsadf_backend.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.INVALIDATED_JWT_TOKEN_CACHE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.LOCAL_INVALIDATED_JWT_TOKEN_CACHE;

/**
 * Configuration class for the application listeners.
 */
@Configuration
public class ApplicationListenerConfiguration {

    @Bean
    public DbInitializer dbInitializer(DbInitProperties dbInitProperties,
                                       UserService userService) {
        return new DbInitializer(userService, dbInitProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "true")
    public InvalidatedJwtTokenCacheInitializer redisInvalidatedJwtTokenCacheInitializer(ClockService clockService,
                                                                                        TokenProvider<JwtTokenEntity> tokenProvider,
                                                                                        @Qualifier(INVALIDATED_JWT_TOKEN_CACHE) Cache<String> invalidatedJwtTokenCache) {
        return new InvalidatedJwtTokenCacheInitializer(clockService, tokenProvider, invalidatedJwtTokenCache);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "false")
    public InvalidatedJwtTokenCacheInitializer localInvalidatedJwtTokenCacheInitializer(ClockService clockService,
                                                                                        TokenProvider<JwtTokenEntity> tokenProvider,
                                                                                        @Qualifier(LOCAL_INVALIDATED_JWT_TOKEN_CACHE) Cache<String> invalidatedJwtTokenCache) {
        return new InvalidatedJwtTokenCacheInitializer(clockService, tokenProvider, invalidatedJwtTokenCache);
    }

    @Bean
    public ShutdownListener shutdownListener(CacheFlushService cacheFlushService,
                                             ShutdownProperties shutdownProperties) {
        return new ShutdownListener(cacheFlushService, shutdownProperties);
    }
}
