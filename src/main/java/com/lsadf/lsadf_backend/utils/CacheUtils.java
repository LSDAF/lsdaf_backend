package com.lsadf.lsadf_backend.utils;

import lombok.experimental.UtilityClass;
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

@UtilityClass
@Slf4j
public class CacheUtils {

    /**
     * Get all keyName entries
     *
     * @param redisTemplate the redis template
     * @param keyName       the keyName
     * @return a map of game save id to keyName amount
     */
    public static <T> Map<String, T> getAllEntries(RedisTemplate<String, T> redisTemplate, String keyName) {
        String entryType = keyName.substring(0, keyName.length() - 1);
        Map<String, T> currencyMap = new HashMap<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(keyName + "*").build();
        try {
            RedisKeyCommands commands = initRedisKeyCommands(redisTemplate);
            iterateOverKeys(commands, scanOptions, keyName, redisTemplate, currencyMap::put);
        } catch (DataAccessException e) {
            log.warn("Error while getting " + entryType + " from redis cache", e);
        }

        return currencyMap;
    }

    /**
     * Iterates over the keys of the cache
     *
     * @param commands      the RedisKeyCommands
     * @param scanOptions   the scan options
     * @param pattern       the pattern to match
     * @param redisTemplate the redis template
     * @param consumer      the consumer
     * @param <T>           the type of the value
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
            log.warn("Error while scanning entries from gold cache", e);
        }
    }

    /**
     * Initialize the RedisKeyCommands
     *
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
     * Clear the cache
     * @param redisTemplate the redis template
     * @param keyName the key name
     * @param <T> the type of the value
     */
    public static <T> void clearCache(RedisTemplate<String, T> redisTemplate, String keyName) {
        String entryType = keyName.substring(0, keyName.length() - 1);
        log.info("Clearing {} cache", entryType);
        RedisKeyCommands commands = initRedisKeyCommands(redisTemplate);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(keyName + "*").build();
        try {
            iterateOverKeys(commands, scanOptions, keyName, redisTemplate, (gameSaveId, value) -> {
                var result = redisTemplate.delete(keyName + gameSaveId);
                log.info("Deleted key: {} with result: {}", gameSaveId, result);
            });
            log.info("{} cache cleared", entryType);
        } catch (DataAccessException e) {
            log.warn("Error while clearing {} cache", entryType, e);
        }
    }
}
