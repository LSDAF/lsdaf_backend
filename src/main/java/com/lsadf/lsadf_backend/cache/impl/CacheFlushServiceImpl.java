package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheFlushService;
import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.services.GoldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
public class CacheFlushServiceImpl implements CacheFlushService {

    private final CacheService cacheService;
    private final GoldService goldService;


    public CacheFlushServiceImpl(CacheService cacheService, GoldService goldService) {
        this.cacheService = cacheService;
        this.goldService = goldService;
    }

    @Override
    @Transactional
    public void flushGold() {
        log.info("Flushing gold cache");
        Map<String, Long> goldCacheEntries = cacheService.getAllGold();
        for (Map.Entry<String, Long> entry : goldCacheEntries.entrySet()) {
            String gameSaveId = entry.getKey();
            Long gold = entry.getValue();
            try {
                goldService.saveGold(gameSaveId, gold, false);

            } catch (NotFoundException e) {
                log.error("Error while flushing gold cache entry: goldEntity with id {} not found", gameSaveId, e);
            } catch (Exception e) {
                log.error("Error while flushing gold cache entry", e);
            }
        }

        log.info("Flushed {} gold entries in DB", goldCacheEntries.size());
    }
}
