package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheFlushService;
import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
public class CacheFlushServiceImpl implements CacheFlushService {

    private final CacheService cacheService;
    private final CurrencyService currencyService;


    public CacheFlushServiceImpl(CacheService cacheService, CurrencyService currencyService) {
        this.cacheService = cacheService;
        this.currencyService = currencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void flushCurrencies() {
        log.info("Flushing gold cache");
        Map<String, Currency> goldCacheEntries = cacheService.getAllCurrencies();
        for (Map.Entry<String, Currency> entry : goldCacheEntries.entrySet()) {
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

        log.info("Flushed {} currencies in DB", goldCacheEntries.size());
    }

}
