package com.lsadf.lsadf_backend.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Cucumber tests runner
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features"},
        glue = {"com.lsadf.lsadf_backend.bdd"},
        plugin = {
                "pretty",
                "html:target/cucumber",
                "json:target/cucumber.json"
        },
        stepNotifications = true,
        tags = "not @ignore"
)
public class CucumberTests {
}
