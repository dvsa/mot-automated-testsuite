package uk.gov.dvsa.mot.framework;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.otp.Generator;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Wraps the <code>WebDriver</code> instances needed by step definitions.
 */
public class WebDriverWrapper {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebDriverWrapper.class);

    /** The web driver to use. */
    private WebDriver webDriver;

    /** The environment configuration to use. */
    private final Environment env;

    /**
     * Creates a new instance.
     * @param env   The environment configuration to use
     */
    public WebDriverWrapper(Environment env) {
        logger.debug("Creating WebDriverWrapper...");
        this.env = env;

        logger.debug("Creating new chrome driver");

        String browser = env.getProperty("browser");
        if ("chrome".equals(browser)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            if (env.getProperty("headless").equals("true")) {
                chromeOptions.addArguments("--headless");
            }

            LoggingPreferences loggingPreferences = new LoggingPreferences();

            // logging turned off completely
            loggingPreferences.enable(LogType.BROWSER, Level.OFF);
            loggingPreferences.enable(LogType.PERFORMANCE, Level.OFF);
            loggingPreferences.enable(LogType.PROFILER, Level.OFF);
            loggingPreferences.enable(LogType.SERVER, Level.OFF);

            // logging enabled
            loggingPreferences.enable(LogType.DRIVER, Level.WARNING);
            loggingPreferences.enable(LogType.CLIENT, Level.WARNING);

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
            System.setProperty("webdriver.chrome.driver", "drivers/chromedriver"); // path to driver executable
            this.webDriver = new ChromeDriver(chromeOptions);
        } else {
            String message = "Unsupported browser: " + browser;
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        // ensure all previous sessions are invalidated
        this.webDriver.manage().deleteAllCookies();
    }

    /**
     * Resets the web driver between test scenarios.
     */
    public void reset() {
        logger.debug("WebDriverWrapper.reset");
        webDriver.manage().deleteAllCookies();
    }

    /**
     * Called when Spring is about to shutdown the current application, which is at the end of each feature.
     */
    @PreDestroy
    public void preDestroy() {
        logger.debug("WebDriverWrapper.preDestroy...");
        webDriver.quit();
    }

    /**
     * Logs a user into the application, using 2FA (password and pin).
     * @param username  The username to use
     */
    public void loginWith2FA(String username) {
        String password = env.getProperty("password");
        logger.debug("Logging in as username {} and password {}", username, password);
        String startingUrl = env.getProperty("startingUrl");
        logger.debug("Browsing to {}", startingUrl);

        webDriver.get(startingUrl + "/login");
        waitForPageLoad();
        debugCurrentPage();

        // the visible versions of the username and password fields have dynamic ids ending in _tid1 or _tid2
        WebElement usernameElement = webDriver.findElement(By.xpath("//*[contains(@id,'_tid1')]"));
        WebElement passwordElement = webDriver.findElement(By.xpath("//*[contains(@id,'_tid2')]"));

        // complete the login form
        usernameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        passwordElement.submit();
        waitForPageLoad();

        // check we got to the 2FA PIN screen
        if (!(webDriver.getTitle().contains("Your security card PIN"))) {
            // password authentication must have failed
            String message = "password authentication failed for user: " + username;
            logger.error(message);
            throw new RuntimeException(message);
        }

        // find the 2FA pin field
        WebElement pinElement = webDriver.findElement(By.id("pin"));

        // seed taken from the test OTP generator, used on all test systems
        String pin = Generator.generatePin(env.getProperty("seed"));
        logger.debug("Using PIN {}", pin);

        pinElement.sendKeys(pin);
        pinElement.submit();
        waitForPageLoad();

        // check we
        if (webDriver.getTitle().contains("Your security card PIN")) {
            // 2FA PIN authentication failed
            String message = "2FA PIN authentication failed for user: " + username;
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Browse to a page relative to the environment home.
     * @param relativePath  The relative path, must start with "/"
     */
    public void browseTo(String relativePath) {
        String url = env.getProperty("startingUrl") + relativePath;
        logger.debug("Browsing to {}", url);
        webDriver.get(url);
        waitForPageLoad();
    }

    /**
     * Press the specified button.
     * @param buttonText  The button text
     */
    public void pressButton(String buttonText) {
        WebElement button = webDriver.findElement(By.xpath("//input[@value = '" + buttonText + "']"));
        button.submit();
        waitForPageLoad();
    }

    /**
     * Clicks the specified link.
     * @param linkText  The link text
     */
    public void clickLink(String linkText) {
        WebElement link = webDriver.findElement(By.xpath("//a[contains(text(),'" + linkText + "')]"));
        link.click();
        waitForPageLoad();
    }

    /**
     * Enters the specified text into the field.
     * @param text  The text to enter
     * @param label The field label
     */
    public void enterIntoField(String text, String label) {
        // find the input associated with the specified label...
        WebElement labelElement = webDriver.findElement(By.xpath("//label[contains(text(),'" + label + "')]"));
        WebElement textElement = webDriver.findElement(By.id(labelElement.getAttribute("for")));
        textElement.sendKeys(text);
    }

    /**
     * Enters the specified text into the field.
     *
     * Note: This is a low-level way to locate the field. Please only use this method if the text <code>input</code>
     * doesn't have a corresponding label, otherwise use the <code>enterIntoField(String,String)</code> method using
     * the label text to identify the field.
     *
     * @param text  The text to enter
     * @param id    The field id
     */
    public void enterIntoFieldWithId(String text, String id) {
        WebElement textElement = webDriver.findElement(By.id(id));
        textElement.sendKeys(text);
    }

    /**
     * Selects the specified option in the (dropdown/multi-select) field.
     * @param optionText  The text of the option to select
     * @param label       The field label
     */
    public void selectOptionInField(String optionText, String label) {
        // find the input associated with the specified label...
        WebElement labelElement = webDriver.findElement(By.xpath("//label[contains(text(),'" + label + "')]"));
        Select selectElement = new Select(webDriver.findElement(By.id(labelElement.getAttribute("for"))));
        selectElement.selectByVisibleText(optionText);
    }

    /**
     * Get the title of the current page.
     * @return The title
     */
    public String getCurrentPageTitle() {
        return webDriver.getTitle();
    }

    /**
     * Takes a screenshot of the current browser window.
     * @return The screenshot (in PNG format) or <code>null</code> if failed
     */
    public byte[] takeScreenshot() {
        try {
            return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);

        } catch (WebDriverException ex) {
            logger.error("Error taking screenshot: " + ex.getMessage());
            return null;

        } catch (ClassCastException ex) {
            logger.error("WebDriver does not support taking screenshots");
            return null;
        }
    }

    /**
     * Wait for the web page to fully re-load, and any onload javascript events to complete.
     * Failure to do this between page transitions can result in intermittent failures, such as
     * Selenium still being on the previous page, or failure to find page element in the new page.
     */
    private void waitForPageLoad() {
        logger.debug("Waiting for page reload...");

        // initial wait (in milliseconds) to give the selenium web driver time to tell the web browser to
        // submit the current page
        try {
            Thread.sleep(250);

        } catch (InterruptedException ex) {
            // called if trying to shutdown the test suite
            String message = "Wait for web browser to submit current page was interrupted";
            logger.error(message, ex);

            // propagate a fatal error so testsuite shuts down
            throw new RuntimeException(message, ex);
        }

        logger.debug("Initial wait completed...");

        // maximum time (in seconds) to wait before timing out
        // as we poll much more frequently than this the actual delay should be much less
        int pageWait = Integer.parseInt(env.getProperty("pageWait"));

        // wait until page loaded, ready and JQuery processing completed...
        new WebDriverWait(webDriver, pageWait).pollingEvery(200, TimeUnit.MILLISECONDS).until(
                (ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return jQuery.active").equals(new Long(0L)));

        logger.debug("Page loaded, ready and JQuery activity complete, waiting for footer image...");

        // then for good measure wait for the footer to be available
        (new WebDriverWait(webDriver, pageWait)).pollingEvery(200, TimeUnit.MILLISECONDS)
            .until(ExpectedConditions.presenceOfElementLocated(By.id("footer")));

        logger.debug("Footer image available");
        debugCurrentPage();
    }
    /**
     * Logs the current page URL and title as debug.
     */
    private void debugCurrentPage() {
        logger.debug("** At page {} - \"{}\" **", webDriver.getCurrentUrl(), webDriver.getTitle());
    }
}
