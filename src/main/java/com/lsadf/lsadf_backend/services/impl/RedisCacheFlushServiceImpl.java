package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
public class RedisCacheFlushServiceImpl implements CacheFlushService {

    private final CurrencyService currencyService;
    private final Cache<Currency> currencyCache;

    public RedisCacheFlushServiceImpl(CurrencyService currencyService,
                                      Cache<Currency> currencyCache) {
        this.currencyService = currencyService;
        this.currencyCache = currencyCache;
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

}
