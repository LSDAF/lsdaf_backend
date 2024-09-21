package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.properties.DbInitProperties;
import com.lsadf.lsadf_backend.properties.ShutdownProperties;
import com.lsadf.lsadf_backend.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for the application listeners.
 */
@Configuration
public class ApplicationListenerConfiguration {

    @Bean
    public DbInitializer dbInitializer(DbInitProperties dbInitProperties,
                                       PasswordEncoder passwordEncoder,
                                       UserService userService) {
        return new DbInitializer(userService, passwordEncoder, dbInitProperties);
    }

    @Bean
    public ShutdownListener shutdownListener(CacheFlushService cacheFlushService,
                                             ShutdownProperties shutdownProperties) {
        return new ShutdownListener(cacheFlushService, shutdownProperties);
    }
}
