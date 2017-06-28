package uk.gov.dvsa.mot.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.annotation.PreDestroy;

/**
 * Wraps the <code>WebDriver</code> instance needed by step definitions with a simplified generic API.
 */
public class WebDriverWrapper {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebDriverWrapper.class);

    /** The web driver to use. */
    private WebDriver webDriver;

    /** The environment configuration to use. */
    private final Environment env;

    /** The data map to use. */
    private final Map<String, String> data;

    /**
     * Creates a new instance.
     * @param env   The environment configuration to use
     */
    public WebDriverWrapper(Environment env) {
        logger.debug("Creating WebDriverWrapper...");
        this.env = env;
        this.data = new HashMap<>();

        logger.debug("Creating new chrome driver");
        String browser = env.getRequiredProperty("browser");
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

            // path to driver executable
            System.setProperty("webdriver.chrome.driver", env.getRequiredProperty("driver"));

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
        data.clear();
    }

    /**
     * Get a data item.
     * @param key   The data key
     * @return The data value
     * @throws IllegalStateException if data item not found
     */
    public String getData(String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        } else {
            String message = "Data item '" + key + "' not populated, please check your scenario logic";
            logger.error(message);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Gets all the current data keys.
     * @return A List of key names, possibly empty but never <code>null</code>
     */
    public List<String> getAllDataKeys() {
        return new ArrayList<String>(data.keySet());
    }

    /**
     * Set a data item. If an item already exists with the same key, the previous value will be over-written.
     * @param key       The data key
     * @param value     The data value
     */
    public void setData(String key, String value) {
        data.put(key, value);
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
     * Browse to a page relative to the environment home.
     * @param relativePath  The relative path, must start with "/"
     */
    public void browseTo(String relativePath) {
        String url = env.getRequiredProperty("startingUrl") + relativePath;
        logger.debug("Browsing to {}", url);
        webDriver.get(url);
        waitForPageLoad();
    }

    /**
     * Press the specified button.
     * @param buttonText  The button text
     */
    public void pressButton(String buttonText) {
        List<WebElement> buttons = findButtons(buttonText);
        if (buttons.size() == 0) {
            String message = "No buttons found with text: " + buttonText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else if (buttons.size() > 1) {
            String message = "Several buttons found with text: " + buttonText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            buttons.get(0).submit();
            waitForPageLoad();
        }
    }

    /**
     * Presses the button located by the matching sibling element (located by attribute value).
     * @param buttonText        The button text
     * @param siblingTag        The sibling element tag name
     * @param attributeName     The sibling element attribute name
     * @param attributeValue    The sibling element attribute value
     */
    public void pressButtonWithSiblingElement(String buttonText, String siblingTag, String attributeName,
                                              String attributeValue) {
        List<WebElement> buttons = findButtons(buttonText);
        if (buttons.size() == 0) {
            String message = "No buttons found with text: " + buttonText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            String xpath = "../" + siblingTag + "[@" + attributeName + " = '" + attributeValue + "']";
            for (WebElement button : buttons) {
                List<WebElement> siblings = button.findElements(By.xpath(xpath));
                if (siblings.size() > 0) {
                    logger.debug("button found!");
                    button.click();
                    waitForPageLoad();
                    return;
                }
            }
            String message = "No matching sibling elements found (xpath: " + xpath
                    + ") for buttons with text: " + buttonText;
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Finds any buttons that have the specified text. Handles buttons implemented as either "input" elements or
     * "button" elements.
     * @param buttonText    The button text
     * @return A List of zero or more Elements
     */
    private List<WebElement> findButtons(String buttonText) {
        // find any "input" elements with value matching the button text (exact match)
        List<WebElement> inputs = webDriver.findElements(By.xpath("//input[@value = '" + buttonText + "']"));

        // plus find any "button" elements with text containing the button text (can be partial match)
        inputs.addAll(webDriver.findElements(By.xpath("//button[contains(text(),'" + buttonText + "')]")));

        return inputs;
    }

    /**
     * Finds any links that have the specified text.
     * @param linkText  The link text
     * @return A List of zero or more Elements
     */
    private List<WebElement> findLinks(String linkText) {
        // find any "a" elements with text containing the link text (can be partial match)
        return webDriver.findElements(By.xpath("//a[contains(text(),'" + linkText + "')]"));
    }

    /**
     * Determines whether the current page contains the specified link.
     * @param linkText  The link text
     */
    public boolean hasLink(String linkText) {
        return findLinks(linkText).size() > 0;
    }

    /**
     * Determines whether the current page contains the specified link, found by locating the starting text then
     * following the relative XPath expression.
     * @param startTag          The tag containing the starting text
     * @param startText         The starting text
     * @param relativeXPath     The relative XPath expression
     */
    public boolean hasLink(String startTag, String startText, String relativeXPath) {
        try {
            WebElement startingTextElement = webDriver.findElement(
                    By.xpath("//" + startTag.toLowerCase() + "[contains(text(),'" + startText + "')]"));
            return startingTextElement.findElements(By.xpath(relativeXPath + ".//a")).size() > 0;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /**
     * Clicks the specified link.
     * @param linkText  The link text
     */
    public void clickLink(String linkText) {
        List<WebElement> links = findLinks(linkText);
        if (links.size() == 0) {
            String message = "No links found with text: " + linkText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else if (links.size() > 1) {
            String message = "Several links found with text: " + linkText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            links.get(0).click();
            waitForPageLoad();
        }
    }

    /**
     * Clicks the specified link found by locating the starting text then following the relative XPath expression.
     * @param startTag          The tag containing the starting text
     * @param startText         The starting text
     * @param relativeXPath     The relative XPath expression
     */
    public void clickLink(String startTag, String startText, String relativeXPath) {
        WebElement startingTextElement = webDriver.findElement(
                By.xpath("//" + startTag.toLowerCase() + "[contains(text(),'" + startText + "')]"));
        WebElement link = startingTextElement.findElement(By.xpath(relativeXPath + ".//a"));
        link.click();
        waitForPageLoad();
    }

    /**
     * Clicks the specified link found by locating the starting text then following the relative XPath expression.
     * @param startTag          The tag containing the starting text
     * @param startText         The starting text
     * @param relativeXPath     The relative XPath expression
     * @param linkText          The link text to look for below the relative expression
     */
    public void clickLink(String startTag, String startText, String relativeXPath, String linkText) {
        WebElement startingTextElement = webDriver.findElement(
                By.xpath("//" + startTag.toLowerCase() + "[contains(text(),'" + startText + "')]"));
        WebElement link = startingTextElement.findElement(
                By.xpath(relativeXPath + ".//a[contains(text(),'" + linkText + "')]"));
        link.click();
        waitForPageLoad();
    }

    /**
     * Clicks the specified element.
     * <p>Note: This is a low-level way to locate the element. Please only use this method if there is no better way to
     * locate the element, e.g. using a label.</p>
     * @param id  The element id
     */
    public void clickElement(String id) {
        WebElement element = webDriver.findElement(By.id(id));
        element.click();
    }

    /**
     * Click a radio button by the text in the input. Please only use this if there is no better way.
     * @param text  The text in the element
     */
    public void clickRadioButtonByText(String text) {
        WebElement radioButton = webDriver.findElement(By.xpath("//strong[contains(text(),'" + text + "')]"));
        radioButton.click();
    }

    /**
     * Enter text into field with a name.
     * @param name  The name for the field
     * @param text  The text to enter into the field
     */
    public void enterTextInFieldWithName(String name, String text) {
        WebElement element = webDriver.findElement(By.name(name));
        element.sendKeys(text);
    }

    /**
     * Clicks the specified element.
     * <p>Note: This is a low-level way to locate the element. Please only use this method if there is no better way to
     * locate the element, e.g. using a label.</p>
     * @param name  The element name
     */
    public void clickFirstElementByName(String name) {
        List<WebElement> elements = webDriver.findElements(By.name(name));
        if (elements == null || elements.size() == 0) {
            String message = "Unable to find any elements with name: " + name;
            logger.error(message);
            throw new IllegalStateException(message);
        }
        elements.get(0).click();
    }

    /**
     * Get the text within the specified element.
     * <p>Note: This is a low-level way to locate the element. Please only use this method if there is no better way to
     * locate the element, e.g. using a label.</p>
     * @param id  The element id
     */
    public String getElementText(String id) {
        WebElement element = webDriver.findElement(By.id(id));
        return element.getText();
    }

    /**
     * Get the text within the specified element, found by locating the starting text then following the relative XPath
     * expression.
     * @param startTag          The tag containing the starting text
     * @param startText         The starting text
     * @param relativeXPath     The relative XPath expression
     * @return The text
     */
    public String getElementText(String startTag, String startText, String relativeXPath) {
        WebElement startingElement = webDriver.findElement(
                By.xpath("//" + startTag.toLowerCase() + "[contains(text(),'" + startText + "')]"));
        WebElement relativeElement = startingElement.findElement(By.xpath(relativeXPath));
        return relativeElement.getText();
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
     * <p>Note: This is a low-level way to locate the field. Please only use this method if the text <code>input</code>
     * doesn't have a corresponding label, otherwise use the <code>enterIntoField(String,String)</code> method using
     * the label text to identify the field.</p>
     * @param text  The text to enter
     * @param id    The field id
     */
    public void enterIntoFieldWithId(String text, String id) {
        WebElement textElement = webDriver.findElement(By.id(id));
        textElement.sendKeys(text);
    }

    /**
     * Enters the specified text into the field.
     * <p>Note: This is a low-level way to locate the field. Please only use this method if the text <code>input</code>
     * doesn't have a corresponding label, otherwise use the <code>enterIntoField(String,String)</code> method using
     * the label text to identify the field.</p>
     * @param text      The text to enter
     * @param idSuffix  The field id suffix
     */
    public void enterIntoFieldWithIdSuffix(String text, String idSuffix) {
        WebElement textElement = webDriver.findElement(By.xpath("//*[contains(@id,'" + idSuffix + "')]"));
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
     * Fetchs the data in the td column in the same row as a tr element.
     * @param rowText - The text that will find the first row
     * @return          Return the text found in the table
     */
    public String getTextFromTableRow(String rowText) {
        WebElement webElement = webDriver.findElement(By.xpath("//th[contains(text(),'" + rowText + "')]/../td"));
        return webElement.getText();
    }

    /**
     * Selects a specified option in a dropdown field using the name to identify it, temporary fix for incorrect label.
     * @param optionText    The text of the option to select
     * @param name          The field name
     */
    public void selectOptionInFieldByName(String optionText, String name) {
        Select selectElement = new Select(webDriver.findElement(By.name(name)));
        selectElement.selectByVisibleText(optionText);
    }

    /**
     * Checks the title of the current page contains the specified text.
     * @param expected  The title text to look for
     * @throws WrongPageException Expected title text not found
     */
    public void checkCurrentPageTitle(String expected) {
        String actual = webDriver.getTitle();
        logger.debug("Checking current page title for '{}' contains '{}'", actual, expected);
        if (!actual.contains(expected)) {
            String message = "Wrong page title, on wrong page perhaps? "
                    + "Expected the title to contain " + expected + ", but the title was " + actual;
            logger.error(message);
            throw new WrongPageException(expected, actual);
        }
    }

    /**
     * Get the current page title. Use for tests that need to react to where the user is taken to, to check whether the
     * page is as expected use <code>checkCurrentPageTitle</code>
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
            logger.debug("Taking screenshot of current window as PNG");
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
        int pageWait = Integer.parseInt(env.getRequiredProperty("pageWait"));

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
