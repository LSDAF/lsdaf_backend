package com.lsadf.lsadf_backend.cache;

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
     * Get the gold from cache if any
     *
     * @param gameSaveId the id of the game save
     * @return the gold amount if present in cache
     */
    Optional<Long> getGold(String gameSaveId);

    /**
     * Set the gold in cache
     * @param gameSaveId the id of the game save
     * @param gold the gold amount
     */
    void setGold(String gameSaveId, Long gold);

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
     * Get all the gold from the cache
     * @return a map of game save id to gold amount
     */
    Map<String, Long> getAllGold();

    /**
     * Get all the gold history from the cache
     * @return a map of game save id to gold amount
     */
    Map<String, Long> getAllGoldHisto();

    /**
     * Get all the game save ownership in cache
     */
    Map<String, String> getAllGameSaveOwnership();

    /**
     * Clear all the caches
     */
    void clearCaches();
}
