package com.lsadf.lsadf_backend.cache.listeners;

import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import static com.lsadf.lsadf_backend.constants.RedisConstants.*;

@Slf4j
public class RedisKeyExpirationListener implements MessageListener {

    private final CurrencyService currencyService;
    private final RedisTemplate<String, Long> longRedisTemplate;
    private final RedisTemplate<String, Currency> currencyRedisTemplate;

    public RedisKeyExpirationListener(CurrencyService currencyService,
                                      RedisTemplate<String, Long> longRedisTemplate,
                                      RedisTemplate<String, Currency> currencyRedisTemplate) {
        this.currencyService = currencyService;
        this.longRedisTemplate = longRedisTemplate;
        this.currencyRedisTemplate = currencyRedisTemplate;
    }

    /**
     * Callback for processing received objects through Redis.
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Redis cache entry expired -> {}", expiredKey);
        if (expiredKey.startsWith(CURRENCY)) {
            String gameSaveId = expiredKey.substring(CURRENCY.length());
            handleExpiredCurrency(gameSaveId);
        }
    }

    private void handleExpiredCurrency(String gameSaveId) {
        try {
            Currency currency = currencyRedisTemplate.opsForValue().get(CURRENCY_HISTO + gameSaveId);
            if (currency == null) {
                throw new NotFoundException("Currency not found in cache");
            }
            currencyService.saveCurrency(gameSaveId, currency, false);
            Boolean result = currencyRedisTemplate.delete(CURRENCY_HISTO + gameSaveId);
            if (Boolean.TRUE.equals(result)) {
                log.info("Deleted entry {}", CURRENCY_HISTO + gameSaveId);
            }
        } catch (DataAccessException | NotFoundException e) {
            log.error("Error while handling expired currency", e);
            throw new RuntimeException(e);
        }
        log.info("Currency of game save {} has been saved to DB", gameSaveId);
    }

}
