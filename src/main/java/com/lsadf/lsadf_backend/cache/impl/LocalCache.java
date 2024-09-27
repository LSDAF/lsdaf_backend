package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LocalCache<T> implements Cache<T> {

    private final ExpiringMap<String, T> cache;

    private Integer expirationSeconds;

    private final boolean isEnabled = true;

    public LocalCache(ExpiringMap<String, T> cache,
                     @Nullable Integer expirationSeconds) {
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
        T value = cache.getOrDefault(key, null);
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
        if (expirationSeconds > 0) {
            cache.put(key, value, expirationSeconds, TimeUnit.SECONDS);
        } else {
            cache.put(key, value);
        }
    }

    @Override
    public void set(String key, T value, int expirationSeconds) {
        cache.put(key, value, expirationSeconds, TimeUnit.SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, T> getAll() {
        return cache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        cache.clear();
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
