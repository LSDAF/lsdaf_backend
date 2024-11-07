package com.lsadf.lsadf_backend.configurations.cache;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.cache.impl.RedisCache;
import com.lsadf.lsadf_backend.cache.impl.RedisCharacteristicsCache;
import com.lsadf.lsadf_backend.cache.impl.RedisCurrencyCache;
import com.lsadf.lsadf_backend.cache.impl.RedisStageCache;
import com.lsadf.lsadf_backend.cache.listeners.RedisKeyExpirationListener;
import com.lsadf.lsadf_backend.constants.EntityAttributes;
import com.lsadf.lsadf_backend.models.Characteristics;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.Stage;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import com.lsadf.lsadf_backend.properties.RedisProperties;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.StageService;
import com.lsadf.lsadf_backend.services.impl.RedisCacheFlushServiceImpl;
import com.lsadf.lsadf_backend.services.impl.RedisCacheServiceImpl;
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
    public RedisTemplate<String, Characteristics> characteristicsRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Characteristics> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(Characteristics.class));
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

    @Bean
    public RedisTemplate<String, Stage> stageRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Stage> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericToStringSerializer<>(Stage.class));
        return template;
    }

    @Bean(name = GAME_SAVE_OWNERSHIP_CACHE)
    public RedisCache<String> gameSaveOwnershipCache(RedisTemplate<String, String> redisTemplate,
                                                     CacheExpirationProperties cacheExpirationProperties,
                                                     RedisProperties redisProperties) {
        return new RedisCache<>(redisTemplate, GAME_SAVE_OWNERSHIP, cacheExpirationProperties.getGameSaveOwnershipExpirationSeconds(), redisProperties);
    }

    @Bean(name = CHARACTERISTICS_CACHE)
    public HistoCache<Characteristics> redisCharacteristicsCache(RedisTemplate<String, Characteristics> redisTemplate,
                                                                 CacheExpirationProperties cacheExpirationProperties,
                                                                 RedisProperties redisProperties) {
        return new RedisCharacteristicsCache(redisTemplate, cacheExpirationProperties.getCharacteristicsExpirationSeconds(), redisProperties);
    }

    @Bean(name = CURRENCY_CACHE)
    public HistoCache<Currency> redisCurrencyCache(RedisTemplate<String, Currency> redisTemplate,
                                                   CacheExpirationProperties cacheExpirationProperties,
                                                   RedisProperties redisProperties) {
        return new RedisCurrencyCache(redisTemplate, cacheExpirationProperties.getCurrencyExpirationSeconds(), redisProperties);
    }

    @Bean(name = STAGE_CACHE)
    public HistoCache<Stage> redisStageCache(RedisTemplate<String, Stage> redisTemplate,
                                             CacheExpirationProperties cacheExpirationProperties,
                                             RedisProperties redisProperties) {
        return new RedisStageCache(redisTemplate, cacheExpirationProperties.getStageExpirationSeconds(), redisProperties);
    }

    @Bean(name = REDIS_CACHE_SERVICE)
    public CacheService redisCacheService(RedisCache<String> gameSaveOwnershipCache,
                                          HistoCache<Currency> currencyCache,
                                          HistoCache<Stage> stageCache) {
        return new RedisCacheServiceImpl(gameSaveOwnershipCache, currencyCache, stageCache);
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
                                                                 StageService stageService,
                                                                 RedisTemplate<String, Currency> currencyRedisTemplate,
                                                                 RedisTemplate<String, Stage> stageRedisTemplate) {
        return new RedisKeyExpirationListener(currencyService, stageService, currencyRedisTemplate, stageRedisTemplate);
    }

    @Bean
    public CacheFlushService cacheFlushService(CurrencyService currencyService,
                                               StageService stageService,
                                               Cache<Currency> currencyCache,
                                               Cache<Stage> stageCache) {
        return new RedisCacheFlushServiceImpl(currencyService, stageService, currencyCache, stageCache);
    }
}
