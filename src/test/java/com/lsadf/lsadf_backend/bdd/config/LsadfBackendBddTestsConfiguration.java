package com.lsadf.lsadf_backend.bdd.config;

import com.lsadf.lsadf_backend.mocks.JavaMailSenderMock;
import com.lsadf.lsadf_backend.mocks.repository.CurrencyRepositoryMock;
import com.lsadf.lsadf_backend.mocks.repository.GameSaveRepositoryMock;
import com.lsadf.lsadf_backend.mocks.repository.StageRepositoryMock;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.StageRepository;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.ClockService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.*;

import static org.mockito.Mockito.mock;

/**
 * Configuration class for BDD tests
 */
@TestConfiguration
@Import(BddStackCleaner.class)
public class LsadfBackendBddTestsConfiguration {


    @Bean
    public Stack<MimeMessage> mimeMessageStack(BddStackCleaner stackCleaner) {
        Stack<MimeMessage> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Currency> currencyStack(BddStackCleaner stackCleaner) {
        Stack<Currency> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Stage> stageStack(BddStackCleaner stackCleaner) {
        Stack<Stage> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<GameSave>> gameSaveStack(BddStackCleaner stackCleaner) {
        Stack<List<GameSave>> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<User>> userListStack(BddStackCleaner stackCleaner) {
        Stack<List<User>> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<GlobalInfo> globalInfoStack(BddStackCleaner stackCleaner) {
        Stack<GlobalInfo> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<JwtAuthentication> jwtAuthenticationStack(BddStackCleaner stackCleaner) {
        Stack<JwtAuthentication> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Boolean> booleanStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<Boolean>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<UserInfo>> userInfoListStack(BddStackCleaner stackCleaner) {
        Stack<List<UserInfo>> stack = new Stack<>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<List<GameSaveEntity>> gameSaveEntityListStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<List<GameSaveEntity>>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<Exception> exceptionStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<Exception>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    public Stack<GenericResponse<?>> genericResponseStack(BddStackCleaner stackCleaner) {
        var stack = new Stack<GenericResponse<?>>();
        stackCleaner.addStack(stack);
        return stack;
    }

    @Bean
    @Primary
    public CurrencyRepository currencyRepository(BddStackCleaner stackCleaner) {
        return new CurrencyRepositoryMock();
    }

    @Bean
    @Primary
    public StageRepository stageRepository(BddStackCleaner stackCleaner) {
        return new StageRepositoryMock();
    }

    @Bean
    @Primary
    public GameSaveRepository gameSaveRepository(ClockService clockService,
                                                 CurrencyRepository currencyRepository,
                                                 StageRepository stageRepository) {
        return new GameSaveRepositoryMock(currencyRepository, stageRepository, clockService);
    }

    @Bean
    @Primary
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }


    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public JavaMailSender javaMailSenderMock(Stack<MimeMessage> mimeMessageStack) {
        return new JavaMailSenderMock(mimeMessageStack);
    }

}
