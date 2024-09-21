package com.lsadf.lsadf_backend.bdd.config;

import com.lsadf.lsadf_backend.bdd.config.mocks.*;
import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.RefreshTokenEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.RefreshTokenRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.security.jwt.RefreshTokenProvider;
import com.lsadf.lsadf_backend.security.jwt.TokenAuthenticationFilter;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import io.jsonwebtoken.JwtParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.lsadf.lsadf_backend.bdd.BddBeanConstants.JWT_STACK;
import static com.lsadf.lsadf_backend.bdd.BddBeanConstants.REFRESH_JWT_TOKEN_STACK;
import static com.lsadf.lsadf_backend.constants.BeanConstants.ClientRegistration.OAUTH2_FACEBOOK_CLIENT_REGISTRATION;
import static com.lsadf.lsadf_backend.constants.BeanConstants.ClientRegistration.OAUTH2_GOOGLE_CLIENT_REGISTRATION;
import static com.lsadf.lsadf_backend.constants.BeanConstants.TokenParser.JWT_TOKEN_PARSER;
import static org.mockito.Mockito.mock;

/**
 * Configuration class for BDD tests
 */
@TestConfiguration
public class LsadfBackendBddTestsConfiguration {

    @Bean
    public Stack<Currency> currencyStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<List<GameSave>> gameSaveStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<List<User>> userListStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<GlobalInfo> globalInfoStack() {
        return new Stack<>();
    }

    @Bean
    @Qualifier(JWT_STACK)
    public Stack<String> jwtStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<RefreshTokenEntity> refreshTokenEntityStack() {
        return new Stack<>();
    }

    @Bean
    @Qualifier(REFRESH_JWT_TOKEN_STACK)
    public Stack<String> refreshJwtTokenStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<Boolean> booleanStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<List<UserInfo>> userInfoListStack() {
        return new Stack<>();
    }

    @Bean
    public Map<String, LocalUser> localUserMap() {
        return new HashMap<>();
    }

    @Bean
    public Stack<List<UserEntity>> userEntityListStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<UserAdminDetails> userAdminDetailsStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<List<GameSaveEntity>> gameSaveEntityListStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<Exception> exceptionStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<GenericResponse<?>> genericResponseStack() {
        return new Stack<>();
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return new UserRepositoryMock();
    }

    @Bean
    @Primary
    public CurrencyRepository currencyRepository() {
        return new CurrencyRepositoryMock();
    }

    @Bean
    @Primary
    public RefreshTokenRepository refreshTokenRepository(UserRepository userRepository) {
        return new RefreshTokenRepositoryMock(userRepository);
    }

    @Bean
    @Primary
    public GameSaveRepository gameSaveRepository(CurrencyRepository currencyRepository) {
        return new GameSaveRepositoryMock(currencyRepository);
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    @Primary
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean
    @Qualifier(OAUTH2_GOOGLE_CLIENT_REGISTRATION)
    @Primary
    public ClientRegistration googleClientRegistration() {
        return mock(ClientRegistration.class);
    }

    @Bean
    @Qualifier(OAUTH2_FACEBOOK_CLIENT_REGISTRATION)
    @Primary
    public ClientRegistration facebookClientRegistration() {
        return mock(ClientRegistration.class);
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public TokenAuthenticationFilter tokenAuthenticationFilter(TokenProvider tokenProvider,
                                                               UserDetailsService userDetailsService,
                                                               Map<String, LocalUser> localUserMap,
                                                               Cache<LocalUser> localUserCache) {
        return new TokenAuthenticationFilterMock(tokenProvider, userDetailsService, localUserMap, localUserCache);
    }

    @Bean
    @Primary
    public RefreshTokenProvider refreshTokenProvider(UserService userService,
                                                     RefreshTokenRepository refreshTokenRepository,
                                                     @Qualifier(JWT_TOKEN_PARSER) JwtParser parser,
                                                     AuthProperties authProperties) {
        return new RefreshTokenProviderMock(userService, refreshTokenRepository, parser, authProperties);
    }
}
