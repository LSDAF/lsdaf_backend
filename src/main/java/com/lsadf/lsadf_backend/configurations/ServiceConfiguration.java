package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.cache.CacheService;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.impl.MapperImpl;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.properties.CacheProperties;
import com.lsadf.lsadf_backend.repositories.GoldRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.security.jwt.impl.TokenProviderImpl;
import com.lsadf.lsadf_backend.services.*;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.services.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public GoldService goldService(GoldRepository goldRepository,
                                   CacheService cacheService,
                                   GameSaveService gameSaveService) {
        return new GoldServiceImpl(goldRepository, cacheService, gameSaveService);
    }

    @Bean
    public GameSaveService gameSaveService(UserService userService,
                                           GameSaveRepository gameSaveRepository,
                                           Mapper mapper) {
        return new GameSaveServiceImpl(userService, gameSaveRepository, mapper);
    }

    @Bean
    public TokenProvider tokenProvider(AuthProperties authProperties) {
        return new TokenProviderImpl(authProperties);
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
                                     SearchService searchService) {
        return new AdminServiceImpl(userService, gameSaveService, mapper, searchService);
    }

}
