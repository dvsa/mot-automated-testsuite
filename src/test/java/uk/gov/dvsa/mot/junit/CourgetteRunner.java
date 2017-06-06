package uk.gov.dvsa.mot.junit;

import courgette.api.CourgetteOptions;
import courgette.api.CourgetteRunLevel;
import courgette.api.junit.Courgette;
import cucumber.api.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Junit/Courgette-JVM runner used to run the Gerkin features.
 */
@RunWith(Courgette.class)
@CourgetteOptions(
        threads = 10,
        runLevel = CourgetteRunLevel.SCENARIO,
        rerunFailedScenarios = false,
        showTestOutput = true,
        cucumberOptions = @CucumberOptions(
                features = "features",
                glue = "uk.gov.dvsa.mot",
                plugin = {
                        "pretty",
                        "json:build/reports/selenium/selenium.json",
                        "html:build/reports/selenium"
                }
        ))
public class CourgetteRunner {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(CourgetteRunner.class);

    @BeforeClass
    public static void setupAll() {
        logger.debug("CourgetteRunner.setupAll...");
    }

    @AfterClass
    public static void teardownAll() {
        logger.debug("CourgetteRunner.teardownAll...");
    }
}
