package com.lsadf.core.configurations;

import com.lsadf.core.properties.ClockProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock(ClockProperties clockProperties) {
        if (clockProperties.getTimeZone() != null) {
            ZoneId zoneId = ZoneId.of(clockProperties.getTimeZone());
            return Clock.system(zoneId);
        }
        return Clock.systemDefaultZone();
    }
}