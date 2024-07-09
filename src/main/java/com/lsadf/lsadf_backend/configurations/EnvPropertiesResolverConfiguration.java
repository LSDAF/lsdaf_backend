package com.lsadf.lsadf_backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Configuration class for the .env properties resolver.
 */
@Configuration
public class EnvPropertiesResolverConfiguration {
    private static final String ENV_FILE = ".env";

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        Resource resource = new ClassPathResource(ENV_FILE);
        configurer.setLocation(resource);

        return configurer;
    }
}
