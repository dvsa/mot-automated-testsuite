package uk.gov.dvsa.mot.fixtures;

import cucumber.api.java8.En;
import org.apache.commons.configuration2.Configuration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.otp.Generator;

import javax.inject.Inject;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * Step definitions for web test steps.
 */
public class WebStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebStepDefinitions.class);

    @Inject
    private WebDriverWrapper driverWrapper;

    @Inject
    private Configuration configuration;

    public WebStepDefinitions() {
        When("^I browse to (\\S+)$", (String url) -> {
            WebDriver driver = driverWrapper.getWebDriver();
            logger.debug("Browsing to {}", url);
            driver.get(url);
            logger.debug("Got to page {} - \"{}\"", driver.getCurrentUrl(), driver.getTitle());
        });

        When("^I login as username (\\w+) and password (\\w+)$", (String username, String password) -> {
            logger.debug("Logging in as username {} and password {}", username, password);
            String startingUrl = configuration.getString("startingUrl");
            logger.debug("Browsing to {}", startingUrl);

            WebDriver driver = driverWrapper.getWebDriver();
            driver.get(startingUrl + "/login");
            logger.debug("Got to page {} - \"{}\"", driver.getCurrentUrl(), driver.getTitle());

            // hidden duplicate of username field is ID "IDToken1"
            (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("IDToken1")));
            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            WebElement usernameElement = driver.findElement(By.xpath("//*[contains(@id,'_tid1')]"));
            WebElement passwordElement = driver.findElement(By.xpath("//*[contains(@id,'_tid2')]"));

            // complete the login form
            usernameElement.sendKeys(username);
            passwordElement.sendKeys(password);
            passwordElement.submit();

            logger.debug("Got to page {} - \"{}\"", driver.getCurrentUrl(), driver.getTitle());

            // find the 2FA pin field
            WebElement pinElement = driver.findElement(By.id("pin"));

            // seed taken from the test OTP generator, used on all test systems
            String pin = Generator.generatePin("9b0bf51be1fdd18842209e430092d643b28768b7");
            logger.debug("Using PIN {}", pin);

            pinElement.sendKeys(pin);
            pinElement.submit();

            logger.debug("Got to page {} - \"{}\"", driver.getCurrentUrl(), driver.getTitle());
        });

        Then("^The page title contains (.*+)$", (String title) -> {
            logger.debug("Looking for page title {}", title);
            WebDriver driver = driverWrapper.getWebDriver();
            logger.debug("Current page {} is \"{}\"", driver.getCurrentUrl(), driver.getTitle());
            assertTrue("Wrong page title", driver.getTitle().contains(title));
        });

    }
}
