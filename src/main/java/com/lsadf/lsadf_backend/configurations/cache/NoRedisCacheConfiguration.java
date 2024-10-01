package com.lsadf.lsadf_backend.configurations.cache;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.services.impl.NoOpCacheServiceImpl;
import com.lsadf.lsadf_backend.services.impl.NoOpFlushServiceImpl;
import com.lsadf.lsadf_backend.cache.impl.NoOpCache;
import com.lsadf.lsadf_backend.cache.impl.NoOpHistoCache;
import com.lsadf.lsadf_backend.models.Currency;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.*;

@Configuration
@ConditionalOnProperty(prefix = "cache.redis", name = "enabled", havingValue = "false")
public class NoRedisCacheConfiguration {

    @Bean
    public CacheService noOpCacheService() {
        return new NoOpCacheServiceImpl();
    }

    @Bean
    public CacheFlushService noOpCacheFlushService() {
        return new NoOpFlushServiceImpl();
    }

    @Bean(name = CURRENCY_CACHE)
    public HistoCache<Currency> currencyCache() {
        return new NoOpHistoCache<>();
    }

    @Bean(name = INVALIDATED_JWT_TOKEN_CACHE)
    public Cache<String> invalidatedJwtTokenCache(@Qualifier(LOCAL_INVALIDATED_JWT_TOKEN_CACHE) Cache<String> localInvalidatedJwtTokenCache) {
        return localInvalidatedJwtTokenCache;
    }

    @Bean(name = GAME_SAVE_OWNERSHIP_CACHE)
    public Cache<String> gameSaveOwnershipCache() {
        return new NoOpCache<>();
    }
}
