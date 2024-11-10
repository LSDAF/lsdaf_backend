package com.lsadf.lsadf_backend.services.impl;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.models.Characteristics;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.services.*;
import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
public class RedisCacheFlushServiceImpl implements CacheFlushService {

    private final CharacteristicsService characteristicsService;
    private final Cache<Characteristics> characteristicsCache;

    private final CurrencyService currencyService;
    private final Cache<Currency> currencyCache;

    private final InventoryService inventoryService;
    private final Cache<Inventory> inventoryCache;

    private final StageService stageService;
    private final Cache<Stage> stageCache;

    public RedisCacheFlushServiceImpl(CharacteristicsService characteristicsService,
                                      CurrencyService currencyService,
                                      InventoryService inventoryService,
                                      StageService stageService,
                                      Cache<Characteristics> characteristicsCache,
                                      Cache<Currency> currencyCache,
                                      Cache<Inventory> inventoryCache,
                                      Cache<Stage> stageCache) {
        this.characteristicsService = characteristicsService;
        this.currencyService = currencyService;
        this.inventoryService = inventoryService;
        this.stageService = stageService;
        this.characteristicsCache = characteristicsCache;
        this.currencyCache = currencyCache;
        this.inventoryCache = inventoryCache;
        this.stageCache = stageCache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void flushCharacteristics() {
        log.info("Flushing characteristics cache");
        Map<String, Characteristics> characteristicsEntries = characteristicsCache.getAll();
        for (Map.Entry<String, Characteristics> entry : characteristicsEntries.entrySet()) {
            String gameSaveId = entry.getKey();
            Characteristics characteristics = entry.getValue();
            try {
                characteristicsService.saveCharacteristics(gameSaveId, characteristics, false);
            } catch (NotFoundException e) {
                log.error("Error while flushing characteristics cache entry: CharacteristicsEntity with id {} not found", gameSaveId, e);
            } catch (Exception e) {
                log.error("Error while flushing characteristics cache entry", e);
            }
        }

        log.info("Flushed {} characteristics in DB", characteristicsEntries.size());
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
    public void flushInventories() {
        log.info("Flushing inventory cache");
        Map<String, Inventory> inventoryEntries = inventoryCache.getAll();
        for (Map.Entry<String, Inventory> entry : inventoryEntries.entrySet()) {
            String gameSaveId = entry.getKey();
            Inventory inventory = entry.getValue();
            try {
                inventoryService.saveInventory(gameSaveId, inventory, false);
            } catch (NotFoundException e) {
                log.error("Error while flushing inventory cache entry: InventoryEntity with id {} not found", gameSaveId, e);
            } catch (Exception e) {
                log.error("Error while flushing inventory cache entry", e);
            }
        }

        log.info("Flushed {} inventories in DB", inventoryEntries.size());
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
