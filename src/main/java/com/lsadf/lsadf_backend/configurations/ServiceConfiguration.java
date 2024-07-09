package com.lsadf.lsadf_backend.configurations;

import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.GameSaveServiceImpl;
import com.lsadf.lsadf_backend.services.UserService;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.services.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

    @Bean
    public UserService userService(final UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public GameSaveService gameSaveService(UserRepository userRepository,
                                           GameSaveRepository gameSaveRepository) {
        return new GameSaveServiceImpl(userRepository, gameSaveRepository);
    }
}
