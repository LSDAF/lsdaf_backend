package com.lsadf.lsadf_backend.cache.listeners;

import com.lsadf.lsadf_backend.exceptions.http.NotFoundException;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import static com.lsadf.lsadf_backend.constants.RedisConstants.*;

/**
 * Listener for Redis key expiration events.
 */
@Slf4j
public class RedisKeyExpirationListener implements MessageListener {

    private final CurrencyService currencyService;
    private final StageService stageService;
    private final RedisTemplate<String, Currency> currencyRedisTemplate;
    private final RedisTemplate<String, Stage> stageRedisTemplate;

    public RedisKeyExpirationListener(CurrencyService currencyService,
                                      StageService stageService,
                                      RedisTemplate<String, Currency> currencyRedisTemplate,
                                      RedisTemplate<String, Stage> stageRedisTemplate) {
        this.currencyService = currencyService;
        this.stageService = stageService;
        this.currencyRedisTemplate = currencyRedisTemplate;
        this.stageRedisTemplate = stageRedisTemplate;
    }

    /**
     * Callback for processing received objects through Redis.
     *
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
        } else if (expiredKey.startsWith(STAGE)) {
            String gameSaveId = expiredKey.substring(STAGE.length());
            handleExpiredStage(gameSaveId);
        }
    }

    /**
     * Handles expired stage by saving it to DB.
     * @param gameSaveId game save id
     */
    private void handleExpiredStage(String gameSaveId) {
        try {
            Stage stage = stageRedisTemplate.opsForValue().get(STAGE_HISTO + gameSaveId);
            if (stage == null) {
                throw new NotFoundException("Stage not found in cache");
            }
            stageService.saveStage(gameSaveId, stage, false);
            Boolean result = stageRedisTemplate.delete(STAGE_HISTO + gameSaveId);
            if (Boolean.TRUE.equals(result)) {
                log.info("Deleted entry {}", STAGE_HISTO + gameSaveId);
            }
        } catch (DataAccessException | NotFoundException e) {
            log.error("Error while handling expired stage", e);
            throw e;
        }
        log.info("Stage of game save {} has been saved to DB", gameSaveId);
    }

    /**
     * Handles expired currency by saving it to DB.
     * @param gameSaveId game save id
     */
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
            throw e;
        }
        log.info("Currency of game save {} has been saved to DB", gameSaveId);
    }

}
