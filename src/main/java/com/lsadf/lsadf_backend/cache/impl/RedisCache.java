package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import com.lsadf.lsadf_backend.utils.CacheUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class RedisCache<T> implements Cache<T> {

    protected final RedisTemplate<String, T> redisTemplate;
    protected final String keyType;

    protected int expirationSeconds;

    protected final AtomicBoolean isEnabled;

    public RedisCache(RedisTemplate<String, T> redisTemplate,
                      String keyType,
                      int expirationSeconds,
                      RedisProperties redisProperties) {
        this.redisTemplate = redisTemplate;
        this.keyType = keyType;
        this.expirationSeconds = expirationSeconds;
        this.isEnabled = new AtomicBoolean(redisProperties.isEnabled());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return log;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> get(String key) {
        try {
            T object = redisTemplate.opsForValue().get(keyType + key);
            return Optional.ofNullable(object);
        } catch (DataAccessException e) {
            log.warn("Error while getting element from redis cache", e);
            return Optional.empty();
        }
    }

    @Override
    public void set(String key, T value, int expirationSeconds) {
        try {
            redisTemplate.opsForValue().set(keyType + key, value, expirationSeconds, TimeUnit.SECONDS);
        } catch (DataAccessException e) {
            log.warn("Error while setting entry in redis cache", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String key, T value) {
        try {
            if (expirationSeconds > 0) {
                redisTemplate.opsForValue().set(keyType + key, value, expirationSeconds, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(keyType + key, value);
            }
        } catch (DataAccessException e) {
            log.warn("Error while setting entry in redis cache", e);
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, T> getAll() {
        return CacheUtils.getAllEntries(redisTemplate, keyType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        CacheUtils.clearCache(redisTemplate, keyType);
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled.set(enabled);
        String type = keyType.substring(0, keyType.length() - 1);
        log.error("Redis {} cache is now " + (enabled ? "enabled" : "disabled"), type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unset(String key) {
        try {
            redisTemplate.delete(keyType + key);
        } catch (DataAccessException e) {
            log.warn("Error while deleting entry from redis cache", e);
        }
    }

    @Override
    public void setExpiration(int expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
