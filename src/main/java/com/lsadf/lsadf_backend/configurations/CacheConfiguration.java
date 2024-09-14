package com.lsadf.lsadf_backend.configurations;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lsadf.lsadf_backend.cache.CacheFlushService;
import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.cache.RedisKeyExpirationListener;
import com.lsadf.lsadf_backend.cache.impl.CacheFlushServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.NoOpCacheServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.NoOpFlushServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.RedisCacheServiceImpl;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import com.lsadf.lsadf_backend.properties.CacheProperties;
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
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

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
    public RedisTemplate<String, Long> redisLongTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public RedisTemplate<String, LocalUser> redisLocalUserTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, LocalUser> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(LocalUser.class));
        return template;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public CacheService redisCacheService(CacheProperties cacheProperties,
                                          CacheExpirationProperties cacheExpirationProperties,
                                          RedisTemplate<String, String> stringRedisTemplate,
                                          RedisTemplate<String, Long> longRedisTemplate,
                                          RedisTemplate<String, LocalUser> redisLocalUserTemplate) {
        return new RedisCacheServiceImpl(cacheProperties,
                cacheExpirationProperties,
                stringRedisTemplate,
                longRedisTemplate,
                redisLocalUserTemplate);
    }

    @Bean
    public Cache<String, LocalUser> localUserCache(CacheExpirationProperties cacheExpirationProperties) {
        var builder = Caffeine.newBuilder()
                .maximumSize(100);
        if (cacheExpirationProperties.getLocalUserExpirationSeconds() > 0) {
            builder.expireAfterWrite(cacheExpirationProperties.getLocalUserExpirationSeconds(), TimeUnit.SECONDS);
        }

        return builder.build();
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
    public RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory connectionFactory,
                                                                        RedisKeyExpirationListener redisKeyExpirationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisKeyExpirationListener, new PatternTopic("__keyevent@0__:expired"));
        return container;
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    MessageListenerAdapter messageListener(RedisKeyExpirationListener redisKeyExpirationListener) {
        return new MessageListenerAdapter(redisKeyExpirationListener);
    }


    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public RedisKeyExpirationListener redisKeyExpirationListener(GoldService goldService,
                                                                 RedisTemplate<String, Long> redisTemplate) {
        return new RedisKeyExpirationListener(goldService, redisTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "true")
    public CacheFlushService cacheFlushService(CacheService cacheService, GoldService goldService) {
        return new CacheFlushServiceImpl(cacheService, goldService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "enabled", havingValue = "false")
    public CacheFlushService noOpCacheFlushService() {
        return new NoOpFlushServiceImpl();
    }
}
