package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.bdd.config.LsadfBackendBddTestsConfiguration;
import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.lsadf_backend.controllers.AuthController;
import com.lsadf.lsadf_backend.controllers.AuthControllerImpl;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.models.UserInfo;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.responses.GenericResponse;
import com.lsadf.lsadf_backend.services.AdminService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import io.cucumber.spring.CucumberContextConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Stack;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
        LsadfBackendConfiguration.class,
        LsadfBackendBddTestsConfiguration.class,
        AuthController.class,
        AuthControllerImpl.class,
})
@ExtendWith(MockitoExtension.class)
@EnableConfigurationProperties
@CucumberContextConfiguration
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        ReactiveOAuth2ResourceServerAutoConfiguration.class,
        ReactiveOAuth2ClientAutoConfiguration.class,
})
@ActiveProfiles("test")
public class BddLoader {

    // Repositories
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected GameSaveRepository gameSaveRepository;

    @Autowired
    protected Mapper mapper;

    // Services
    @Autowired
    protected GameSaveService gameSaveService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserDetailsService userDetailsService;

    @Autowired
    protected AdminService adminService;

    // BDD Specific Stacks

    @Autowired
    protected Stack<List<GameSave>> gameSaveListStack;

    @Autowired
    protected Stack<List<GameSaveEntity>> gameSaveEntityListStack;

    @Autowired
    protected Stack<List<User>> userListStack;

    @Autowired
    protected Stack<List<UserEntity>> userEntityListStack;

    @Autowired
    protected Stack<List<UserInfo>> userInfoListStack;

    @Autowired
    protected Stack<Exception> exceptionStack;

    @Autowired
    protected Stack<GenericResponse<?>> responseStack;

    // Controller testing properties

    @LocalServerPort
    protected int serverPort;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    static {
        log.info("Start BDD loader...");
    }
}
