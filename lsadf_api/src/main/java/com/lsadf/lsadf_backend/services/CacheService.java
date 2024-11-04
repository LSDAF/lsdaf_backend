package com.lsadf.lsadf_backend.services;

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
     * Clear all the caches
     */
    void clearCaches();
}
