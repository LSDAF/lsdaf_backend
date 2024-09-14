package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.cache.KeyValueConsumer;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import com.lsadf.lsadf_backend.properties.CacheProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.lsadf.lsadf_backend.constants.RedisConstants.*;

@Slf4j
public class RedisCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisTemplate<String, Long> redisLongTemplate;
    private final RedisTemplate<String, LocalUser> redisLocalUserTemplate;

    private final CacheProperties cacheProperties;
    private final CacheExpirationProperties cacheExpirationProperties;

    private final AtomicBoolean isEnabled;

    public RedisCacheServiceImpl(CacheProperties cacheProperties,
                                 CacheExpirationProperties cacheExpirationProperties,
                                 RedisTemplate<String, String> stringRedisTemplate,
                                 RedisTemplate<String, Long> redisLongTemplate,
                                 RedisTemplate<String, LocalUser> redisLocalUserTemplate) {
        this.cacheProperties = cacheProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisLongTemplate = redisLongTemplate;
        this.redisLocalUserTemplate = redisLocalUserTemplate;
        this.cacheExpirationProperties = cacheExpirationProperties;

        this.isEnabled = new AtomicBoolean(cacheProperties.isEnabled());
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
        log.info(oldValue ? "Disabling cache" : "Enabling cache");
        isEnabled.set(!isEnabled.get());
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
            redisLongTemplate.opsForValue().set(GOLD_HISTO + gameSaveId, gold);
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


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getAllGold() {
        Map<String, Long> goldMap = new HashMap<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(GOLD + "*").build();
        try {
            RedisKeyCommands commands = initRedisKeyCommands(redisLongTemplate);
            iterateOverKeys(commands, scanOptions, GOLD, redisLongTemplate, goldMap::put);
        } catch (DataAccessException e) {
            log.error("Error while getting all gold from redis cache", e);
        }
        return goldMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getAllGoldHisto() {
        Map<String, Long> goldHistoMap = new HashMap<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(GOLD_HISTO + "*").build();
        try {
            RedisKeyCommands commands = initRedisKeyCommands(redisLongTemplate);
            iterateOverKeys(commands, scanOptions, GOLD_HISTO, redisLongTemplate, goldHistoMap::put);
        } catch (DataAccessException e) {
            log.error("Error while getting all gold from redis cache", e);
        }
        return goldHistoMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getAllGameSaveOwnership() {
        Map<String, String> GameSaveOwnershipMap = new HashMap<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(GAME_SAVE_OWNERSHIP + "*").build();
        try {
            RedisKeyCommands commands = initRedisKeyCommands(stringRedisTemplate);
            iterateOverKeys(commands, scanOptions, GAME_SAVE_OWNERSHIP, stringRedisTemplate, GameSaveOwnershipMap::put);
        } catch (DataAccessException e) {
            log.error("Error while getting all gold from redis cache", e);
        }
        return GameSaveOwnershipMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCaches() {
        log.info("Clearing all caches");
        clearGoldCache();
        clearGoldHistoCache();
        clearGameSaveOwnershipCache();
        clearLocalUserCache();
        log.info("Caches cleared");
    }

    /**
     * Clears the gold histo cache
     */
    private void clearGoldHistoCache() {
        log.info("Clearing gold histo cache");
        RedisKeyCommands commands = initRedisKeyCommands(redisLongTemplate);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(GOLD_HISTO + "*").build();
        try {
            iterateOverKeys(commands, scanOptions, GOLD_HISTO, redisLongTemplate, (gameSaveId, value) -> {
                var result = redisLongTemplate.delete(GOLD_HISTO + gameSaveId);
                log.info("Deleted key: {} with result: {}", gameSaveId, result);
            });
            log.info("Gold histo cache cleared");
        } catch (DataAccessException e) {
            log.error("Error while clearing gold histo cache", e);
        }
    }


    /**
     * Clears the gold cache
     */
    private void clearGoldCache() {
        log.info("Clearing gold cache");
        RedisKeyCommands commands = initRedisKeyCommands(redisLongTemplate);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(GOLD + "*").build();
        try {
            iterateOverKeys(commands, scanOptions, GOLD, redisLongTemplate, (gameSaveId, value) -> {
                var result = redisLongTemplate.delete(GOLD + gameSaveId);
                log.info("Deleted key: {} with result: {}", gameSaveId, result);
            });
            log.info("Gold cache cleared");
        } catch (DataAccessException e) {
            log.error("Error while clearing gold cache", e);
        }
    }

    /**
     * Clears the local user cache
     */
    private void clearLocalUserCache() {
        log.info("Clearing local user cache");
        RedisKeyCommands commands = initRedisKeyCommands(redisLocalUserTemplate);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(LOCAL_USER + "*").build();
        try {
            iterateOverKeys(commands, scanOptions, LOCAL_USER, redisLocalUserTemplate, (email, value) -> {
                var result = redisLocalUserTemplate.delete(LOCAL_USER + email);
                log.info("Deleted key: {} with result: {}", email, result);
            });
            log.info("Local user cache cleared");
        } catch (DataAccessException e) {
            log.error("Error while clearing local user cache", e);
        }
    }

    /**
     * Clears the game save ownership cache
     */
    private void clearGameSaveOwnershipCache() {
        log.info("Clearing game save ownership cache");
        RedisKeyCommands commands = initRedisKeyCommands(stringRedisTemplate);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(GAME_SAVE_OWNERSHIP + "*").build();
        try {
            iterateOverKeys(commands, scanOptions, GAME_SAVE_OWNERSHIP, stringRedisTemplate, (gameSaveId, value) -> {
                var result = stringRedisTemplate.delete(GAME_SAVE_OWNERSHIP + gameSaveId);
                log.info("Deleted key: {} with result: {}", gameSaveId, result);
            });
            log.info("Game save ownership cache cleared");
        } catch (DataAccessException e) {
            log.error("Error while clearing game save ownership cache", e);
        }
    }

    /**
     * Initialize the RedisKeyCommands
     * @param redisTemplate the redis template
     * @return the RedisKeyCommands
     */
    private static RedisKeyCommands initRedisKeyCommands(RedisTemplate<?, ?> redisTemplate) {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory == null) {
            throw new RedisConnectionFailureException("No RedisConnectionFactory available");
        }
        RedisConnection connection = connectionFactory.getConnection();
        return connection.keyCommands();
    }

    /**
     * Iterates over the keys of the cache
     * @param commands the RedisKeyCommands
     * @param scanOptions the scan options
     * @param pattern the pattern to match
     * @param redisTemplate the redis template
     * @param consumer the consumer
     * @param <T> the type of the value
     */
    private static <T> void iterateOverKeys(RedisKeyCommands commands,
                                            ScanOptions scanOptions,
                                            String pattern,
                                            RedisTemplate<String, T> redisTemplate,
                                            KeyValueConsumer<T> consumer) {
        try (Cursor<byte[]> cursor = commands.scan(scanOptions)) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                String gameSaveId = key.substring(pattern.length());
                T value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    consumer.accept(gameSaveId, value);
                }
            }
        } catch (Exception e) {
            log.error("Error while scanning entries from gold cache", e);
        }
    }
}