package com.lsadf.core.services;

public interface CacheFlushService {
    /**
     * Flush the characteristics cache, and persists the characteristics of every entries in the database
     */
    void flushCharacteristics();

    /**
     * Flush the currency cache, and persists the currency of every entries in the database
     */
    void flushCurrencies();

    /**
     * Flush the stage cache, and persists the stage of every entries in the database
     */
    void flushStages();

}
