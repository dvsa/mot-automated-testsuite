package uk.gov.dvsa.mot.framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * Each scenario needs to get it's own WebDriver instance.
 */
@Singleton
public class WebDriverWrapper {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebDriverWrapper.class);

    private WebDriver webDriver;

    public WebDriverWrapper() {
        logger.debug("Creating WebDriverWrapper...");
    }

    public void create() {
        logger.debug("Creating new chrome driver");

        // TODO: extract to external settings
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver"); // path to driver executable
        this.webDriver = new ChromeDriver();
    }

    public void destroy() {
        logger.debug("Destroying chrome driver");
        webDriver.quit();
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }
}
