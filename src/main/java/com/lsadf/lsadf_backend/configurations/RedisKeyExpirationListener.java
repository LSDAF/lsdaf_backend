package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.exceptions.NotFoundException;
import com.lsadf.lsadf_backend.services.GoldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class RedisKeyExpirationListener implements MessageListener {

    private final GoldService goldService;
    private final RedisTemplate<String, Long> redisTemplate;

    public RedisKeyExpirationListener(GoldService goldService,
                                      RedisTemplate<String, Long> redisTemplate) {
        this.goldService = goldService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith("gold:")) {
            String gameSaveId = expiredKey.substring(5);
            Long gold = redisTemplate.opsForValue().get(expiredKey);
            if (gold == null) {
                return;
            }
            try {
                goldService.saveGold(gameSaveId, gold, false);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
            log.info("Gold of game save {} has been saved to DB -> {}", gameSaveId, gold);
        }
    }
}
