package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.models.Currency;
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
    public Map<String, String> getAllGameSaveOwnership() {
        return Map.of();
    }

    @Override
    public Optional<Currency> getCurrency(String gameSaveId) {
        return Optional.empty();
    }

    @Override
    public void setCurrency(String gameSaveId, Currency currency) {
        // Do nothing
    }

    @Override
    public Map<String, Currency> getAllCurrencies() {
        return Map.of();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCaches() {
        // Do nothing
    }

    @Override
    public Map<String, Currency> getAllCurrenciesHisto() {
        return Map.of();
    }
}
