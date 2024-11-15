package com.lsadf.core.configurations;

import com.lsadf.core.properties.ConfigurationDisplayProperties;
import com.lsadf.core.properties.ShutdownProperties;
import com.lsadf.core.services.CacheFlushService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

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

    @Bean
    public ConfigurationLogger configurationLogger(ConfigurableEnvironment environment,
                                                   ConfigurationDisplayProperties configurationDisplayProperties) {
        return new ConfigurationLogger(environment, configurationDisplayProperties);
    }
}
