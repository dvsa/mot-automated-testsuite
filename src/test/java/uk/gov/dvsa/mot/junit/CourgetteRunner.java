package uk.gov.dvsa.mot.junit;

import courgette.api.CourgetteOptions;
import courgette.api.CourgetteRunLevel;
import courgette.api.junit.Courgette;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Junit/Courgette-JVM runner used to run the Gerkin features.
 *
 * <code>runLevel</code> of <code>FEATURE</code> results in one independent copy of the Spring Application Context
 * being created for each cucumber **feature** (so shared between scenarios within each feature).
 */
@RunWith(Courgette.class)
@CourgetteOptions(
        threads = 1,
        runLevel = CourgetteRunLevel.FEATURE,
        rerunFailedScenarios = false,
        showTestOutput = true,
        cucumberOptions = @CucumberOptions(
                features = "src/test/resources/features",
                glue = "uk.gov.dvsa.mot",
                plugin = {
                        "pretty",
                        "json:build/reports/selenium/selenium.json",
                        "html:build/reports/selenium"
                }
        ))
public class CourgetteRunner {
}
