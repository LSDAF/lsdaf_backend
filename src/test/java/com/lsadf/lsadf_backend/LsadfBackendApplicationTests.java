package com.lsadf.lsadf_backend;

import com.lsadf.lsadf_backend.bdd.config.LsadfBackendIntegrationTestsConfiguration;
import com.lsadf.lsadf_backend.configurations.LsadfBackendConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@EnableConfigurationProperties
@EnableAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        ReactiveOAuth2ResourceServerAutoConfiguration.class,
        ReactiveOAuth2ClientAutoConfiguration.class,
})
@SpringBootTest(classes = {
        LsadfBackendConfiguration.class,
        LsadfBackendIntegrationTestsConfiguration.class
})
@AutoConfigureEmbeddedDatabase(
        provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY,
        type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
class LsadfBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
