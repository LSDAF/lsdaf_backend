package com.lsadf.admin.unit.config;

import com.lsadf.core.configurations.ShutdownListener;
import com.lsadf.core.http_clients.KeycloakClient;
import com.lsadf.core.mappers.Mapper;
import com.lsadf.core.services.*;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@TestConfiguration
public class UnitTestConfiguration {

  @MockBean private Mapper mapper;

  @MockBean private KeycloakClient keycloakClient;

  @MockBean private ShutdownListener shutdownListener;

  @MockBean private RedisMessageListenerContainer redisMessageListenerContainer;

  @MockBean private LettuceConnectionFactory lettuceConnectionFactory;

  @MockBean private StageService stageService;

  @MockBean private UserService userService;

  @MockBean private SearchService searchService;

  @MockBean private GameSaveService gameSaveService;

  @MockBean private CharacteristicsService characteristicsService;

  @MockBean private CurrencyService currencyService;

  @MockBean private InventoryService inventoryService;

  @MockBean private CacheService redisCacheService;

  @MockBean private CacheFlushService cacheFlushService;

  @MockBean private DataSource dataSource;
}
