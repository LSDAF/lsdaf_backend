package com.lsadf.lsadf_backend.cache.listeners;

import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationListener;

@Slf4j
public class LocalRemovalListener<T> implements ExpirationListener<String, T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void expired(String s, T t) {
        log.info("Local cache entry expired -> {}", s);
    }
}
