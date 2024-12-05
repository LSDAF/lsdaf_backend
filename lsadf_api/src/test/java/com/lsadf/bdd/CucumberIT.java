package com.lsadf.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/** Cucumber tests runner */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"classpath:features"},
    glue = {"com.lsadf.bdd"},
    plugin = {
      "pretty",
      "html:target/cucumber-lsadf_api.html",
      "json:target/cucumber-lsadf_api.json"
    },
    stepNotifications = true,
    tags = "not @ignore")
public class CucumberIT {}
