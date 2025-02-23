package com.lsadf.core.configurations;

import com.lsadf.core.properties.ShutdownProperties;
import com.lsadf.core.services.CacheFlushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/** Listener for the application shutdown event. */
@Slf4j
public class ShutdownListener implements ApplicationListener<ContextClosedEvent> {

  private final CacheFlushService cacheFlushService;
  private final ShutdownProperties shutdownProperties;

  public ShutdownListener(
      CacheFlushService cacheFlushService, ShutdownProperties shutdownProperties) {
    this.cacheFlushService = cacheFlushService;
    this.shutdownProperties = shutdownProperties;
  }

  @Override
  public void onApplicationEvent(ContextClosedEvent event) {
    log.info("Application is shutting down");

    if (shutdownProperties.isFlushCacheAtShutdown()) {
      log.info("Flushing the cache before shutting down application");
      cacheFlushService.flushStages();
      cacheFlushService.flushCharacteristics();
      cacheFlushService.flushCurrencies();
      return;
    }

    log.info("Cache flushing is disabled, shutting down the application");
  }
}
