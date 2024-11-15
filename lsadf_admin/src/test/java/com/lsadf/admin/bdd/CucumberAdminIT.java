package com.lsadf.admin.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Cucumber tests runner
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features"},
        glue = {
                "com.lsadf.admin.bdd",
        },
        plugin = {
                "pretty",
                "html:target/cucumber-lsadf_admin.html",
                "json:target/cucumber-lsadf_admin.json"
        },
        stepNotifications = true,
        tags = "not @ignore"
)
public class CucumberAdminIT {
}
