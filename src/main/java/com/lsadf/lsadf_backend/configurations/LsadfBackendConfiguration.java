package com.lsadf.lsadf_backend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DataSourceConfiguration.class,
        EnvPropertiesResolverConfiguration.class,
        SwaggerConfiguration.class,
        ServiceConfiguration.class
})
public class LsadfBackendConfiguration {
}
