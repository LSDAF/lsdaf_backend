package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.cache.impl.NoOpCacheServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.RedisCacheServiceImpl;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import com.lsadf.lsadf_backend.services.GoldService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public CacheService redisCacheService(RedisTemplate<String, String> redisTemplate) {
        return new RedisCacheServiceImpl(redisTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "false")
    public CacheService noOpCacheService() {
        return new NoOpCacheServiceImpl();
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setDatabase(redisProperties.getDatabase());
        configuration.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(new MessageListenerAdapter(this), new PatternTopic("__keyevent@0__:expired"));
        return container;
    }


    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public RedisKeyExpirationListener redisKeyExpirationListener(GoldService goldService,
                                                                 RedisTemplate<String, Long> redisTemplate) {
        return new RedisKeyExpirationListener(goldService, redisTemplate);
    }
}
