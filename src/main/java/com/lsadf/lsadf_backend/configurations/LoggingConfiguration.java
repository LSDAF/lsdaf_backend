package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.configurations.interceptors.RequestLoggerInterceptor;
import com.lsadf.lsadf_backend.properties.HttpLogProperties;
import com.lsadf.lsadf_backend.services.ClockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {
    @Bean
    public RequestLoggerInterceptor requestLoggerInterceptor(HttpLogProperties httpLogProperties,
                                                             ClockService clockService) {
        return new RequestLoggerInterceptor(httpLogProperties, clockService);
    }
}
