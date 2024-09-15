package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.cache.CacheFlushService;
import com.lsadf.lsadf_backend.properties.ShutdownProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Listener for the application shutdown event.
 */
@Slf4j
public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private final CacheFlushService cacheFlushService;
    private final ShutdownProperties shutdownProperties;


    public ShutdownListener(CacheFlushService cacheFlushService,
                            ShutdownProperties shutdownProperties) {
        this.cacheFlushService = cacheFlushService;
        this.shutdownProperties = shutdownProperties;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("Application is shutting down");

        if (shutdownProperties.isFlushCacheAtShutdown()) {
            log.info("Flushing the cache before shutting down application");
            cacheFlushService.flushCurrencies();
            return;
        }

        log.info("Cache flushing is disabled, shutting down the application");
    }
}
