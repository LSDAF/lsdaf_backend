package com.lsadf.core.cache.impl;

import com.lsadf.core.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class NoOpCache<T> implements Cache<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<T> get(String key) {
        return Optional.empty();
    }

    @Override
    public void set(String key, T value, int expirationSeconds) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String key, T value) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, T> getAll() {
        return Map.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unset(String key) {
        // Do nothing
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
    public boolean isEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean enabled) {
        // Do nothing
    }

    @Override
    public void setExpiration(int expirationSeconds) {
        // Do nothing
    }
}
