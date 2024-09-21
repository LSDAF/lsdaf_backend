package com.lsadf.lsadf_backend.services;

public interface CacheFlushService {
    /**
     * Flush the currency cache, and persists the currency of every entries in the database
     */
    void flushCurrencies();
}
