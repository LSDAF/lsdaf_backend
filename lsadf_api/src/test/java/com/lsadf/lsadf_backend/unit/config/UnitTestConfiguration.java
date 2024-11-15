package com.lsadf.lsadf_backend.unit.config;

import com.lsadf.core.services.*;
import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.core.configurations.ShutdownListener;
import com.lsadf.core.http_clients.KeycloakAdminClient;
import com.lsadf.core.http_clients.KeycloakClient;
import com.lsadf.core.mappers.Mapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.sql.DataSource;

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
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @MockBean
    private LettuceConnectionFactory lettuceConnectionFactory;

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
    private InventoryService inventoryService;

    @MockBean
    private CacheService redisCacheService;

    @MockBean
    private CacheFlushService cacheFlushService;

    @MockBean
    private DataSource dataSource;
}
