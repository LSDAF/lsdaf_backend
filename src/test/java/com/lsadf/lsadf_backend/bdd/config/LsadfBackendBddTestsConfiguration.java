package com.lsadf.lsadf_backend.bdd.config;

import com.lsadf.lsadf_backend.bdd.config.mocks.impl.TokenAuthenticationFilterMock;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.LocalUser;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.GoldRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.security.jwt.TokenAuthenticationFilter;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.UserDetailsService;
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

import static org.mockito.Mockito.mock;

@TestConfiguration
public class LsadfBackendBddTestsConfiguration {

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
    @Qualifier("jwtStack")
    public Stack<String> jwtStack() {
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
        return mock(UserRepository.class);
    }

    @Bean
    @Primary
    public GoldRepository goldRepository() {
        return mock(GoldRepository.class);
    }

    @Bean
    @Primary
    public GameSaveRepository gameSaveRepository() {
        return mock(GameSaveRepository.class);
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
    @Qualifier("oAuth2GoogleClientRegistration")
    @Primary
    public ClientRegistration googleClientRegistration() {
        return mock(ClientRegistration.class);
    }

    @Bean
    @Qualifier("oAuth2FacebookClientRegistration")
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
                                                               Map<String, LocalUser> localUserMap) {
        return new TokenAuthenticationFilterMock(tokenProvider, userDetailsService, localUserMap);
    }
}
