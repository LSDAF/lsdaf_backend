package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
public class RedisCacheFlushServiceImpl implements CacheFlushService {

    private final CurrencyService currencyService;
    private final Cache<Currency> currencyCache;

    private final StageService stageService;
    private final Cache<Stage> stageCache;

    public RedisCacheFlushServiceImpl(CurrencyService currencyService,
                                      StageService stageService,
                                      Cache<Currency> currencyCache,
                                      Cache<Stage> stageCache) {
        this.currencyService = currencyService;
        this.stageService = stageService;
        this.currencyCache = currencyCache;
        this.stageCache = stageCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void flushCurrencies() {
        log.info("Flushing currency cache");
        Map<String, Currency> currencyEntries = currencyCache.getAll();
        for (Map.Entry<String, Currency> entry : currencyEntries.entrySet()) {
            String gameSaveId = entry.getKey();
            Currency currency = entry.getValue();
            try {
                currencyService.saveCurrency(gameSaveId, currency, false);
            } catch (NotFoundException e) {
                log.error("Error while flushing currency cache entry: CurrencyEntity with id {} not found", gameSaveId, e);
            } catch (Exception e) {
                log.error("Error while flushing currency cache entry", e);
            }
        }

        log.info("Flushed {} currencies in DB", currencyEntries.size());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void flushStages() {
        log.info("Flushing stage cache");
        Map<String, Stage> stageEntries = stageCache.getAll();
        for (Map.Entry<String, Stage> entry : stageEntries.entrySet()) {
            String gameSaveId = entry.getKey();
            Stage stage = entry.getValue();
            try {
                stageService.saveStage(gameSaveId, stage, false);
            } catch (NotFoundException e) {
                log.error("Error while flushing stage cache entry: Stage with id {} not found", gameSaveId, e);
            } catch (Exception e) {
                log.error("Error while flushing stage cache entry", e);
            }
        }

        log.info("Flushed {} stages in DB", stageEntries.size());
    }
}
