package com.lsadf.lsadf_backend.configurations.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.listeners.LocalRemovalListener;
import com.lsadf.lsadf_backend.properties.LocalCacheProperties;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.impl.LocalCacheServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.LocalCache;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.LOCAL_INVALIDATED_JWT_TOKEN_CACHE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.LOCAL_CACHE_SERVICE;

@Configuration
public class LocalCacheConfiguration {

    @Bean
    public com.github.benmanes.caffeine.cache.Cache<String, LocalUser> localUserCaffeineCache(RemovalListener<String, LocalUser> removalListener,
                                                                                              LocalCacheProperties localCacheProperties) {
        return Caffeine.newBuilder()
                .maximumSize(localCacheProperties.getLocalUserCacheMaxSize())
                .removalListener(removalListener)
                .build();
    }

    @Bean
    public com.github.benmanes.caffeine.cache.Cache<String, String> localJwtTokenCaffeineCache(RemovalListener<String, String> removalListener,
                                                                                               LocalCacheProperties localCacheProperties) {
        return Caffeine.newBuilder()
                .maximumSize(localCacheProperties.getInvalidatedRefreshTokenCacheMaxSize())
                .removalListener(removalListener)
                .build();
    }

    @Bean
    public Cache<LocalUser> localUserCache(CacheExpirationProperties cacheExpirationProperties,
                                           com.github.benmanes.caffeine.cache.Cache<String, LocalUser> localUserCaffeineCache) {
        return new LocalCache<>(localUserCaffeineCache, cacheExpirationProperties.getLocalUserExpirationSeconds());
    }

    @Bean
    @Qualifier(LOCAL_INVALIDATED_JWT_TOKEN_CACHE)
    public Cache<String> localJwtTokenCache(CacheExpirationProperties cacheExpirationProperties,
                                            com.github.benmanes.caffeine.cache.Cache<String, String> localJwtTokenCaffeineCache) {
        return new LocalCache<>(localJwtTokenCaffeineCache, cacheExpirationProperties.getInvalidatedJwtTokenExpirationSeconds());
    }


    @Bean
    public RemovalListener<String, LocalUser> localUserRemovalListener() {
        return new LocalRemovalListener<>();
    }

    @Bean
    public RemovalListener<String, String> jwtTokenRemovalListener() {
        return new LocalRemovalListener<>();
    }

    @Bean
    @Qualifier(LOCAL_CACHE_SERVICE)
    public CacheService localCacheService(Cache<LocalUser> localUserCache,
                                          Cache<String> localJwtTokenCache) {
        return new LocalCacheServiceImpl(localUserCache, localJwtTokenCache);
    }
}
