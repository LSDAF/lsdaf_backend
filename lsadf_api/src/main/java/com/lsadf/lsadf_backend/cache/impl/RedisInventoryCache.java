package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.models.Inventory;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.lsadf.lsadf_backend.constants.RedisConstants.INVENTORY;
import static com.lsadf.lsadf_backend.constants.RedisConstants.INVENTORY_HISTO;
import static com.lsadf.lsadf_backend.utils.CacheUtils.clearCache;
import static com.lsadf.lsadf_backend.utils.CacheUtils.getAllEntries;

@Slf4j
public class RedisInventoryCache extends RedisCache<Inventory> implements HistoCache<Inventory> {

    private static final String HISTO_KEY_TYPE = INVENTORY_HISTO;

    public RedisInventoryCache(RedisTemplate<String, Inventory> redisTemplate,
                               int expirationSeconds,
                               RedisProperties redisProperties) {
        super(redisTemplate, INVENTORY, expirationSeconds, redisProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void set(String key, Inventory value) {
        set(key, value, this.expirationSeconds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(String gameSaveId, Inventory inventory, int expirationSeconds) {
        try {
            Inventory toUpdateInventory = inventory;
            Optional<Inventory> optionalInventory = get(gameSaveId);
            if (optionalInventory.isPresent()) {
                toUpdateInventory = mergeInventories(optionalInventory.get(), inventory);
            }
            if (expirationSeconds > 0) {
                redisTemplate.opsForValue().set(keyType + gameSaveId, toUpdateInventory, expirationSeconds, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(keyType + gameSaveId, toUpdateInventory);
            }
            redisTemplate.opsForValue().set(INVENTORY_HISTO + gameSaveId, inventory);
        } catch (DataAccessException e) {
            log.warn("Error while setting inventory in redis cache", e);
        }
    }

    private static Inventory mergeInventories(Inventory toUpdate, Inventory newInventory) {
        // TODO: Implement

        return toUpdate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Inventory> getAllHisto() {
        return getAllEntries(redisTemplate, HISTO_KEY_TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        super.clear();
        clearCache(redisTemplate, HISTO_KEY_TYPE);
    }
}
