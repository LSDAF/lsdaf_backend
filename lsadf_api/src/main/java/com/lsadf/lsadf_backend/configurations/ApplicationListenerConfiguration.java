package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.properties.ShutdownProperties;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the application listeners.
 */
@Configuration
public class ApplicationListenerConfiguration {

    @Bean
    public ShutdownListener shutdownListener(CacheFlushService cacheFlushService,
                                             ShutdownProperties shutdownProperties) {
        return new ShutdownListener(cacheFlushService, shutdownProperties);
    }
}
