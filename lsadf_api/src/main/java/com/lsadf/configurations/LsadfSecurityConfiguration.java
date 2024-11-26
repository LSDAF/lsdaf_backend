package com.lsadf.configurations;

import com.lsadf.core.configurations.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

import static com.lsadf.core.configurations.SecurityConfiguration.WHITELIST_URLS;

@Configuration
@Import(SecurityConfiguration.class)
public class LsadfSecurityConfiguration {
    @Bean
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> customAuthorizationManager() {
        return configurer -> configurer
                .requestMatchers(WHITELIST_URLS).permitAll()
                .anyRequest().authenticated();
    }
}
