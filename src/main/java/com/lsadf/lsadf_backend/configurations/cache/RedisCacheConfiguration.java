package com.lsadf.lsadf_backend.configurations.cache;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.cache.listeners.RedisKeyExpirationListener;
import com.lsadf.lsadf_backend.services.impl.RedisCacheFlushServiceImpl;
import com.lsadf.lsadf_backend.services.impl.RedisCacheServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.InvalidatedJwtTokenCache;
import com.lsadf.lsadf_backend.cache.impl.RedisCache;
import com.lsadf.lsadf_backend.cache.impl.RedisCurrencyCache;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import com.lsadf.lsadf_backend.services.CurrencyService;
import org.springframework.beans.factory.annotation.Qualifier;
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

import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.*;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.constants.RedisConstants.GAME_SAVE_OWNERSHIP;

@Configuration
@ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "true")
public class RedisCacheConfiguration {


    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisTemplate<String, Long> redisLongTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, Currency> currencyRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Currency> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(Currency.class));
        return template;
    }

    @Bean(name = GAME_SAVE_OWNERSHIP_CACHE)
    public RedisCache<String> gameSaveOwnershipCache(RedisTemplate<String, String> redisTemplate,
                                                     CacheExpirationProperties cacheExpirationProperties,
                                                     RedisProperties redisProperties) {
        return new RedisCache<>(redisTemplate, GAME_SAVE_OWNERSHIP, cacheExpirationProperties.getGameSaveOwnershipExpirationSeconds(), redisProperties);
    }

    @Bean(name = CURRENCY_CACHE)
    public HistoCache<Currency> redisCurrencyCache(RedisTemplate<String, Currency> redisTemplate,
                                                   CacheExpirationProperties cacheExpirationProperties,
                                                   RedisProperties redisProperties) {
        return new RedisCurrencyCache(redisTemplate, cacheExpirationProperties.getCurrencyExpirationSeconds(), redisProperties);
    }

    @Bean(name = INVALIDATED_JWT_TOKEN_CACHE)
    public Cache<String> jwtTokenCache(RedisTemplate<String, String> redisTemplate,
                                       @Qualifier(LOCAL_INVALIDATED_JWT_TOKEN_CACHE) Cache<String> localInvalidatedJwtTokenCache,
                                       RedisProperties redisProperties) {
        return new InvalidatedJwtTokenCache(redisTemplate, localInvalidatedJwtTokenCache, redisProperties);
    }

    @Bean(name = REDIS_CACHE_SERVICE)
    public CacheService redisCacheService(RedisCache<String> gameSaveOwnershipCache,
                                          HistoCache<Currency> currencyCache,
                                          @Qualifier(INVALIDATED_JWT_TOKEN_CACHE) Cache<String> invalidatedJwtTokenCache) {
        return new RedisCacheServiceImpl(gameSaveOwnershipCache, currencyCache, invalidatedJwtTokenCache);
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setDatabase(redisProperties.getDatabase());
        configuration.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisMessageListenerContainer keyExpirationListenerContainer(RedisConnectionFactory connectionFactory,
                                                                        RedisKeyExpirationListener redisKeyExpirationListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisKeyExpirationListener, new PatternTopic("__keyevent@0__:expired"));
        return container;
    }


    @Bean
    MessageListenerAdapter messageListener(RedisKeyExpirationListener redisKeyExpirationListener) {
        return new MessageListenerAdapter(redisKeyExpirationListener);
    }


    @Bean
    public RedisKeyExpirationListener redisKeyExpirationListener(CurrencyService currencyService,
                                                                 RedisTemplate<String, Currency> currencyRedisTemplate) {
        return new RedisKeyExpirationListener(currencyService, currencyRedisTemplate);
    }

    @Bean
    public CacheFlushService cacheFlushService(CurrencyService currencyService,
                                               Cache<Currency> currencyCache) {
        return new RedisCacheFlushServiceImpl(currencyService, currencyCache);
    }
}
