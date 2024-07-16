package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.bdd.config.LsadfBackendIntegrationTestsConfiguration;
import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import com.lsadf.lsadf_backend.models.GameSave;
import com.lsadf.lsadf_backend.repositories.GameSaveRepository;
import com.lsadf.lsadf_backend.repositories.UserRepository;
import com.lsadf.lsadf_backend.services.GameSaveService;
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

import java.util.Stack;

@Slf4j
@SpringBootTest(classes = {
        LsadfBackendConfiguration.class,
        LsadfBackendIntegrationTestsConfiguration.class
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

    // Services
    protected final GameSaveService gameSaveService;

    // BDD Specific Stacks
    protected final Stack<GameSave> gameSaveStack;
    protected final Stack<Exception> exceptionStack;

    @Autowired
    public BddLoader(UserRepository userRepository,
                     GameSaveRepository gameSaveRepository,
                     GameSaveService gameSaveService,
                     Stack<GameSave> gameSaveStack,
                     Stack<Exception> exceptionStack) {
        this.gameSaveRepository = gameSaveRepository;
        this.userRepository = userRepository;
        this.gameSaveService = gameSaveService;
        this.gameSaveStack = gameSaveStack;
        this.exceptionStack = exceptionStack;
    }

    static {
        log.info("Start BDD loader...");
    }
}
