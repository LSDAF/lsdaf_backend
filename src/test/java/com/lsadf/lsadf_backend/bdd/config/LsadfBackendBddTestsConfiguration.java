package com.lsadf.lsadf_backend.bdd.config;

import com.lsadf.lsadf_backend.bdd.config.mocks.JavaMailSenderMock;
import com.lsadf.lsadf_backend.bdd.config.mocks.repository.CurrencyRepositoryMock;
import com.lsadf.lsadf_backend.bdd.config.mocks.repository.GameSaveRepositoryMock;
import com.lsadf.lsadf_backend.bdd.config.mocks.repository.StageRepositoryMock;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.Currency;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.repositories.CurrencyRepository;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.StageRepository;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.ClockService;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.*;

import static com.lsadf.lsadf_backend.bdd.BddBeanConstants.JWT_STACK;
import static com.lsadf.lsadf_backend.bdd.BddBeanConstants.REFRESH_JWT_TOKEN_STACK;
import static com.lsadf.lsadf_backend.constants.BeanConstants.ClientRegistration.OAUTH2_FACEBOOK_CLIENT_REGISTRATION;
import static com.lsadf.lsadf_backend.constants.BeanConstants.ClientRegistration.OAUTH2_GOOGLE_CLIENT_REGISTRATION;
import static org.mockito.Mockito.mock;

/**
 * Configuration class for BDD tests
 */
@TestConfiguration
public class LsadfBackendBddTestsConfiguration {

    @Bean
    public Stack<MimeMessage> mimeMessageStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<Currency> currencyStack() {
        return new Stack<>();
    }

    @Bean
    public Stack<Stage> stageStack() {
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

    @Bean(name = JWT_STACK)
    public Stack<String> jwtStack() {
        return new Stack<>();
    }

    @Bean(name = REFRESH_JWT_TOKEN_STACK)
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
    public Map<String, Pair<Date, LocalUser>> localUserMap() {
        return new HashMap<>();
    }

    @Bean
    public Stack<List<UserEntity>> userEntityListStack() {
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
    public CurrencyRepository currencyRepository() {
        return new CurrencyRepositoryMock();
    }

    @Bean
    @Primary
    public StageRepository stageRepository() {
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
    public UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    @Primary
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean(name = OAUTH2_GOOGLE_CLIENT_REGISTRATION)
    @Primary
    public ClientRegistration googleClientRegistration() {
        return mock(ClientRegistration.class);
    }

    @Bean(name = OAUTH2_FACEBOOK_CLIENT_REGISTRATION)
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
    public JavaMailSender javaMailSenderMock(Stack<MimeMessage> mimeMessageStack) {
        return new JavaMailSenderMock(mimeMessageStack);
    }

}
