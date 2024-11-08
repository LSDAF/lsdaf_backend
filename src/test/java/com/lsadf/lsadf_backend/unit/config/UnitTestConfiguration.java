package com.lsadf.lsadf_backend.unit.config;

import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.lsadf_backend.configurations.ShutdownListener;
import com.lsadf.lsadf_backend.http_clients.KeycloakAdminClient;
import com.lsadf.lsadf_backend.http_clients.KeycloakClient;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.services.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;

@TestConfiguration
public class UnitTestConfiguration {
    @MockBean
    private LsadfBackendConfiguration lsadfBackendConfiguration;

    @MockBean
    private Mapper mapper;

    @MockBean
    private KeycloakClient keycloakClient;

    @MockBean
    private ShutdownListener shutdownListener;

    @MockBean
    RedisMessageListenerContainer redisMessageListenerContainer;

    @MockBean
    LettuceConnectionFactory lettuceConnectionFactory;

    @MockBean
    private KeycloakAdminClient keycloakAdminClient;

    @MockBean
    private StageService stageService;

    @MockBean
    private UserService userService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private GameSaveService gameSaveService;

    @MockBean
    private CharacteristicsService characteristicsService;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    @Qualifier(REDIS_CACHE_SERVICE)
    private CacheService redisCacheService;

    @MockBean
    private CacheFlushService cacheFlushService;
}
