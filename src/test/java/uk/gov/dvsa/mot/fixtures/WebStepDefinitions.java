package uk.gov.dvsa.mot.fixtures;

import cucumber.api.java8.En;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import uk.gov.dvsa.mot.di.SpringConfiguration;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

import static junit.framework.TestCase.assertTrue;

/**
 * Step definitions for web test steps.
 */
@ContextConfiguration(classes=SpringConfiguration.class)
public class WebStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebStepDefinitions.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    @Inject
    public WebStepDefinitions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        When("^I browse to (\\S+)$", (String url) -> {
            WebDriver driver = driverWrapper.getWebDriver();
            logger.debug("Browsing to {}", url);
            driver.get(url);
            logger.debug("Got to page {} - \"{}\"", driver.getCurrentUrl(), driver.getTitle());
        });

        When("^I login as username (\\w+) and password (\\w+)$", (String username, String password) -> {
            driverWrapper.loginWith2FA(username, password);
        });

        Then("^The page title contains (.*+)$", (String title) -> {
            logger.debug("Looking for page title {}", title);
            WebDriver driver = driverWrapper.getWebDriver();
            logger.debug("Current page {} is \"{}\"", driver.getCurrentUrl(), driver.getTitle());
            assertTrue("Wrong page title", driver.getTitle().contains(title));
        });

    }
}
