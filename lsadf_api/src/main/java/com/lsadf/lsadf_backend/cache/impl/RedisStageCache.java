package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.core.models.Stage;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.lsadf.core.constants.RedisConstants.STAGE;
import static com.lsadf.core.constants.RedisConstants.STAGE_HISTO;
import static com.lsadf.lsadf_backend.utils.CacheUtils.clearCache;
import static com.lsadf.lsadf_backend.utils.CacheUtils.getAllEntries;

@Slf4j
public class RedisStageCache extends RedisCache<Stage> implements HistoCache<Stage> {
    private static final String HISTO_KEY_TYPE = STAGE_HISTO;

    public RedisStageCache(RedisTemplate<String, Stage> redisTemplate,
                           int expirationSeconds,
                           RedisProperties redisProperties) {
        super(redisTemplate, STAGE, expirationSeconds, redisProperties);
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void set(String gameSaveId, Stage stage, int expirationSeconds) {
        try {
            Stage toUpdateStage = stage;
            Optional<Stage> optionalStage = get(gameSaveId);
            if (optionalStage.isPresent()) {
                toUpdateStage = mergeStages(optionalStage.get(), stage);
            }
            if (expirationSeconds > 0) {
                redisTemplate.opsForValue().set(keyType + gameSaveId, toUpdateStage, expirationSeconds, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(keyType + gameSaveId, toUpdateStage);
            }
            redisTemplate.opsForValue().set(STAGE_HISTO + gameSaveId, toUpdateStage);
        } catch (DataAccessException e) {
            log.warn("Error while setting currency in redis cache", e);
        }
    }

    @Override
    public void set(String gameSaveId, Stage stage) {
        set(gameSaveId, stage, this.expirationSeconds);
    }

    private static Stage mergeStages(Stage toUpdate, Stage newCurrency) {
        if (newCurrency.getCurrentStage() != null) {
            toUpdate.setCurrentStage(newCurrency.getCurrentStage());
        }
        if (newCurrency.getMaxStage() != null) {
            toUpdate.setMaxStage(newCurrency.getMaxStage());
        }
        return toUpdate;
    }

    @Override
    public Map<String, Stage> getAllHisto() {
        return getAllEntries(redisTemplate, HISTO_KEY_TYPE);
    }

    @Override
    public void clear() {
        super.clear();
        clearCache(redisTemplate, HISTO_KEY_TYPE);
    }

    @Override
    public void unset(String key) {
        super.unset(key);
        try {
            redisTemplate.delete(STAGE_HISTO + key);
        } catch (DataAccessException e) {
            log.warn("Error while deleting element from redis cache", e);
        }
    }
}
