package uk.gov.dvsa.mot.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.otp.Generator;

/**
 * Wraps the <code>WebDriver</code> instances needed by step definitions.
 */
public class WebDriverWrapper {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebDriverWrapper.class);

    private WebDriver webDriver;

    /** The environment configuration to use. */
    private final Environment env;

    /**
     * Creates a new instance.
     * @param env   The environment configuration to use
     */
    public WebDriverWrapper(Environment env) {
        this.env = env;
        logger.debug("Creating WebDriverWrapper...");
    }

    /**
     * Creates a new <code>WebDriver</code>, ready to start a test scenario.
     */
    public void create() {
        logger.debug("Creating new chrome driver");

        String browser = env.getProperty("browser");
        if ("chrome".equals(browser)) {
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver"); // path to driver executable
            this.webDriver = new ChromeDriver();
        } else {
            String message = "Unsupported browser: " + browser;
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Destroys a <code>WebDriver</code> at the end of a test scenario.
     */
    public void destroy() {
        logger.debug("Destroying chrome driver");
        webDriver.quit();
    }

    /**
     * Get the <code>WebDriver</code>.
     * @return The driver
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Logs a user into the application, using 2FA (password and pin).
     * @param username  The username to use
     * @param password  The password to use
     */
    public void loginWith2FA(String username, String password) {
        logger.debug("Logging in as username {} and password {}", username, password);
        String startingUrl = env.getProperty("startingUrl");
        logger.debug("Browsing to {}", startingUrl);

        webDriver.get(startingUrl + "/login");
        logger.debug("Got to page {} - \"{}\"", webDriver.getCurrentUrl(), webDriver.getTitle());

        // hidden duplicate of username field is ID "IDToken1"
        (new WebDriverWait(webDriver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("IDToken1")));
        WebElement usernameElement = webDriver.findElement(By.xpath("//*[contains(@id,'_tid1')]"));
        WebElement passwordElement = webDriver.findElement(By.xpath("//*[contains(@id,'_tid2')]"));

        // complete the login form
        usernameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        passwordElement.submit();

        logger.debug("Got to page {} - \"{}\"", webDriver.getCurrentUrl(), webDriver.getTitle());

        // find the 2FA pin field
        WebElement pinElement = webDriver.findElement(By.id("pin"));

        // seed taken from the test OTP generator, used on all test systems
        String pin = Generator.generatePin(env.getProperty("seed"));
        logger.debug("Using PIN {}", pin);

        pinElement.sendKeys(pin);
        pinElement.submit();

        logger.debug("Got to page {} - \"{}\"", webDriver.getCurrentUrl(), webDriver.getTitle());
    }
}
