package com.lsadf.lsadf_backend.cache.impl;

import com.github.benmanes.caffeine.cache.Policy;
import com.lsadf.lsadf_backend.cache.Cache;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class LocalCache<T> implements Cache<T> {

    private final com.github.benmanes.caffeine.cache.Cache<String, T> cache;

    private int expirationSeconds;

    private final boolean isEnabled = true;

    public LocalCache(com.github.benmanes.caffeine.cache.Cache<String, T> cache,
                      int expirationSeconds) {
        this.cache = cache;
        this.expirationSeconds = expirationSeconds;
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
        T value = cache.getIfPresent(key);
        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String key, T value) {
        var optionalVarExpiration = cache.policy().expireVariably();
        if (optionalVarExpiration.isPresent()) {
            Policy.VarExpiration<String, T> varExpiration = optionalVarExpiration.get();
            var result = varExpiration.put(key, value, expirationSeconds, TimeUnit.SECONDS);
            if (result == null) {
                log.warn("Error while setting entry in local cache");
            }
        } else {
            log.warn("Variable expiration not allowed for this LocalCache. Adding entry with no expiration.");
            cache.put(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, T> getAll() {
        return cache.asMap();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        cache.invalidateAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean enabled) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public void setExpiration(int expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
