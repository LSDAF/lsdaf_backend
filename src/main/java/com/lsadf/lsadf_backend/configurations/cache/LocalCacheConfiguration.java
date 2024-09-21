package com.lsadf.lsadf_backend.configurations.cache;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.listeners.LocalRemovalListener;
import com.lsadf.lsadf_backend.properties.LocalCacheProperties;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.impl.LocalCacheServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.LocalCache;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.LOCAL_INVALIDATED_JWT_TOKEN_CACHE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.LOCAL_CACHE_SERVICE;

@Configuration
public class LocalCacheConfiguration {

    @Bean
    ExpiringMap<String, String> localJwtTokenExpiringMap(LocalCacheProperties localCacheProperties,
                                                         ExpirationListener<String, String> jwtTokenRemovalListener) {
        return ExpiringMap.builder()
                .variableExpiration()
                .maxSize(localCacheProperties.getInvalidatedRefreshTokenCacheMaxSize())
                .expirationListener(jwtTokenRemovalListener)
                .build();
    }

    @Bean
    ExpiringMap<String, LocalUser> localUserExpiringMap(LocalCacheProperties localCacheProperties,
                                                        ExpirationListener<String, LocalUser> localUserRemovalListener) {
        return ExpiringMap.builder()
                .variableExpiration()
                .maxSize(localCacheProperties.getLocalUserCacheMaxSize())
                .expirationListener(localUserRemovalListener)
                .build();
    }

    @Bean
    public Cache<LocalUser> localUserCache(CacheExpirationProperties cacheExpirationProperties,
                                           ExpiringMap<String, LocalUser> localUserExpiringMap) {
        return new LocalCache<>(localUserExpiringMap, cacheExpirationProperties.getLocalUserExpirationSeconds());
    }

    @Bean
    @Qualifier(LOCAL_INVALIDATED_JWT_TOKEN_CACHE)
    public Cache<String> localJwtTokenCache(CacheExpirationProperties cacheExpirationProperties,
                                            ExpiringMap<String, String> localJwtTokenExpiringMap) {
        return new LocalCache<>(localJwtTokenExpiringMap, cacheExpirationProperties.getInvalidatedJwtTokenExpirationSeconds());
    }


    @Bean
    public ExpirationListener<String, LocalUser> localUserRemovalListener() {
        return new LocalRemovalListener<>();
    }

    @Bean
    public ExpirationListener<String, String> jwtTokenRemovalListener() {
        return new LocalRemovalListener<>();
    }

    @Bean
    @Qualifier(LOCAL_CACHE_SERVICE)
    public CacheService localCacheService(Cache<LocalUser> localUserCache,
                                          Cache<String> localJwtTokenCache) {
        return new LocalCacheServiceImpl(localUserCache, localJwtTokenCache);
    }
}
