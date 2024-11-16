package com.lsadf.core.configurations;

import com.lsadf.core.configurations.interceptors.RequestLoggerInterceptor;
import com.lsadf.core.configurations.keycloak.KeycloakJwtAuthenticationConverter;
import com.lsadf.core.properties.HttpLogProperties;
import com.lsadf.core.properties.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collection;

import static com.lsadf.core.constants.ControllerConstants.ADMIN;

@Configuration
@Import({
        OAuth2Properties.class
})
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SecurityConfiguration implements WebMvcConfigurer {

    private RequestLoggerInterceptor requestLoggerInterceptor;

    private HttpLogProperties httpLogProperties;

    @Autowired
    public SecurityConfiguration(RequestLoggerInterceptor requestLoggerInterceptor,
                                 HttpLogProperties httpLogProperties) {
        this.requestLoggerInterceptor = requestLoggerInterceptor;
        this.httpLogProperties = httpLogProperties;
    }

    public static final String[] WHITELIST_URLS = {
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/oauth2/callback",
            "/error",
            "/actuator",
            "/actuator/**"
    };

    public static final String ADMIN_URLS = ADMIN + "/**";


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security,
                                           CorsFilter corsFilter,
                                           JwtAuthenticationConverter customJwtAuthenticationProvider,
                                           Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> requestMatcherRegistry) throws Exception {
        security
                .addFilter(corsFilter)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requestMatcherRegistry)
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/oauth2/login"))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationProvider)));

        return security.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (httpLogProperties.isEnabled()) {
            registry.addInterceptor(requestLoggerInterceptor);
        }
    }
}
