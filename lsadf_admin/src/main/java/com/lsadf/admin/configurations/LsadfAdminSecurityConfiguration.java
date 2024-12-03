package com.lsadf.admin.configurations;

import static com.lsadf.core.configurations.SecurityConfiguration.ADMIN_URLS;
import static com.lsadf.core.configurations.SecurityConfiguration.WHITELIST_URLS;

import com.lsadf.core.configurations.SecurityConfiguration;
import com.lsadf.core.constants.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

@Configuration
@Import(SecurityConfiguration.class)
public class LsadfAdminSecurityConfiguration {

  @Bean
  public Customizer<
          AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
      customAuthorizationManager() {
    return configurer ->
        configurer
            .requestMatchers(WHITELIST_URLS)
            .permitAll()
            .requestMatchers(ADMIN_URLS)
            .hasAuthority(UserRole.ADMIN.getRole())
            .anyRequest()
            .permitAll();
  }
}
