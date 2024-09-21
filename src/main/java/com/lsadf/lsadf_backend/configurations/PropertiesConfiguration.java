package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.properties.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "http-log")
    public HttpLogProperties httpLogProperties() {
        return new HttpLogProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "cache")
    public CacheProperties cacheProperties() {
        return new CacheProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "clock")
    public ClockProperties clockProperties() {
        return new ClockProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "cache.local")
    public LocalCacheProperties localProperties() {
        return new LocalCacheProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "cache.expiration")
    public CacheExpirationProperties cacheExpirationProperties() {
        return new CacheExpirationProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "cache.redis")
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "shutdown")
    public ShutdownProperties shutdownProperties() {
        return new ShutdownProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "db")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "auth")
    public AuthProperties authProperties() {
        return new AuthProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "db.init")
    public DbInitProperties dbInitProperties() {
        return new DbInitProperties();
    }

    @ConfigurationProperties(prefix = "swagger")
    @Bean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @ConfigurationProperties(prefix = "swagger.contact")
    @Bean
    public SwaggerContactProperties swaggerContactProperties() {
        return new SwaggerContactProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "cors")
    public CorsConfigurationProperties corsConfigurationProperties() {
        return new CorsConfigurationProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "oauth2")
    public OAuth2Properties oAuth2Properties() {
        return new OAuth2Properties();
    }
}
