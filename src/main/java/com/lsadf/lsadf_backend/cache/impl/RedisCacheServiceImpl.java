package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.configurations.CacheConfiguration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

public class RedisCacheServiceImpl implements CacheService {

    private static final String GOLD = "gold:";
    private static final String GAME_SAVE_OWNERSHIP = "gamesaveownership:";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisCacheServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Long> getGold(String gameSaveId) {
        String result = redisTemplate.opsForValue().get(GOLD + gameSaveId);
        if (result == null) {
            return Optional.empty();
        }
        return Optional.of(Long.parseLong(result));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGold(String gameSaveId, Long gold) {
        redisTemplate.opsForValue().set(GOLD + gameSaveId, gold.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getGameSaveOwnership(String gameSaveId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(GAME_SAVE_OWNERSHIP + gameSaveId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameSaveOwnership(String gameSaveId, String userEmail) {
        redisTemplate.opsForValue().set(GAME_SAVE_OWNERSHIP + gameSaveId, userEmail);
    }
}
