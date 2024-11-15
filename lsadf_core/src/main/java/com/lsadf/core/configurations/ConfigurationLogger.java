package com.lsadf.core.configurations;

import com.lsadf.core.properties.ConfigurationDisplayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
public class ConfigurationLogger implements ApplicationListener<ApplicationReadyEvent> {

    private final ConfigurableEnvironment environment;
    private final ConfigurationDisplayProperties configurationDisplayProperties;

    public ConfigurationLogger(ConfigurableEnvironment environment, ConfigurationDisplayProperties configurationDisplayProperties) {
        this.environment = environment;
        this.configurationDisplayProperties = configurationDisplayProperties;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (configurationDisplayProperties.isEnabled()) {
            log.info("Displaying configuration properties:");
            environment.getPropertySources().forEach(source -> {
                if (source.getSource() instanceof java.util.Map) {
                    ((java.util.Map<?, ?>) source.getSource()).forEach((key, value) -> {
                        String resolvedValue = environment.getProperty((String) key);
                        log.info("Property: {} Value: {}", key, resolvedValue);
                    });
                }
            });
        }
    }
}
