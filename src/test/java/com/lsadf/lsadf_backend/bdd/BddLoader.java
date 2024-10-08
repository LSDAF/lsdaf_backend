package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.bdd.config.LsadfBackendBddTestsConfiguration;
import com.lsadf.lsadf_backend.cache.Cache;
import com.lsadf.lsadf_backend.cache.HistoCache;
import com.lsadf.lsadf_backend.entities.tokens.JwtTokenEntity;
import com.lsadf.lsadf_backend.entities.tokens.RefreshTokenEntity;
import com.lsadf.lsadf_backend.repositories.*;
import com.lsadf.lsadf_backend.services.CacheFlushService;
import com.lsadf.lsadf_backend.services.CacheService;
import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.lsadf_backend.controllers.*;
import com.lsadf.lsadf_backend.controllers.impl.*;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.*;
import com.lsadf.lsadf_backend.models.admin.GlobalInfo;
import com.lsadf.lsadf_backend.models.admin.UserAdminDetails;
import com.lsadf.lsadf_backend.properties.CacheExpirationProperties;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.security.jwt.TokenProvider;
import com.lsadf.lsadf_backend.services.*;
import io.cucumber.spring.CucumberContextConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.lsadf.lsadf_backend.bdd.BddBeanConstants.JWT_STACK;
import static com.lsadf.lsadf_backend.bdd.BddBeanConstants.REFRESH_JWT_TOKEN_STACK;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.GAME_SAVE_OWNERSHIP_CACHE;
import static com.lsadf.lsadf_backend.constants.BeanConstants.Cache.INVALIDATED_JWT_TOKEN_CACHE;

/**
 * BDD Loader class for the Cucumber tests
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
        LsadfBackendConfiguration.class,
        LsadfBackendBddTestsConfiguration.class,

        // Precise both the interface and the implementation to avoid ambiguity & errors for testing
        AuthController.class,
        AuthControllerImpl.class,
        GameSaveController.class,
        GameSaveControllerImpl.class,
        UserController.class,
        UserControllerImpl.class,
        AdminController.class,
        AdminControllerImpl.class,
        CurrencyController.class,
        CurrencyControllerImpl.class,
})
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
@CucumberContextConfiguration
// Autoconfigure database for overall config of the app.
@AutoConfigureEmbeddedDatabase(
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
//@ImportAutoConfiguration(
//        AopAutoConfiguration.class
//)
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        ReactiveOAuth2ResourceServerAutoConfiguration.class,
        ReactiveOAuth2ClientAutoConfiguration.class,
})
@ActiveProfiles("test")
public class BddLoader {

    // Caches
    @Autowired
    protected Cache<LocalUser> localUserCache;

    @Autowired
    @Qualifier(INVALIDATED_JWT_TOKEN_CACHE)
    protected Cache<String> invalidatedJwtTokenCache;

    @Autowired
    @Qualifier(GAME_SAVE_OWNERSHIP_CACHE)
    protected Cache<String> gameSaveOwnershipCache;

    @Autowired
    protected HistoCache<Currency> currencyCache;

    @Autowired
    protected HistoCache<Stage> stageCache;

    // Repositories
    @Autowired
    protected CurrencyRepository currencyRepository;

    @Autowired
    protected UserVerificationTokenRepository userVerificationTokenRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected GameSaveRepository gameSaveRepository;

    @Autowired
    protected Mapper mapper;

    @Autowired
    protected TokenProvider<JwtTokenEntity> tokenProvider;

    @Autowired
    protected TokenProvider<RefreshTokenEntity> refreshTokenProvider;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    // Services

    @Autowired
    protected ClockService clockService;

    @Autowired
    protected CurrencyService currencyService;

    @Autowired
    protected CacheService redisCacheService;

    @Autowired
    protected CacheService localCacheService;

    @Autowired
    protected CacheFlushService cacheFlushService;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected UserVerificationService userVerificationService;

    @Autowired
    protected GameSaveService gameSaveService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserDetailsService lsadfUserDetailsService;

    @Autowired
    protected AdminService adminService;

    // BDD Specific Stacks & Maps

    @Autowired
    protected Map<String, Pair<Date, LocalUser>> localUserMap;

    @Autowired
    protected Stack<RefreshTokenEntity> refreshTokenEntityStack;

    @Autowired
    protected Stack<List<GameSave>> gameSaveListStack;

    @Autowired
    protected Stack<List<GameSaveEntity>> gameSaveEntityListStack;

    @Autowired
    protected Stack<List<User>> userListStack;

    @Autowired
    protected Stack<GlobalInfo> globalInfoStack;

    @Autowired
    protected Stack<List<UserEntity>> userEntityListStack;

    @Autowired
    protected Stack<UserAdminDetails> userAdminDetailsStack;

    @Autowired
    protected Stack<MimeMessage> mimeMessageStack;

    @Autowired
    protected Stack<Currency> currencyStack;

    @Autowired
    protected Stack<List<UserInfo>> userInfoListStack;

    @Autowired
    protected Stack<Exception> exceptionStack;

    @Autowired
    protected Stack<GenericResponse<?>> responseStack;

    @Autowired
    protected Stack<Boolean> booleanStack;

    @Autowired
    @Qualifier(JWT_STACK)
    protected Stack<String> jwtTokenStack;

    @Autowired
    @Qualifier(REFRESH_JWT_TOKEN_STACK)
    protected Stack<String> refreshJwtTokenStack;

    // Properties
    @Autowired
    protected CacheExpirationProperties cacheExpirationProperties;

    // Controller testing properties

    @LocalServerPort
    protected int serverPort;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    static {
        log.info("Start BDD loader...");
    }
}
