package com.lsadf.lsadf_backend.configurations.cache;

import com.lsadf.lsadf_backend.properties.RedisProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(prefix = "cache.redis", name = "embedded", havingValue = "true")
public class RedisEmbeddedCacheConfiguration {

    private RedisServer redisServer;

    public RedisEmbeddedCacheConfiguration(RedisProperties redisProperties) throws IOException {
        if (redisProperties.isEnabled() && redisProperties.isEmbedded()) {
            this.redisServer = initRedisServer(redisProperties);
        }
    }

    public RedisServer initRedisServer(RedisProperties redisProperties) throws IOException {
        return RedisServer.newRedisServer()
                .setting("requirepass " + redisProperties.getPassword())
                .setting("bind 127.0.0.1")
                .setting("notify-keyspace-events KEA")
                .port(redisProperties.getPort())
                .build();
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        this.redisServer.stop();
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        this.redisServer.start();
    }
}
