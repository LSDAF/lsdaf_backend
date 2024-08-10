package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.configurations.interceptors.RequestLoggerInterceptor;
import com.lsadf.lsadf_backend.properties.HttpLogProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class LoggingConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "http-log")
    public HttpLogProperties httpLogProperties() {
        return new HttpLogProperties();
    }

    @Bean
    public RequestLoggerInterceptor requestLoggerInterceptor(HttpLogProperties httpLogProperties) {
        return new RequestLoggerInterceptor(httpLogProperties);
    }

}
