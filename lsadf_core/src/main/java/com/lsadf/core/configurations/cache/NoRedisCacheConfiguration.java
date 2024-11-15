package com.lsadf.core.configurations.cache;

import com.lsadf.core.cache.Cache;
import com.lsadf.core.cache.HistoCache;
import com.lsadf.core.cache.impl.NoOpCache;
import com.lsadf.core.cache.impl.NoOpHistoCache;
import com.lsadf.core.models.Characteristics;
import com.lsadf.core.models.Currency;
import com.lsadf.core.services.CacheFlushService;
import com.lsadf.core.services.CacheService;
import com.lsadf.core.services.impl.NoOpCacheServiceImpl;
import com.lsadf.core.services.impl.NoOpFlushServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.lsadf.core.constants.BeanConstants.Cache.*;

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


    @Bean(name = CHARACTERISTICS_CACHE)
    public HistoCache<Characteristics> characteristicsCache() {
        return new NoOpHistoCache<>();
    }

    @Bean(name = CURRENCY_CACHE)
    public HistoCache<Currency> currencyCache() {
        return new NoOpHistoCache<>();
    }

    @Bean(name = GAME_SAVE_OWNERSHIP_CACHE)
    public Cache<String> gameSaveOwnershipCache() {
        return new NoOpCache<>();
    }
}
