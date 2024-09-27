package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.configurations.interceptors.RequestLoggerInterceptor;
import com.lsadf.lsadf_backend.constants.UserRole;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.properties.*;
import com.lsadf.lsadf_backend.security.jwt.TokenAuthenticationFilter;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.security.oauth2.*;
import com.lsadf.lsadf_backend.services.impl.CustomAuthenticationProviderImpl;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
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
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Base64;

import static com.lsadf.lsadf_backend.constants.BeanConstants.TokenParser.JWT_TOKEN_PARSER;
import static com.lsadf.lsadf_backend.constants.BeanConstants.TokenParser.JWT_REFRESH_TOKEN_PARSER;
import static com.lsadf.lsadf_backend.constants.ControllerConstants.ADMIN;

@Configuration
@Import({
        AuthProperties.class,
        OAuth2Properties.class
})
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, proxyTargetClass = true)
public class SecurityConfiguration implements WebMvcConfigurer {

    @Autowired
    private RequestLoggerInterceptor requestLoggerInterceptor;

    @Autowired
    private HttpLogProperties httpLogProperties;

    public static final String[] WHITELIST_URLS = {
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/auth/**",
            "/oauth2/**",
            "/error"
    };

    public static final String ADMIN_URLS = ADMIN + "/**";

    @Bean
    public CustomAuthenticationProviderImpl customAuthenticationProvider(UserDetailsService userDetailsService,
                                                                         ExpiringMap<String, LocalUser> localUserCache) {
        return new CustomAuthenticationProviderImpl(userDetailsService, localUserCache);
    }

    @Bean
    @Qualifier(JWT_TOKEN_PARSER)
    public JwtParser jwtParser(AuthProperties authProperties) {
        return Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encode(authProperties.getTokenSecret().getBytes()))
                .build();
    }

    @Bean
    @Qualifier(JWT_REFRESH_TOKEN_PARSER)
    public JwtParser jwtRefreshTokenParser(AuthProperties authProperties) {
        return Jwts.parserBuilder()
                .setSigningKey(Base64.getEncoder().encode(authProperties.getRefreshTokenSecret().getBytes()))
                .build();
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
                            .requestMatchers(ADMIN_URLS).hasAuthority(UserRole.ADMIN.getRole())
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

        security.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Bean
    public TokenAuthenticationFilter TokenAuthenticationFilter(TokenProvider<JwtTokenEntity> tokenProvider,
                                                               UserDetailsService userDetailsService,
                                                               com.lsadf.lsadf_backend.cache.Cache<LocalUser> localUserCache) {
        return new TokenAuthenticationFilter(tokenProvider, userDetailsService, localUserCache);
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
    public AuthenticationManager authenticationManagerBean(CustomAuthenticationProviderImpl authenticationProvider) throws Exception {
        return new ProviderManager(authenticationProvider);
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
