package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.configurations.cache.NoRedisCacheConfiguration;
import com.lsadf.lsadf_backend.configurations.cache.RedisCacheConfiguration;
import com.lsadf.lsadf_backend.configurations.cache.RedisEmbeddedCacheConfiguration;
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
@EnableFeignClients(basePackages = "com.lsadf.lsadf_backend.http_clients")
@Import({
        DataSourceConfiguration.class,
        PropertiesConfiguration.class,
        SwaggerConfiguration.class,
        ServiceConfiguration.class,
        WebConfiguration.class,
        SecurityConfiguration.class,
        LoggingConfiguration.class,
        RedisCacheConfiguration.class,
        ApplicationListenerConfiguration.class,
        RedisEmbeddedCacheConfiguration.class,
        RedisCacheConfiguration.class,
        NoRedisCacheConfiguration.class,
        ClockConfiguration.class,
        VelocityConfiguration.class,
})
public class LsadfBackendConfiguration {
}
