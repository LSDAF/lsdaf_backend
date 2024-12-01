package com.lsadf.core.configurations;

import com.lsadf.core.configurations.interceptors.RequestLoggerInterceptor;
import com.lsadf.core.properties.HttpLogProperties;
import com.lsadf.core.services.ClockService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {
  @Bean
  public RequestLoggerInterceptor requestLoggerInterceptor(
      HttpLogProperties httpLogProperties, ClockService clockService) {
    return new RequestLoggerInterceptor(httpLogProperties, clockService);
  }
}
