package com.lsadf.lsadf_backend.cache;

import java.util.Optional;

public interface CacheService {

    /**
     * Check if the cache is enabled
     * @return true if cache is enabled, false otherwise
     */
    boolean isCacheEnabled();

    /**
     * Get the gold from cache if any
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
}
