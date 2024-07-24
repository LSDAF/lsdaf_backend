package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.bdd.config.LsadfBackendIntegrationTestsConfiguration;
import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.lsadf_backend.entities.GameSaveEntity;
import com.lsadf.lsadf_backend.entities.UserEntity;
import com.lsadf.lsadf_backend.mappers.Mapper;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.models.User;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.AdminService;
import com.lsadf.lsadf_backend.services.GameSaveService;
import com.lsadf.lsadf_backend.services.UserDetailsService;
import com.lsadf.lsadf_backend.services.UserService;
import io.cucumber.spring.CucumberContextConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Stack;

@Slf4j
@SpringBootTest(classes = {
        LsadfBackendConfiguration.class,
        LsadfBackendIntegrationTestsConfiguration.class,
})
@AutoConfigureEmbeddedDatabase(
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
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
    protected final UserRepository userRepository;
    protected final GameSaveRepository gameSaveRepository;

    protected final Mapper mapper;

    // Services
    protected final GameSaveService gameSaveService;
    protected final UserService userService;
    protected final UserDetailsService userDetailsService;
    protected final AdminService adminService;

    // BDD Specific Stacks
    protected final Stack<List<GameSave>> gameSaveListStack;
    protected final Stack<List<GameSaveEntity>> gameSaveEntityListStack;
    protected final Stack<List<User>> userListStack;
    protected final Stack<List<UserEntity>> userEntityListStack;
    protected final Stack<Exception> exceptionStack;

    @Autowired
    public BddLoader(UserRepository userRepository,
                     GameSaveRepository gameSaveRepository,
                     GameSaveService gameSaveService,
                     Stack<List<GameSave>> gameSaveListStack,
                     Stack<List<User>> userListStack,
                     Stack<List<UserEntity>> userEntityListStack,
                     Stack<List<GameSaveEntity>> gameSaveEntityListStack,
                     Stack<Exception> exceptionStack,
                     UserService userService,
                     UserDetailsService userDetailsService,
                     AdminService adminService,
                     Mapper mapper) {
        this.gameSaveRepository = gameSaveRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.userListStack = userListStack;
        this.gameSaveEntityListStack = gameSaveEntityListStack;
        this.userEntityListStack = userEntityListStack;
        this.gameSaveService = gameSaveService;
        this.gameSaveListStack = gameSaveListStack;
        this.exceptionStack = exceptionStack;
        this.userService = userService;
        this.adminService = adminService;
        this.userDetailsService = userDetailsService;
    }

    static {
        log.info("Start BDD loader...");
    }
}
