package com.lsadf.lsadf_backend.cache.listeners;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

@Slf4j
public class LocalRemovalListener<T> implements RemovalListener<String, T> {

    @Override
    public void onRemoval(@Nullable String s, @Nullable T t, RemovalCause removalCause) {
        log.info("Local cache entry expired -> {}", s);
    }
}
