package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.models.Currency;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class RedisCacheServiceImpl implements CacheService {

    private final Cache<String> gameSaveOwnershipCache;
    private final HistoCache<Currency> currencyCache;

    private final AtomicBoolean isEnabled = new AtomicBoolean(true);

    public RedisCacheServiceImpl(Cache<String> gameSaveOwnershipCache,
                                 HistoCache<Currency> currencyCache) {
        this.currencyCache = currencyCache;
        this.gameSaveOwnershipCache = gameSaveOwnershipCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isEnabled() {
        return this.isEnabled.get();
    }

    @Override
    public void toggleCacheEnabling() {
        boolean oldValue = isEnabled.get();
        boolean newValue = !oldValue;
        log.info(oldValue ? "Disabling redis caches" : "Enabling redis caches");
        isEnabled.set(newValue);
        currencyCache.setEnabled(newValue);
        gameSaveOwnershipCache.setEnabled(newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCaches() {
        log.info("Clearing all caches");
        currencyCache.clear();
        gameSaveOwnershipCache.clear();
        log.info("Caches cleared");
    }

}