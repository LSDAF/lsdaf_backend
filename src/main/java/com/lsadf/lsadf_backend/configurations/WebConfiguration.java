package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.properties.CorsConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import({
        CorsConfigurationProperties.class,
})
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {
    private static final long MAX_AGE_SECS = 3600;
    private static final String PATH_PATTERN = "/**";

    private final CorsConfigurationProperties corsConfigurationProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(PATH_PATTERN)
                .allowedOrigins(corsConfigurationProperties.getAllowedOrigins().toArray(new String[0]))
                .allowedMethods(corsConfigurationProperties.getAllowedMethods().toArray(new String[0]))
                .maxAge(MAX_AGE_SECS);
    }
}
