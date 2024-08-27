package com.lsadf.lsadf_backend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Global Configuration class for the LSADF backend. It imports all other configurations to be used in the application.
 */
@Configuration
@Import({
        DataSourceConfiguration.class,
        PropertiesConfiguration.class,
        SwaggerConfiguration.class,
        ServiceConfiguration.class,
        WebConfiguration.class,
        SecurityConfiguration.class,
        OAuth2Configuration.class,
        LoggingConfiguration.class,
        CacheConfiguration.class
})
public class LsadfBackendConfiguration {
}
