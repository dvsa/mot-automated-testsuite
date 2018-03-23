package uk.gov.dvsa.mot.runner;

import courgette.api.CourgetteOptions;
import courgette.api.CourgetteRunLevel;
import courgette.api.junit.Courgette;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runs cucumber features in parallel using the Courgette-JVM runner.
 */
@RunWith(Courgette.class)
@CourgetteOptions(
        threads = 5,
        runLevel = CourgetteRunLevel.FEATURE,
        rerunFailedScenarios = false,
        showTestOutput = true,
        cucumberOptions = @CucumberOptions(
            features = "src/main/resources/features",
            glue = "uk.gov.dvsa.mot",
            plugin = { "json:build/reports/selenium/selenium.json" }
        ))
public class CourgetteJUnitRunner {
}
