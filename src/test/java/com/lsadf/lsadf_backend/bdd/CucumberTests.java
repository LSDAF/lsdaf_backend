package com.lsadf.lsadf_backend.bdd;

import com.lsadf.lsadf_backend.controllers.AdminController;
import com.lsadf.lsadf_backend.controllers.AuthController;
import com.lsadf.lsadf_backend.controllers.GameSaveController;
import com.lsadf.lsadf_backend.controllers.UserController;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = {AdminController.class, UserController.class, GameSaveController.class, AuthController.class})
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
