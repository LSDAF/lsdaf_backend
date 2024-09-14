package com.lsadf.lsadf_backend.cache;

import com.lsadf.lsadf_backend.constants.RedisConstants;
import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.services.GoldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import static com.lsadf.lsadf_backend.constants.RedisConstants.GOLD_HISTO;

@Slf4j
public class RedisKeyExpirationListener implements MessageListener {

    private final GoldService goldService;
    private final RedisTemplate<String, Long> longRedisTemplate;

    public RedisKeyExpirationListener(GoldService goldService,
                                      RedisTemplate<String, Long> longRedisTemplate) {
        this.goldService = goldService;
        this.longRedisTemplate = longRedisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Redis entry expired -> {}", expiredKey);
        if (expiredKey.startsWith(RedisConstants.GOLD)) {
            String gameSaveId = expiredKey.substring(5);
            handleExpiredGold(gameSaveId);
        }
    }

    private void handleExpiredGold(String gameSaveId) {
        try {
            Long gold = longRedisTemplate.opsForValue().get(GOLD_HISTO + gameSaveId);
            if (gold != null) {
                goldService.saveGold(gameSaveId, gold, false);
            }
            var result = longRedisTemplate.delete(GOLD_HISTO + gameSaveId);
            if (Boolean.TRUE.equals(result)) {
                log.info("Deleted entry {}", GOLD_HISTO + gameSaveId);
            }
        } catch (DataAccessException | NotFoundException e) {
            throw new RuntimeException(e);
        }
        log.info("Gold of game save {} has been saved to DB", gameSaveId);
    }
}
