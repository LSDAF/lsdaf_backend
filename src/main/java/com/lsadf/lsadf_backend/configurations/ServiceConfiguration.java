package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.properties.EmailProperties;
import com.lsadf.lsadf_backend.properties.ServerProperties;
import com.lsadf.lsadf_backend.properties.UserVerificationProperties;
import com.lsadf.lsadf_backend.repositories.*;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.security.jwt.impl.RefreshTokenProviderImpl;
import com.lsadf.lsadf_backend.security.jwt.impl.JwtTokenProviderImpl;
import com.lsadf.lsadf_backend.services.*;
import com.lsadf.lsadf_backend.services.impl.*;
import io.jsonwebtoken.JwtParser;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.INVALIDATED_JWT_TOKEN_CACHE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.LOCAL_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Service.REDIS_CACHE_SERVICE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.TokenParser.JWT_REFRESH_TOKEN_PARSER;
import static com.lsadf.lsadf_backend.constants.BeanConstants.TokenParser.JWT_TOKEN_PARSER;
import static com.lsadf.lsadf_backend.constants.BeanConstants.TokenProvider.JWT_TOKEN_PROVIDER;
import static com.lsadf.lsadf_backend.constants.BeanConstants.TokenProvider.REFRESH_TOKEN_PROVIDER;


/**
 * Configuration class for the services.
 */
@Configuration
public class ServiceConfiguration {

    @Bean
    public UserService userService(final UserRepository userRepository,
                                   final PasswordEncoder passwordEncoder,
                                   final Mapper mapper) {
        return new UserServiceImpl(userRepository, passwordEncoder, mapper);
    }

    @Bean
    public CurrencyService currencyService(CurrencyRepository currencyRepository,
                                           Cache<Currency> currencyCache,
                                           GameSaveService gameSaveService,
                                           Mapper mapper) {
        return new CurrencyServiceImpl(currencyRepository, currencyCache, gameSaveService, mapper);
    }

    @Bean
    public GameSaveService gameSaveService(UserService userService,
                                           GameSaveRepository gameSaveRepository,
                                           @Qualifier(GAME_SAVE_OWNERSHIP_CACHE) Cache<String> gameSaveOwnershipCache) {
        return new GameSaveServiceImpl(userService, gameSaveRepository, gameSaveOwnershipCache);
    }

    @Bean
    @Qualifier(REFRESH_TOKEN_PROVIDER)
    public TokenProvider refreshTokenProvider(UserService userService,
                                                     RefreshTokenRepository refreshTokenRepository,
                                                     @Qualifier(JWT_REFRESH_TOKEN_PARSER) JwtParser parser,
                                                     AuthProperties authProperties,
                                                     ClockService clockService) {
        return new RefreshTokenProviderImpl(userService, refreshTokenRepository, parser, authProperties, clockService);
    }

    @Bean
    @Qualifier(JWT_TOKEN_PROVIDER)
    public TokenProvider tokenProvider(AuthProperties authProperties,
                                       @Qualifier(JWT_TOKEN_PARSER) JwtParser parser,
                                       @Qualifier(INVALIDATED_JWT_TOKEN_CACHE) Cache<String> invalidatedJwtTokenCache,
                                       ClockService clockService,
                                       JwtTokenRepository jwtTokenRepository) {
        return new JwtTokenProviderImpl(authProperties, parser, invalidatedJwtTokenCache, clockService, jwtTokenRepository);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    public SearchService searchService(UserService userService,
                                       GameSaveService gameSaveService) {
        return new SearchServiceImpl(userService, gameSaveService);
    }

    @Bean
    public Mapper mapper() {
        return new MapperImpl();
    }

    @Bean
    public AdminService adminService(UserService userService,
                                     GameSaveService gameSaveService,
                                     Mapper mapper,
                                     SearchService searchService,
                                     @Qualifier(LOCAL_CACHE_SERVICE) CacheService localCacheService,
                                     @Qualifier(REDIS_CACHE_SERVICE) CacheService redisCacheService,
                                     CacheFlushService cacheFlushService,
                                     ClockService clockService,
                                     EmailService emailService) {
        return new AdminServiceImpl(userService, gameSaveService, mapper, searchService, localCacheService, redisCacheService, cacheFlushService, clockService, emailService);
    }

    @Bean
    public ClockService clockService(Clock clock) {
        return new ClockServiceImpl(clock);
    }

    @Bean
    public EmailService emailService(VelocityEngine velocityEngine,
                                     JavaMailSender emailSender,
                                     EmailProperties mailProperties,
                                     ServerProperties serverProperties) {
        return new EmailServiceImpl(velocityEngine, emailSender, mailProperties, serverProperties);
    }

    @Bean
    public UserVerificationService userVerificationService(UserService userService,
                                                           UserVerificationTokenRepository userVerificationTokenRepository,
                                                           UserVerificationProperties userVerificationProperties,
                                                           ClockService clockService) {
        return new UserVerificationServiceImpl(userVerificationTokenRepository, userService, userVerificationProperties, clockService);
    }

}
