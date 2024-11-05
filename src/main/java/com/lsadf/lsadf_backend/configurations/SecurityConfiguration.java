package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.configurations.interceptors.RequestLoggerInterceptor;
import com.lsadf.lsadf_backend.configurations.keycloak.KeycloakJwtAuthenticationConverter;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.properties.CorsConfigurationProperties;
import com.lsadf.lsadf_backend.properties.HttpLogProperties;
import com.lsadf.lsadf_backend.properties.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collection;

import static com.lsadf.lsadf_backend.constants.ControllerConstants.ADMIN;

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

    protected static final String[] WHITELIST_URLS = {
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/oauth2/callback",
            "/error"
    };

    public static final String ADMIN_URLS = ADMIN + "/**";


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security,
                                           CorsFilter corsFilter,
                                           JwtAuthenticationConverter customJwtAuthenticationProvider) throws Exception {
        security
                .addFilter(corsFilter)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(WHITELIST_URLS).permitAll()
                        .requestMatchers(ADMIN_URLS).hasAuthority(UserRole.ADMIN.getRole())
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/login"))
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


    @Bean
    public CorsFilter corsFilter(CorsConfigurationProperties corsConfigurationProperties) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowCredentials(corsConfigurationProperties.getAllowCredentials());
        corsConfiguration.setAllowedOriginPatterns(corsConfigurationProperties.getAllowedOrigins());
        corsConfiguration.setAllowedMethods(corsConfigurationProperties.getAllowedMethods());
        corsConfiguration.setAllowedHeaders(corsConfigurationProperties.getAllowedHeaders());
        corsConfiguration.setExposedHeaders(corsConfigurationProperties.getExposedHeaders());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String roleUser = UserRole.USER.getRole();
        String roleAdmin = UserRole.ADMIN.getRole();
        roleHierarchy.setHierarchy(roleAdmin + " > " + roleUser);

        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (httpLogProperties.isEnabled()) {
            registry.addInterceptor(requestLoggerInterceptor);
        }
    }
}
