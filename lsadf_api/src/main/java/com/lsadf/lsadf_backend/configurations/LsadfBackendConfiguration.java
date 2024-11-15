package com.lsadf.lsadf_backend.configurations;

import com.lsadf.core.configurations.*;
import com.lsadf.core.configurations.cache.NoRedisCacheConfiguration;
import com.lsadf.core.configurations.cache.RedisCacheConfiguration;
import com.lsadf.core.configurations.cache.RedisEmbeddedCacheConfiguration;
import com.lsadf.core.configurations.keycloak.KeycloakConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Global Configuration class for the LSADF backend. It imports all other configurations to be used in the application.
 */
@Configuration
@EnableScheduling
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.lsadf.core.http_clients")
@Import({
        DataSourceConfiguration.class,
        PropertiesConfiguration.class,
        SwaggerConfiguration.class,
        ServiceConfiguration.class,
        CorsConfiguration.class,
        SecurityConfiguration.class,
        LoggingConfiguration.class,
        RedisCacheConfiguration.class,
        ApplicationListenerConfiguration.class,
        RedisEmbeddedCacheConfiguration.class,
        RedisCacheConfiguration.class,
        NoRedisCacheConfiguration.class,
        ClockConfiguration.class,
        VelocityConfiguration.class,
        KeycloakConfiguration.class
})
public class LsadfBackendConfiguration {
}
