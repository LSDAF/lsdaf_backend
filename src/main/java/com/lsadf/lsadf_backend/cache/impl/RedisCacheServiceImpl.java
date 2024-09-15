package com.lsadf.lsadf_backend.cache.impl;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.cache.KeyValueConsumer;
import com.lsadf.lsadf_backend.models.Currency;
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
    private final RedisTemplate<String, Currency> objectRedisTemplate;

    private final CacheProperties cacheProperties;
    private final CacheExpirationProperties cacheExpirationProperties;

    private final AtomicBoolean isEnabled;

    public RedisCacheServiceImpl(CacheProperties cacheProperties,
                                 CacheExpirationProperties cacheExpirationProperties,
                                 RedisTemplate<String, String> stringRedisTemplate,
                                 RedisTemplate<String, Currency> objectRedisTemplate) {
        this.cacheProperties = cacheProperties;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectRedisTemplate = objectRedisTemplate;
        this.cacheExpirationProperties = cacheExpirationProperties;

        this.isEnabled = new AtomicBoolean(cacheProperties.isEnabled());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Currency> getAllCurrenciesHisto() {
        return getAllEntries(CURRENCY_HISTO, objectRedisTemplate);
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
    public Optional<String> getGameSaveOwnership(String gameSaveId) {
        try {
            String owner = stringRedisTemplate.opsForValue().get(GAME_SAVE_OWNERSHIP + gameSaveId);
            return Optional.ofNullable(owner);
        } catch (DataAccessException e) {
            log.warn("Error while getting game save ownership from redis cache", e);
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
            log.warn("Error while setting game save ownership in redis cache", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getAllGameSaveOwnership() {
        return getAllEntries(GAME_SAVE_OWNERSHIP, stringRedisTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCaches() {
        log.info("Clearing all caches");
        clearGameSaveOwnershipCache();
        clearCurrencyCache();
        clearCurrencyHistoCache();
        log.info("Caches cleared");
    }

    @Override
    public Optional<Currency> getCurrency(String gameSaveId) {
        try {
            Currency currency = objectRedisTemplate.opsForValue().get(CURRENCY + gameSaveId);
            return Optional.ofNullable(currency);
        } catch (DataAccessException e) {
            log.warn("Error while getting currency from redis cache", e);
            return Optional.empty();
        }
    }

    @Override
    public void setCurrency(String gameSaveId, Currency currency) {
        try {
            Currency toUpdateCurrency = currency;
            Optional<Currency> optionalCurrency = getCurrency(gameSaveId);
            if (optionalCurrency.isPresent()) {
                toUpdateCurrency = mergeCurrencies(optionalCurrency.get(), currency);
            }
            if (cacheExpirationProperties.getCurrencyExpirationSeconds() > 0) {
                objectRedisTemplate.opsForValue().set(CURRENCY + gameSaveId, toUpdateCurrency, cacheExpirationProperties.getCurrencyExpirationSeconds(), TimeUnit.SECONDS);
            } else {
                objectRedisTemplate.opsForValue().set(CURRENCY + gameSaveId, toUpdateCurrency);
            }
            objectRedisTemplate.opsForValue().set(CURRENCY_HISTO + gameSaveId, currency);
        } catch (DataAccessException e) {
            log.warn("Error while setting currency in redis cache", e);
        }
    }

    @Override
    public Map<String, Currency> getAllCurrencies() {
        return getAllEntries(CURRENCY, objectRedisTemplate);
    }

    /**
     * Clears the game save ownership cache
     */
    private void clearGameSaveOwnershipCache() {
        clearCache(stringRedisTemplate, GAME_SAVE_OWNERSHIP);
    }

    /**
     * Clears the currency cache
     */
    private void clearCurrencyCache() {
        clearCache(objectRedisTemplate, CURRENCY);
    }

    /**
     * Clears the currency histo cache
     */
    private void clearCurrencyHistoCache() {
        clearCache(objectRedisTemplate, CURRENCY_HISTO);
    }

    private static <T> void clearCache(RedisTemplate<String, T> redisTemplate, String keyName) {
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

    /**
     * Get all keyName entries
     *
     * @param keyName       the keyName
     * @param redisTemplate the redis template
     * @return a map of game save id to keyName amount
     */
    private static <T> Map<String, T> getAllEntries(String keyName, RedisTemplate<String, T> redisTemplate) {
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


    private static Currency mergeCurrencies(Currency toUpdate, Currency newCurrency) {
        if (newCurrency.getGold() != null) {
            toUpdate.setGold(newCurrency.getGold());
        }
        if (newCurrency.getDiamond() != null) {
            toUpdate.setDiamond(newCurrency.getDiamond());
        }
        if (newCurrency.getEmerald() != null) {
            toUpdate.setEmerald(newCurrency.getEmerald());
        }
        if (newCurrency.getAmethyst() != null) {
            toUpdate.setAmethyst(newCurrency.getAmethyst());
        }
        return toUpdate;
    }
}