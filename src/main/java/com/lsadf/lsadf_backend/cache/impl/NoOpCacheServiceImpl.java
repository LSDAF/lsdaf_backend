package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.models.LocalUser;

import java.util.Map;
import java.util.Optional;

public class NoOpCacheServiceImpl implements CacheService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isEnabled() {
        return false;
    }

    @Override
    public void toggleCacheEnabling() {
        // Do nothing
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getGameSaveOwnership(String gameSaveId) {
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameSaveOwnership(String gameSaveId, String userId) {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getAllGold() {
        return Map.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getAllGameSaveOwnership() {
        return Map.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getAllGoldHisto() {
        return Map.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCaches() {
        // Do nothing
    }
}
