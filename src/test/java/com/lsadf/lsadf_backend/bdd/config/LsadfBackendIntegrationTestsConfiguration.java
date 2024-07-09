package com.lsadf.lsadf_backend.bdd.config;

import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Stack;

import static org.mockito.Mockito.mock;

@Configuration
public class LsadfBackendIntegrationTestsConfiguration {

    @Bean
    public Stack<GameSave> gameSaveStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<Exception> exceptionStack() {
        return new Stack<>();
    }

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public GameSaveRepository gameSaveRepository() {
        return mock(GameSaveRepository.class);
    }
}
