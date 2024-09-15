package com.lsadf.lsadf_backend.cache;

import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.LocalUser;

import java.util.Map;
import java.util.Optional;

public interface CacheService {

    /**
     * Check if the cache is enabled
     * @return true if cache is enabled, false otherwise
     */
    Boolean isEnabled();

    /**
     * Toggle the cache enabling
     */
    void toggleCacheEnabling();

    /**
     * Get the currency from cache if any
     * @param gameSaveId the id of the game save
     * @return the currency if present in cache
     */
    Optional<Currency> getCurrency(String gameSaveId);

    /**
     * Set the currency in cache
     * @param gameSaveId the id of the game save
     * @param currency the currency
     */
    void setCurrency(String gameSaveId, Currency currency);

    /**
     * Get the user email of the creator of a game save in cache if any
     * @param gameSaveId the id of the game save
     */
    Optional<String> getGameSaveOwnership(String gameSaveId);

    /**
     * Set the user email of the creator of a game save in cache
     * @param gameSaveId the id of the game save
     * @param userId the id of the user
     */
    void setGameSaveOwnership(String gameSaveId, String userId);

    /**
     * Get all the game save ownership in cache
     */
    Map<String, String> getAllGameSaveOwnership();

    /**
     * Get all the currency entries in cache
     * @return a map of game save id to currency
     */
    Map<String, Currency> getAllCurrencies();

    /**
     * Get the gold for a game save in cache if any
     * @return the gold if present in cache
     */
    Map<String, Currency> getAllCurrenciesHisto();

    /**
     * Clear all the caches
     */
    void clearCaches();
}
