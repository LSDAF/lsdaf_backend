package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.mappers.MapperImpl;
import com.lsadf.lsadf_backend.properties.AuthProperties;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.security.jwt.TokenProviderImpl;
import com.lsadf.lsadf_backend.services.*;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public GameSaveService gameSaveService(UserRepository userRepository,
                                           GameSaveRepository gameSaveRepository,
                                           Mapper mapper) {
        return new GameSaveServiceImpl(userRepository, gameSaveRepository, mapper);
    }

    @Bean
    public TokenProvider tokenProvider(AuthProperties authProperties,
                                       UserDetailsService userDetailsService,
                                       UserService userService) {
        return new TokenProviderImpl(authProperties, userService, userDetailsService);
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new UserDetailsServiceImpl(userService);
    }

    @Bean
    public Mapper mapper() {
        return new MapperImpl();
    }
}
