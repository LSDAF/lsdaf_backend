package com.lsadf.lsadf_backend.configurations;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Velocity engine
 */
@Configuration
public class VelocityConfiguration {

    @Bean
    public VelocityEngine velocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loaders", "file");
        velocityEngine.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.setProperty("resource.loader.file.path", "BOOT-INF/classes/templates");

        velocityEngine.init();
        return velocityEngine;
    }
}
