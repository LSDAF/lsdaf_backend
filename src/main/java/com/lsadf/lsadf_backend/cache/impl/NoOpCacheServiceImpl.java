package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheService;

import java.util.Optional;

public class NoOpCacheServiceImpl implements CacheService {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCacheEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Long> getGold(String gameSaveId) {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGold(String gameSaveId, Long gold) {
        // Do nothing
    }

    @Override
    public Optional<String> getGameSaveOwnership(String gameSaveId) {
        return Optional.empty();
    }

    @Override
    public void setGameSaveOwnership(String gameSaveId, String userId) {
        // Do nothing
    }
}
