package com.lsadf.lsadf_backend.unit.config;

import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.lsadf_backend.configurations.ShutdownListener;
import com.lsadf.lsadf_backend.http_clients.KeycloakAdminClient;
import com.lsadf.lsadf_backend.http_clients.KeycloakClient;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.services.CurrencyService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.StageService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

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
    private GameSaveService gameSaveService;

    @MockBean
    private CurrencyService currencyService;
}
