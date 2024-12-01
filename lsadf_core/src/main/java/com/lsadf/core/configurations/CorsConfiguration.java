package com.lsadf.core.configurations;

import com.lsadf.core.properties.CorsConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfiguration {
  private static final long MAX_AGE_SECS = 3600;
  private static final String PATH_PATTERN = "/**";

  @Bean
  public org.springframework.web.cors.CorsConfiguration springCorsConfiguration(
      CorsConfigurationProperties corsConfigurationProperties) {
    org.springframework.web.cors.CorsConfiguration corsConfiguration =
        new org.springframework.web.cors.CorsConfiguration();
    // corsConfiguration.setAllowedOrigins(corsConfigurationProperties.getAllowedOrigins());
    corsConfiguration.setAllowedMethods(corsConfigurationProperties.getAllowedMethods());
    corsConfiguration.setAllowedHeaders(corsConfigurationProperties.getAllowedHeaders());
    corsConfiguration.setAllowCredentials(corsConfigurationProperties.getAllowCredentials());
    corsConfiguration.setAllowedOriginPatterns(corsConfigurationProperties.getAllowedOrigins());
    corsConfiguration.setMaxAge(MAX_AGE_SECS);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(PATH_PATTERN, corsConfiguration);

    return corsConfiguration;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(
      org.springframework.web.cors.CorsConfiguration corsConfiguration) {
    var configurationSource = new UrlBasedCorsConfigurationSource();
    configurationSource.registerCorsConfiguration(PATH_PATTERN, corsConfiguration);

    return configurationSource;
  }

  @Bean
  public CorsFilter corsFilter(CorsConfigurationSource corsConfigurationSource) {
    return new CorsFilter(corsConfigurationSource);
  }
}
