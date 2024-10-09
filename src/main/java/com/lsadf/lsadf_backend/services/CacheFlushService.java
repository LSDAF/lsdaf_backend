package com.lsadf.lsadf_backend.services;

public interface CacheFlushService {
    /**
     * Flush the currency cache, and persists the currency of every entries in the database
     */
    void flushCurrencies();

    /**
     * Flush the stage cache, and persists the stage of every entries in the database
     */
    void flushStages();
    
}
