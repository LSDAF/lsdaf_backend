package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.constants.RedisConstants;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.lsadf.lsadf_backend.constants.RedisConstants.GAME_SAVE_OWNERSHIP;
import static com.lsadf.lsadf_backend.constants.RedisConstants.GOLD;

@Slf4j
public class RedisCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisTemplate<String, Long> redisLongTemplate;

    private final CacheExpirationProperties cacheExpirationProperties;


    public RedisCacheServiceImpl(CacheExpirationProperties cacheExpirationProperties,
                                 RedisTemplate<String, String> stringRedisTemplate,
                                 RedisTemplate<String, Long> redisLongTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisLongTemplate = redisLongTemplate;
        this.cacheExpirationProperties = cacheExpirationProperties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Long> getGold(String gameSaveId) {
        try {
            Long result = redisLongTemplate.opsForValue().get(GOLD + gameSaveId);
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            log.error("Error while getting gold from redis cache", e);
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGold(String gameSaveId, Long gold) {
        try {
            if (cacheExpirationProperties.getGoldExpirationSeconds() > 0) {
                redisLongTemplate.opsForValue().set(GOLD + gameSaveId, gold, cacheExpirationProperties.getGoldExpirationSeconds(), TimeUnit.SECONDS);
            } else {
                redisLongTemplate.opsForValue().set(GOLD + gameSaveId, gold);
            }
        } catch (DataAccessException e) {
            log.error("Error while setting gold in redis cache", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getGameSaveOwnership(String gameSaveId) {
        try {
            String owner = stringRedisTemplate.opsForValue().get(GAME_SAVE_OWNERSHIP + gameSaveId);
            return Optional.ofNullable(owner);
        } catch (DataAccessException e) {
            log.error("Error while getting game save ownership from redis cache", e);
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameSaveOwnership(String gameSaveId, String userEmail) {
        try {
            if (cacheExpirationProperties.getGameSaveOwnershipExpirationSeconds() > 0) {
                stringRedisTemplate.opsForValue().set(GAME_SAVE_OWNERSHIP + gameSaveId, userEmail, cacheExpirationProperties.getGameSaveOwnershipExpirationSeconds(), TimeUnit.SECONDS);
            } else {
                stringRedisTemplate.opsForValue().set(GAME_SAVE_OWNERSHIP + gameSaveId, userEmail);
            }
        } catch (DataAccessException e) {
            log.error("Error while setting game save ownership in redis cache", e);
        }
    }
}
