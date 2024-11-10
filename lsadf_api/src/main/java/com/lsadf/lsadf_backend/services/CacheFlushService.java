package com.lsadf.lsadf_backend.services;

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
     * Flush the inventory cache, and persists the inventory of every entries in the database
     */
    void flushInventories();

    /**
     * Flush the stage cache, and persists the stage of every entries in the database
     */
    void flushStages();

}
