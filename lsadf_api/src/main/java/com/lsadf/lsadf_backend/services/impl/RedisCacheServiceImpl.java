package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.models.Characteristics;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.models.Currency;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class RedisCacheServiceImpl implements CacheService {

    private final Cache<String> gameSaveOwnershipCache;
    private final HistoCache<Characteristics> characteristicsCache;
    private final HistoCache<Currency> currencyCache;
    private final HistoCache<Stage> stageCache;

    private final AtomicBoolean isEnabled = new AtomicBoolean(true);

    public RedisCacheServiceImpl(Cache<String> gameSaveOwnershipCache,
                                 HistoCache<Characteristics> characteristicsCache,
                                 HistoCache<Currency> currencyCache,
                                 HistoCache<Stage> stageCache) {
        this.characteristicsCache = characteristicsCache;
        this.currencyCache = currencyCache;
        this.stageCache = stageCache;
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
        characteristicsCache.setEnabled(newValue);
        currencyCache.setEnabled(newValue);
        stageCache.setEnabled(newValue);
        gameSaveOwnershipCache.setEnabled(newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCaches() {
        log.info("Clearing all caches");
        characteristicsCache.clear();
        currencyCache.clear();
        stageCache.clear();
        gameSaveOwnershipCache.clear();
        log.info("Caches cleared");
    }

}
