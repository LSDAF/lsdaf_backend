package com.lsadf.lsadf_backend.cache;

public interface CacheFlushService {
    /**
     * Flush the gold cache, and persists the gold of every entries in the database
     */
    void flushCurrencies();
}
