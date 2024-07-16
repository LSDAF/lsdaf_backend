package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.properties.CorsConfigurationProperties;
import com.lsadf.lsadf_backend.properties.OAuth2Properties;
import com.lsadf.lsadf_backend.security.jwt.TokenAuthenticationFilter;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.security.oauth2.*;
import com.lsadf.lsadf_backend.services.CustomAuthenticationProvider;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserDetailsServiceImpl;
import com.lsadf.lsadf_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@Import({
        AuthProperties.class,
        OAuth2Properties.class
})
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    private static String[] WHITELIST_URLS = {
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/auth/**",
            "/oauth2/**",
            "/**"
    };

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(UserDetailsService userDetailsService) {
        return new CustomAuthenticationProvider(userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security,
                                           CorsFilter corsFilter,
                                           RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                                           OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient,
                                           OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                                           OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                                           CustomOidcUserService customOidcUserService,
                                           CustomOAuth2UserService customOAuth2UserService,
                                           TokenAuthenticationFilter tokenAuthenticationFilter) throws Exception {
        security
                .addFilter(corsFilter)
                .sessionManagement((configurer) -> {
                    configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling((configurer) -> {
                    configurer.authenticationEntryPoint(restAuthenticationEntryPoint);
                })
                .authorizeHttpRequests((configurer) -> {
                    configurer
                            .requestMatchers(WHITELIST_URLS).permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2Login((configurer) -> {
                    configurer
                            .authorizationEndpoint((authorization) -> {
                                authorization
                                        .baseUri("/oauth2/authorize");
                            })
                            .userInfoEndpoint((userInfo) -> {
                                userInfo
                                        .oidcUserService(customOidcUserService)
                                        .userService(customOAuth2UserService);
                            })
                            .successHandler(oAuth2AuthenticationSuccessHandler)
                            .tokenEndpoint((token) -> {
                                token.accessTokenResponseClient(oAuth2AccessTokenResponseClient);
                            })
                            .failureHandler(oAuth2AuthenticationFailureHandler);
                });


        return security.build();
    }

    @Bean
    public TokenAuthenticationFilter TokenAuthenticationFilter(TokenProvider tokenProvider,
                                                               UserDetailsService userDetailsService) {
        return new TokenAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    @ConfigurationProperties(prefix = "auth")
    public AuthProperties authProperties() {
        return new AuthProperties();
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

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean(CustomAuthenticationProvider authenticationProvider) throws Exception {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    @ConfigurationProperties(prefix = "cors")
    public CorsConfigurationProperties corsConfigurationProperties() {
        return new CorsConfigurationProperties();
    }

}
