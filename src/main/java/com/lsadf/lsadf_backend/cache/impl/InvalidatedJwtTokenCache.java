package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Optional;

import static com.lsadf.lsadf_backend.constants.RedisConstants.INVALIDATED_JWT_TOKEN;

public class InvalidatedJwtTokenCache extends RedisCache<String> implements Cache<String> {

    private final Cache<String> localInvalidatedJwtTokenCache;

    public InvalidatedJwtTokenCache(RedisTemplate<String, String> redisTemplate,
                                    Cache<String> localInvalidatedJwtTokenCache,
                                    int expirationSeconds,
                                    RedisProperties redisProperties) {
        super(redisTemplate, INVALIDATED_JWT_TOKEN, expirationSeconds, redisProperties);
        this.localInvalidatedJwtTokenCache = localInvalidatedJwtTokenCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> get(String key) {
        if (isEnabled()) {
            return super.get(key);
        }
        return localInvalidatedJwtTokenCache.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String key, String value) {
        if (isEnabled()) {
            super.set(key, value);
            return;
        }
        localInvalidatedJwtTokenCache.set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getAll() {
        if (isEnabled()) {
            return super.getAll();
        }
        return localInvalidatedJwtTokenCache.getAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        if (isEnabled()) {
            super.clear();
            return;
        }
        localInvalidatedJwtTokenCache.clear();
    }

    @Override
    public void setExpiration(int expirationSeconds) {
        localInvalidatedJwtTokenCache.setExpiration(expirationSeconds);
    }
}
