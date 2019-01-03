package uk.gov.dvsa.mot.framework;

import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.csv.CsvDocument;
import uk.gov.dvsa.mot.framework.csv.CsvException;
import uk.gov.dvsa.mot.framework.pdf.PdfDocument;
import uk.gov.dvsa.mot.framework.pdf.PdfException;

import java.io.Console;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PreDestroy;

/**
 * Wraps the <code>WebDriver</code> instance needed by step definitions with a simplified generic API.
 */
public class WebDriverWrapper {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebDriverWrapper.class);

    /** The web driver to use. */
    private final WebDriver webDriver;

    /** The environment configuration to use. */
    private final Environment env;

    /** The amount of time to wait (in milliseconds) for browser clicks before page refresh. */
    private final long clickWaitMilliseconds;

    /** The maximum time (in seconds) to wait for page refresh/events before timing out. */
    private final long pageWaitSeconds;

    /** The frequency (in milliseconds) to poll for page refresh/events. */
    private final long pollFrequencyMilliseconds;

    /** The data map to use. */
    private final Map<String, String> data;

    /** Request handler to process HTTP requests. **/
    private final RequestHandler requestHandler;

    /** Starting URL. */
    private String startingUrl;

    /**
     * Creates a new instance.
     * @param env   The environment configuration to use
     */
    public WebDriverWrapper(Environment env) {
        logger.debug("Creating WebDriverWrapper...");
        this.env = env;
        this.data = new HashMap<>();
        this.webDriver = createWebDriver();
        String browserWidth = env.getProperty("browserWidth", "1280");
        String browserHeight = env.getProperty("browserHeight", "1960");
        this.webDriver.manage().window().setSize(
                new Dimension(Integer.parseInt(browserWidth), Integer.parseInt(browserHeight)));
        this.requestHandler = new RequestHandler(this.webDriver, env);

        // amount of time (in milliseconds) to wait for browser clicks to happen, before page refresh logic
        // this is a mandatory delay, to accommodate any browser/environment/network latency
        this.clickWaitMilliseconds = Long.parseLong(env.getRequiredProperty("clickWait"));

        // maximum time (in seconds) to wait before timing out
        // as we poll much more frequently than this (see below) the actual delay should be much less
        this.pageWaitSeconds = Long.parseLong(env.getRequiredProperty("pageWait"));

        // poll frequency (in milliseconds) for page refresh/events
        this.pollFrequencyMilliseconds = 200;

        // ensure all previous sessions are invalidated
        this.webDriver.manage().deleteAllCookies();

        // ensure the starting url is set
        this.startingUrl = env.getRequiredProperty("startingUrl");
    }

    /**
     * Creates the web driver being wrapped.
     * <p>This can be overridden by subclasses wanting to use alternative test web drivers.</p>
     * @return The web driver instance
     */
    protected WebDriver createWebDriver() {
        logger.debug("Creating new chrome driver");
        String browser = env.getRequiredProperty("browser");
        if ("chrome".equals(browser)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            if ("true".equals(env.getProperty("acceptInsecureCerts"))) {
                chromeOptions.setAcceptInsecureCerts(true);
            }
            if ("true".equals(env.getProperty("headless"))) {
                chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("window-size=1920,1080");
            }

            if ("true".equals(env.getProperty("securityProxy"))) {
                Proxy proxy = new Proxy();
                proxy.setSslProxy(env.getRequiredProperty("proxyServer"))
                        .setHttpProxy(env.getRequiredProperty("proxyServer"));
                chromeOptions.setProxy(proxy);
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

            //If gridURL is set create a remote webdriver instance
            if (env.containsProperty("gridURL")) {
                try {
                    return new RemoteWebDriver(new URL(env.getProperty("gridURL")), capabilities);

                } catch (MalformedURLException malformedUrlException) {
                    String message = "Error while creating remote driver: " + malformedUrlException.getMessage();
                    logger.error(message);
                    throw new IllegalArgumentException(message);
                }
            } else {
                return new ChromeDriver(chromeOptions);
            }
        } else {
            String message = "Unsupported browser: " + browser;
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
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
        List<String> keys = new ArrayList<>(data.keySet());
        keys.sort(Comparator.naturalOrder());
        return keys;
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
        // to handle retrying logins, clear the browser cookies between retries
        webDriver.manage().deleteAllCookies();

        // browse to the specified web page
        String url = startingUrl + relativePath;
        logger.debug("Browsing to {}", url);
        webDriver.get(url);
        waitForFullPageLoad();
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
            waitForFullPageLoad();
        }
    }

    /**
     * Clicking a button specified by its class name.
     */
    public void clickButtonByClassName(String className) {
        List<WebElement> buttons = webDriver.findElements(By.xpath("//button[@class='" + className + "']"));
        if (buttons.size() == 0) {
            String message = "No buttons found with class name: " + className;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else if (buttons.size() > 1) {
            String message = "Several buttons found with class name: " + className;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            buttons.get(0).click();
        }
    }

    /**
     * Click a button rather than submit it.
     * @param buttonText    The text on the button to be clicked
     */
    public void clickButton(String buttonText) {
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
            buttons.get(0).click();
            waitForFullPageLoad();
        }
    }

    /**
     * Determine whether the specified button is disabled.
     * @param buttonText  The button text
     * @return <code>true</code> if disabled
     */
    public boolean isButtonDisabled(String buttonText) {
        List<WebElement> buttons = findButtons(buttonText);
        if (buttons.size() == 0) {
            String message = "No buttons found with text: " + buttons;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else if (buttons.size() > 1) {
            String message = "Several buttons found with text: " + buttons;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            return buttons.get(0).getAttribute("disabled") != null;
        }
    }

    /**
     * Press the first of the specified buttons.
     * @param buttonText  The button text
     */
    public void pressFirstButton(String buttonText) {
        List<WebElement> buttons = findButtons(buttonText);
        if (buttons.size() == 0) {
            String message = "No buttons found with text: " + buttonText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            buttons.get(0).submit();
            waitForFullPageLoad();
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
                    waitForFullPageLoad();
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
     * Presses the button located by the matching sibling element by its text.
     * Finds the sibling by text then traverses back up the tree to find the parent button of the sibling.
     * @param siblingText        The text for the sibling of the button
     */
    public void clickButtonWithSiblingText(String siblingText) {
        String xpath = "//*[text() = '" + siblingText + "']/ancestor::button";
        List<WebElement> buttons = webDriver.findElements(By.xpath(xpath));
        if (buttons.size() == 0) {
            String message = "No buttons found for sibling text: " + siblingText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else if (buttons.size() > 1) {
            String message = "Several buttons found for sibling text: " + siblingText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            buttons.get(0).click();
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
     * Finds any links that have the specified text, which may contain single quotes, and <code>{key}</code> format
     * data keys, but not double quotes. Also matches links containing sub-elements (e.g. formatting the text).
     * @param linkText  The link text
     * @return A List of zero or more Elements
     */
    private List<WebElement> findLinks(String linkText) {
        // find any "a" elements with text containing the link text
        List<WebElement> links = webDriver.findElements(
                By.xpath("//a[contains(normalize-space(),\"" + expandDataKeys(linkText) + "\")]"));

        // then add any "a" elements with any sub-elements (e.g. nested "span"s) with text containing the link text
        links.addAll(webDriver.findElements(
                By.xpath("//a[.//*[contains(normalize-space(),\"" + expandDataKeys(linkText) + "\")]]")));

        return links;
    }

    /**
     * Finds any span elements that contain the specified text.
     * @param text  The text
     * @return A List of zero or more Elements
     */
    List<WebElement> findSpans(String text) {
        // find any "span" elements with text containing the given text (can be partial match)
        return webDriver.findElements(By.xpath("//span[contains(text(),\"" + text + "\")]"));
    }

    /**
     * Clicks the first link that matches the text, which may contain single quotes, and <code>{key}</code> format
     * data keys, but not double quotes.
     * @param linkText  The link text used to find the link
     */
    public void clickFirstLink(String linkText) {
        List<WebElement> links = findLinks(linkText);
        if (links.size() == 0) {
            String message = "No links found with text: " + linkText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            clickAndWaitForPageLoad(links.get(0));
        }
    }

    /**
     * Clicks the last link that matches the text, which may contain single quotes, and <code>{key}</code> format
     * data keys, but not double quotes.
     * @param linkText  the link text used to find the link
     */
    public void clickLastLink(String linkText) {
        List<WebElement> links = findLinks(linkText);
        if (links.size() == 0) {
            String message = "No links found with text: " + linkText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            clickAndWaitForPageLoad(links.get(links.size() - 1));
        }
    }

    /**
     * Get the class(es) for the specified link.
     * @param linkText  The link text
     * @return The class(es)
     */
    public String getLinkClass(String linkText) {
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
            return links.get(0).getAttribute("class");
        }
    }

    /**
     * Determines whether the current page contains the specified link, which may contain single quotes, and
     * <code>{key}</code> format data keys, but not double quotes.
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
     * @param relativeXPath     The relative XPath expression, must end in "/"
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
     * Clicks the specified link, which may contain single quotes, and <code>{key}</code> format data keys, but not
     * double quotes.
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
            clickAndWaitForPageLoad(links.get(0));
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
        clickAndWaitForPageLoad(link);
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
                By.xpath("//" + startTag + "[contains(text(),'" + startText + "')]"));
        WebElement link = startingTextElement.findElement(
                By.xpath(relativeXPath + ".//a[contains(text(),'" + linkText + "')]"));
        clickAndWaitForPageLoad(link);
    }

    /**
     * Clicks the span element that contains the given text.
     * @param text Span element text
     */
    public void clickText(String text) {
        List<WebElement> spans = findSpans(text);
        if (spans.size() == 0) {
            String message = "No span elements found with text: " + text;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else if (spans.size() > 1) {
            String message = "Several links found with text: " + text;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            clickAndWaitForPageLoad(spans.get(0));
        }
    }

    /**
     * Clicks the specified element.
     * <p>Note: This is a low-level way to locate the element. Please only use this method if there is no better way to
     * locate the element, e.g. using a label.</p>
     * @param id  The element id
     */
    public void clickElement(String id) {
        WebElement element = webDriver.findElement(By.id(id));
        clickAndWaitForPageLoad(element);
    }

    /**
     * Clicks the specified FontAwesome icon. Expects a <code>i</code> element with a class of
     * <code>fa-..icon name..</code> (may also have other CSS classes set).
     * @param iconName      The FontAwesome icon css class name, without the <code>fa-</code> prefix
     */
    public void clickIcon(String iconName) {
        WebElement element = webDriver.findElement(By.xpath("//i[contains(@class, '" + iconName + "')]"));
        clickAndWaitForPageLoad(element);
    }

    /**
     * Clicks the accepts button (often labelled "OK") in a javascript alert popup dialog box.
     */
    public void acceptAlert() {
        getAlert().accept();
    }

    /**
     * Clicks the dismiss button (often labelled "Cancel") in a javascript alert popup dialog box.
     */
    public void dismissAlert() {
        getAlert().dismiss();
    }

    /**
     * Get the text of the current javascript alert popup dialog box.
     * @return The text
     */
    public String getAlertText() {
        return getAlert().getText();
    }

    /**
     * Get the current javascript alert popup dialog box.
     * @return The alert
     */
    private Alert getAlert() {
        logger.debug("Waiting for alert to popup...");
        (new WebDriverWait(webDriver, pageWaitSeconds)).pollingEvery(pollFrequencyMilliseconds, TimeUnit.MILLISECONDS)
                .until(ExpectedConditions.alertIsPresent());
        logger.debug("Alert has popped up...");

        return webDriver.switchTo().alert();
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
        element.clear();
        element.sendKeys(text);
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
     * @param value The value to enter
     * @param label The field label
     */
    public void enterIntoField(int value, String label) {
        enterIntoField(String.valueOf(value), label);
    }

    /**
     * Enters the specified text into the field.
     *
     * @param text  The text to enter
     * @param label The field label
     */
    public void enterIntoField(String text, String label) {
        // find the input associated with the specified label...
        WebElement labelElement = webDriver.findElement(By.xpath("//label[contains(text(),'" + label + "')]"));
        WebElement textElement = webDriver.findElement(By.id(labelElement.getAttribute("for")));
        textElement.clear();
        textElement.sendKeys(text);
    }

    /**
     * Enters the specified text into the field, within the specified fieldset.
     * @param text          The text to enter
     * @param fieldLabel    The field label
     * @param fieldsetLabel The fieldset label
     */
    public void enterIntoFieldInFieldset(String text, String fieldLabel, String fieldsetLabel) {
        WebElement fieldsetElement;

        try {
            // find the fieldset with the fieldset label
            fieldsetElement = webDriver.findElement(
                    By.xpath("//label[contains(text(),'" + fieldsetLabel + "')]/ancestor::fieldset[1]"));

        } catch (NoSuchElementException noSuchElement) {
            fieldsetElement = findFieldsetByLegend(fieldsetLabel);
        }

        // find the specified label (with the for="id" attribute)...
        WebElement labelElement = fieldsetElement.findElement(
                By.xpath(".//label[contains(text(),'" + fieldLabel + "')]"));

        // find the text element with id matching the for attribute
        // (search in the fieldset rather than the whole page, to get around faulty HTML where id's aren't unique!)
        WebElement textElement = fieldsetElement.findElement(By.id(labelElement.getAttribute("for")));
        textElement.clear();
        textElement.sendKeys(text);
    }

    /**
     * Enters the specified text into the field.
     * <p>Note: This is a low-level way to locate the field. Please only use this method if the text <code>input</code>
     * doesn't have a corresponding label, otherwise use the <code>enterIntoField(String,String)</code> method using
     * the label text to identify the field.</p>
     * @param value The value to enter
     * @param id    The field id
     */
    public void enterIntoFieldWithId(int value, String id) {
        enterIntoFieldWithId(String.valueOf(value), id);
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
        textElement.clear();
        textElement.sendKeys(text);
    }

    /**
     * Selects the specified radio button. Supports well-formed labels and radio buttons nested inside the label.
     * @param labelText  The radio button label
     */
    public void selectRadio(String labelText) {
        try {
            // find the input associated with the specified (well-formed) label...
            WebElement labelElement = webDriver.findElement(By.xpath("//label[contains(text(),'" + labelText + "')]"));
            webDriver.findElement(By.id(labelElement.getAttribute("for"))).click();

        } catch (NoSuchElementException | IllegalArgumentException ex) {
            webDriver.findElements(By.tagName("label")).stream()
                .filter((l) -> l.getText().contains(labelText)) // label with text
                .map((l) -> l.findElement(By.xpath("./input[@type = 'radio']"))) // nested radio
                .findFirst().orElseThrow(() -> {
                    String message = "No radio button found with label (well-formed or nested): " + labelText;
                    logger.error(message);
                    return new IllegalArgumentException(message);
                }).click();
        }
    }

    /**
     * Selects the specified radio button, within the specified fieldset. Supports well-formed labels and radio buttons
     * nested inside the label.
     * @param fieldset   The fieldset legend
     * @param labelText  The radio button label
     */
    public void selectRadioInFieldset(String fieldset, String labelText) {
        try {
            WebElement fieldsetElement = findFieldsetByLegend(fieldset);

            // find the input associated with the specified (well-formed) label...
            WebElement labelElement = fieldsetElement.findElement(
                    By.xpath(".//label[contains(text(),'" + labelText + "')]"));
            webDriver.findElement(By.id(labelElement.getAttribute("for"))).click();

        } catch (NoSuchElementException | IllegalArgumentException ex) {
            WebElement fieldsetElement = null;
            try {
                fieldsetElement = findFieldsetByLegend(fieldset);

            } catch (NoSuchElementException ex2) {
                String message = "Fieldset " + fieldset + " not found";
                logger.error(message);
                throw new IllegalArgumentException(message);
            }

            fieldsetElement.findElements(By.tagName("label")).stream()
                .filter((l) -> l.getText().contains(labelText)) // label with text
                .map((l) -> l.findElement(By.xpath("./input[@type = 'radio']"))) // nested radio
                .findFirst().orElseThrow(() -> {
                    String message = "No radio button found in fieldset " + fieldset
                            + "with label (well-formed or nested): " + labelText;
                    logger.error(message);
                    return new IllegalArgumentException(message);
                }).click();
        }
    }

    /**
     * Selects the specified radio button, within the specified nested fieldset. Supports well-formed labels and radio
     * buttons nested inside the label.
     * @param outerFieldset     The outer fieldset legend
     * @param nestedFieldset    The nested fieldset legend
     * @param labelText         The radio button label
     */
    public void selectRadioInNestedFieldset(String outerFieldset, String nestedFieldset, String labelText) {
        try {
            WebElement nestedFieldsetElement = findNestedFieldsetByLegend(outerFieldset, nestedFieldset);

            // find the input associated with the specified (well-formed) label...
            WebElement labelElement = nestedFieldsetElement.findElement(
                    By.xpath(".//label[contains(text(),'" + labelText + "')]"));

            webDriver.findElement(By.id(labelElement.getAttribute("for"))).click();

        } catch (NoSuchElementException | IllegalArgumentException ex) {
            WebElement nestedFieldsetElement = null;
            try {
                nestedFieldsetElement = findNestedFieldsetByLegend(outerFieldset, nestedFieldset);

            } catch (NoSuchElementException ex2) {
                String message = "Fieldset " + nestedFieldset + " within fieldset " + outerFieldset + " not found";
                logger.error(message);
                throw new IllegalArgumentException(message);
            }

            nestedFieldsetElement.findElements(By.tagName("label")).stream()
                .filter((l) -> l.getText().contains(labelText)) // label with text
                .map((l) -> l.findElement(By.xpath("./input[@type = 'radio']"))) // nested radio
                .findFirst().orElseThrow(() -> {
                    String message = "No radio button found in fieldset " + nestedFieldset + " within fieldset "
                            + outerFieldset + "with label (well-formed or nested): " + labelText;
                    logger.error(message);
                    return new IllegalArgumentException(message);
                }).click();
        }
    }

    /**
     * Find a fieldset element, by the associated wrapped legend element text.
     * @param legend    The legend text
     * @return The fieldset element
     */
    private WebElement findFieldsetByLegend(String legend) {
        return webDriver.findElement(
            By.xpath("//legend[contains(text(),'" + legend + "')]/ancestor::fieldset[1]"));
    }

    /**
     * Find a nested fieldset element, by the associated wrapped legend element text.
     * @param outerLegend    The outer legend text
     * @param nestedLegend   The nested legend text
     * @return The nested fieldset element
     */
    private WebElement findNestedFieldsetByLegend(String outerLegend, String nestedLegend) {
        return findFieldsetByLegend(outerLegend).findElement(
                By.xpath(".//legend[contains(text(),'" + nestedLegend + "')]/ancestor::fieldset[1]"));
    }

    /**
     * Selects the specified checkbox button. Supports well-formed labels and inputs nested inside the label.
     * @param labelText  The checkbox button label
     */
    public void selectCheckbox(String labelText) {
        WebElement checkboxElement = findCheckbox(labelText);

        // click checkbox if not already selected, otherwise leave as selected
        if (!checkboxElement.isSelected()) {
            checkboxElement.click();
        }
    }

    /**
     * Clears the specified checkbox button. Supports well-formed labels and inputs nested inside the label.
     * @param labelText  The checkbox button label
     */
    public void clearCheckbox(String labelText) {
        WebElement checkboxElement = findCheckbox(labelText);

        // click checkbox if already selected, otherwise leave as not selected
        if (checkboxElement.isSelected()) {
            checkboxElement.click();
        }
    }

    /**
     * Finds the specified checkbox button. Supports well-formed labels and inputs nested inside the label.
     * @param labelText  The checkbox button label
     */
    private WebElement findCheckbox(String labelText) {
        try {
            // find the input associated with the specified (well-formed) label...
            WebElement labelElement = webDriver.findElement(By.xpath("//label[contains(text(),'" + labelText + "')]"));
            return webDriver.findElement(By.id(labelElement.getAttribute("for")));

        } catch (NoSuchElementException | IllegalArgumentException ex) {
            return webDriver.findElements(By.tagName("label")).stream()
                    .filter((label) -> label.getText().contains(labelText)) // label with text
                    .map((label) -> label.findElement(By.xpath("./input[@type = 'checkbox']"))) // nested checkbox
                    .findFirst().orElseThrow(() -> {
                        String message = "No checkbox button found with label (well-formed or nested): " + labelText;
                        logger.error(message);
                        return new IllegalArgumentException(message);
                    });
        }
    }

    /**
     * Selects the specified checkbox button by id.
     * @param idValue  The id of checkbox button
     */
    public void selectCheckboxById(String idValue) {
        WebElement checkboxElement = webDriver.findElement(By.id(idValue));

        // click checkbox if not already selected, otherwise leave as selected
        if (!checkboxElement.isSelected()) {
            checkboxElement.click();
        }
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
     * Checks whether a table, identified by having a heading with the specified text, has at least the specified
     * number of rows (ignoring any heading rows).
     * @param headingText       The table heading text to look for
     * @param rows              The minimum number of rows the table must have
     * @return <code>true</code> if the table has at least <code>rows</code> rows.
     */
    public boolean checkTableHasRows(String headingText, int rows) {
        try {
            // find the table with a heading containing the specified text...
            WebElement tableElement = webDriver.findElement(
                    By.xpath("//th[contains(text(),'" + headingText + "')]//ancestor::table[1]"));

            // then count the number of rows in the table...
            List<WebElement> rowElements = tableElement.findElements(By.tagName("tr"));

            // is the number of rows (minus the heading row) at lest the specified amount?
            return (rowElements.size() - 1) >= rows;

        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /**
     * Checks whether the expected text is present within any of the td columns present in the same row with the
     * row text, of which there may be several.
     * @param rowText       The row text to look for
     * @param expectedText  The text to check for in any matching row
     * @return <code>true</code> if at least one match is found.
     */
    public boolean checkTextFromAnyTableRow(String rowText, String expectedText) {
        return webDriver.findElements(By.xpath("//th[contains(text(),'" + rowText + "')]/../td")).stream()
                .anyMatch((WebElement e) -> {
                    String text = e.getText();
                    return (text != null && text.contains(expectedText));
                });
    }

    /**
     * Fetches the data in the td column in the same row as a tr element.
     * @param rowText       The text that will find the first row
     * @return The text found in the table, or an empty string
     */
    public String getTextFromTableRow(String rowText) {
        try {
            return webDriver.findElement(By.xpath("//th[contains(text(),'" + rowText + "')]/../td")).getText();

        } catch (NoSuchElementException ex) {
            return "";
        }
    }

    /**
     * Fetches the data in the td column in the same row as a link within a th element.
     * @param linkText      The link text to look for
     * @return The text found in the table row, or an empty string
     */
    public String getTextFromTableRowWithLink(String linkText) {
        try {
            return webDriver.findElement(By.xpath("//th/a[contains(text(),'" + linkText + "')]/../../td")).getText();

        } catch (NoSuchElementException ex) {
            return "";
        }
    }

    /**
     * Fetches the data in the column of the first row identified by table header text.
     * @param headerText - The text that will find the first row
     * @return The text found in the column
     */
    public String getTextFromTableColumn(String headerText) {
        List<WebElement> tableHeaders =
                webDriver.findElements(By.xpath("//thead/tr/th"));
        int columnIndex = 1;
        for (WebElement header : tableHeaders) {
            if (header.getText().contains(headerText)) {
                break;
            } else {
                columnIndex++;
            }
        }

        return webDriver.findElement(By.xpath("//tbody/tr/td[" + columnIndex + "]")).getText();
    }

    /**
     * Checks whether a specified piece of text exists under a certain column in the table.
     * @param column    The column name
     * @param value     The value to check for
     * @return          Whether the value exists in the table column
     */
    public boolean tableColumnContainsValue(String column, String value) {

        List<WebElement> tableHeaders =
                webDriver.findElements(By.xpath("//thead/tr/th"));
        int columnIndex = 1;
        boolean foundColumn = false;
        for (WebElement header : tableHeaders) {
            if (header.getText().contains(column)) {
                foundColumn = true;
                break;
            } else {
                columnIndex++;
            }
        }

        return foundColumn && webDriver.findElements(
                By.xpath("//tbody/tr/td[" + columnIndex + "][contains(text(), '" + value + "')]")).size() > 0;
    }

    /**
     * Fetches the text in any list items in a div following the h2 element (also in a div).
     * @param headingText   The heading text
     * @return The list item text (concatenated together)
     */
    public String getTextFromUnorderedList(String headingText) {
        WebElement heading = webDriver.findElement(By.xpath("//h2[contains(text(),'" + headingText + "')]"));
        StringBuilder builder = new StringBuilder();
        heading.findElements(By.xpath("../following-sibling::div[1]/ul/li"))
            .forEach((i) -> builder.append(i.getText()));
        return builder.toString();
    }


    /**
     * Fetches the data in the dd element following a specific dt element, in a flat dt/dd list.
     * @param termText  The text that will find the term
     * @return The description text found
     */
    public String getTextFromDefinitionList(String termText) {
        return webDriver.findElement(
                By.xpath("//dt[contains(text(),'" + termText + "')]/following-sibling::dd[1]")).getText();
    }

    /**
     * Fetches the text from any span elements within a h2 element that also has the specified text.
     * @param headingText  The heading text
     * @return The span text found
     */
    public String getTextFromHeading(String headingText) {
        StringBuilder builder = new StringBuilder();
        webDriver.findElements(By.xpath("//h2[contains(text(),'" + headingText + "')]/span"))
            .forEach((s) -> builder.append(s.getText()));
        return builder.toString();
    }

    /**
     * Fetches the text from the p element within a div following a h2 element within a div that has the specified text.
     * @param headingText  The heading text
     * @return The relative text found
     */
    public String getRelativeTextFromHeading(String headingText) {
        return webDriver.findElement(
                By.xpath("//h2[contains(text(),'" + headingText + "')]/../following-sibling::div[1]/p")).getText();
    }

    /**
     * Checks whether the current page contains the specified message, anywhere within the page. Use only with long
     * unique messages - may contain single quotes, and <code>{key}</code> format data keys, but not double quotes.
     * @param message   The message
     * @return <code>true</code> if found
     */
    public boolean containsMessage(String message) {
        return webDriver.findElements(
                By.xpath("//body[contains(normalize-space(.),\"" + expandDataKeys(message) + "\")]")).size() > 0;
    }

    /**
     * Checks whether the current page does not contain the specified message, anywhere within the page. Use only with
     * long unique messages - may contain single quotes, and <code>{key}</code> format data keys, but not double quotes.
     * @param message   The message
     * @return <code>true</code> if not found
     */
    public boolean doesNotContainMessage(String message) {
        return webDriver.findElements(
                By.xpath("//body[contains(normalize-space(.),\"" + expandDataKeys(message) + "\")]")).size() == 0;
    }

    /**
     * Expands any data references (of the format <code>{key}</code>) with the data value.
     * @param message   The message
     * @return The expanded message
     * @throws IllegalStateException if data item not found
     */
    private String expandDataKeys(String message) {
        Pattern pattern = Pattern.compile("\\{([^\\}]+)\\}");
        Matcher matcher = pattern.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            // data key name is the regex match, without the { prefix and } suffix
            String dataKey = message.substring(matcher.start() + 1, matcher.end() - 1);

            // replace data key with data value
            matcher.appendReplacement(buffer, getData(dataKey));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    /**
     * Selects a specified option in a dropdown field using the name to identify it, temporary fix for incorrect label.
     * @param optionText    The text of the option to select
     * @param name          The field name
     */
    public void selectOptionInFieldByName(String optionText, String name) {
        Select selectElement = new Select(webDriver.findElement(By.name(name)));
        selectElement.selectByVisibleText(optionText);
        waitForFullPageLoad();
    }

    /**
     * Selects a specified option in a dropdown field using the id to identify it, temporary fix for incorrect label.
     * @param optionText    The text of the option to select
     * @param id            The field id
     */
    public void selectOptionInFieldById(String optionText, String id) {
        Select selectElement = new Select(webDriver.findElement(By.id(id)));
        selectElement.selectByVisibleText(optionText);
        waitForFullPageLoad();
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
     * Returns the raw HTML for the current page.
     * @return The raw HTML
     */
    public String getHtml() {
        return webDriver.getPageSource();
    }

    /**
     * Click the specified element, and wait for a page load, unless the click was simply to trigger some JavaScript
     * in the current page.
     * @param element   The element to click
     */
    private void clickAndWaitForPageLoad(WebElement element) {
        String beforeUrl = webDriver.getCurrentUrl();
        logger.debug("*** before click - current URL is {}", beforeUrl);
        element.click();

        /*
         * Attempt to detect whether the click caused a full page refresh (in which case we need to wait for the new
         * page to load) or simply triggered some javascript in the current page (in which case waiting may cause
         * errors).
         *
         * Note: in practice selenium reports different page source in the second scenario (even though the page hasn't
         * changed, so page source can't be used to detect this reliably.
         */
        boolean fullPageRefresh = true;
        try {
            // triggers UnhandledAlertException if the click above launched an alert popup
            String afterUrl = webDriver.getCurrentUrl();
            logger.debug("*** after click - current URL is {}", afterUrl);

            /*
             * Note: for now, we assume if the page URL stays the same then it is the second javascript case. Of course
             * this depends on convention, the app could post back to the same URL.
             */
            if (afterUrl.equals(beforeUrl)) {
                fullPageRefresh = false;
            }
        } catch (UnhandledAlertException ex) {
            logger.info("Page has alert box so assuming no fullPageRefresh!");
            fullPageRefresh = false;
        }

        // only wait if there has been a full page refresh
        if (fullPageRefresh) {
            logger.debug("we assume there has been a full page refresh, so waiting...");
            waitForFullPageLoad();
        } else {
            logger.debug("we assume there has not been a full page refresh, so continuing almost immediately...");

            // short wait for the web browser to complete JavaScript events...
            try {
                Thread.sleep(200);

            } catch (InterruptedException ex) {
                // called if trying to shutdown the test suite
                String message = "Wait for web browser to submit current page was interrupted";
                logger.error(message, ex);

                // propagate a fatal error so testsuite shuts down
                throw new RuntimeException(message, ex);
            }

            logger.debug("Short wait completed...");
        }
    }

    /**
     * Wait for the web page to fully re-load, and any onload javascript events to complete.
     * Failure to do this between page transitions can result in intermittent failures, such as
     * Selenium still being on the previous page, or failure to find page element in the new page.
     */
    protected void waitForFullPageLoad() {
        logger.debug("Waiting for page reload...");

        // initial wait (in milliseconds) to give the selenium web driver time to tell the web browser to
        // submit the current page
        try {
            Thread.sleep(clickWaitMilliseconds);

        } catch (InterruptedException ex) {
            // called if trying to shutdown the test suite
            String message = "Wait for web browser to submit current page was interrupted";
            logger.error(message, ex);

            // propagate a fatal error so testsuite shuts down
            throw new RuntimeException(message, ex);
        }

        logger.debug("Initial wait completed...");

        /**
         * Selenium doesn't return HTTP status codes so we can't check for a 503 error, which happens when a test
         * environment is down. This is a dodgy Chrome-specific work around...
         */
        if (CHROME_BLANK_SCREEN.equals(webDriver.getPageSource())) {
            String message = "Environment is probably down?";
            logger.error(message);
            throw new RuntimeException(message);
        }

        // wait until page loaded, ready and JQuery processing completed...
        if ((Boolean) ((JavascriptExecutor)webDriver).executeScript("return window.jQuery != undefined")) {
            new WebDriverWait(webDriver, pageWaitSeconds)
                    .pollingEvery(pollFrequencyMilliseconds, TimeUnit.MILLISECONDS).until(
                        (ExpectedCondition<Boolean>) wd ->
                            ((JavascriptExecutor) wd).executeScript("return jQuery.active").equals(0L));

            logger.debug("Page loaded, ready and JQuery activity complete, waiting for footer image...");
        }

        // then for good measure wait for the footer to be available
        // the gocardless direct debit page and the card payment page are excluded from this wait
        if (!webDriver.getTitle().contains("Customer Payment Management System")
                && !webDriver.getTitle().contains("Payment details - BJSS")) {
            (new WebDriverWait(webDriver, pageWaitSeconds))
                .pollingEvery(pollFrequencyMilliseconds, TimeUnit.MILLISECONDS)
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("footer")));
            logger.debug("Footer image available");
        }

        debugCurrentPage();
    }

    /**
     * What Chrome has been observed to return as the page source when encountering a 503 error
     * and showing a "This page isn't working" screen.
     */
    private static final String CHROME_BLANK_SCREEN =
            "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head></head>"
                    + "<body><pre style=\"word-wrap: break-word; white-space: pre-wrap;\"></pre></body></html>";

    /**
     * Switch the webdriver context to a frame, required in CPMS pages.
     * @param frameIdOrName the name or id of the frame
     */
    public void switchToFrame(String frameIdOrName) {
        //Switch to the fram by id or name
        webDriver.switchTo().frame(frameIdOrName);
    }

    /**
     * Logs the current page URL and title as debug.
     */
    private void debugCurrentPage() {
        logger.debug("** At page {} - \"{}\" **", webDriver.getCurrentUrl(), webDriver.getTitle());
    }

    /**
     * Fills in all the fields with an id prefix.
     * @param idPrefix  the id prefix used to find the fields
     * @param text      the text to be entered into the fields
     */
    public void enterIntoAllFieldsWithIdPrefix(String idPrefix, String text) {
        List<WebElement> fields = webDriver.findElements(By.xpath("//*[contains(@id,'" + idPrefix + "')]"));

        for (WebElement field : fields) {
            field.clear();
            field.sendKeys(text);
        }
    }

    /**
     * Checks if there is a button present by text.
     * @param buttonText    The text on the button to look for
     * @return  return whether the button exists or not
     */
    public boolean checkButtonExists(String buttonText) {
        return findButtons(buttonText).size() > 0;
    }

    /**
     * Clicks a link with href containing value.
     * @param hrefContains  The value the href has to contain
     */
    public void clickLinkContainingHrefValue(String hrefContains) {
        WebElement link = webDriver.findElement(By.xpath("//*[contains(@href,'" + hrefContains + "')]"));
        link.click();
    }

    /**
     * Check whether the specified cookie has been set.
     * @param name      The cookie name (can be a partial match)
     * @return <code>true</code> if set
     */
    public boolean isCookieSet(String name) {
        return webDriver.manage().getCookies().stream()
                .anyMatch((Cookie c) -> c.getName().contains(name));
    }

    /**
     * Deletes the specified cookie.
     * @param name      The cookie name (can be a partial match)
     */
    public void deleteCookie(String name) {
        Cookie cookie = webDriver.manage().getCookies().stream()
                .filter((Cookie c) -> c.getName().contains(name))
                .findFirst().orElseThrow(() -> {
                    return new IllegalStateException("Cookie with name containing " + name + " not found");
                });

        webDriver.manage().deleteCookie(cookie);
    }

    /**
     * Check if an element is visible.
     *
     * @param selector Selector to find the element.
     * @return Return whether the element is visible or not.
     */
    public boolean isVisible(By selector) {
        WebElement element = webDriver.findElement(selector);

        if (element == null) {
            return false;
        }

        return element.isDisplayed();
    }

    /**
     * Get an attribute of an element.
     *
     * @param id ID of the element.
     * @param attribute Attribute to get.
     * @return Value of the attribute.
     */
    public String getAttribute(String id, String attribute) {
        WebElement element = webDriver.findElement(By.id(id));
        String value = element.getAttribute(attribute);

        return value == null ? "" : value;
    }

    /**
     * Enters the specified text into the field.
     * @param text  The text to enter
     */
    public void enterIntoFieldWithLabel(String labelText, String text) {
        // find the input associated with the specified label...
        WebElement labelElement = webDriver.findElement(
                By.xpath("//*[text() = '" + labelText + "']//ancestor::label"));
        WebElement textElement = webDriver.findElement(By.id(labelElement.getAttribute("for")));
        textElement.clear();
        textElement.sendKeys(text);
    }

    /**
     * Goes to the next tab.
     */
    public void goNextTab() {
        String oldTab = webDriver.getWindowHandle();
        List<String> newTab = new ArrayList<String>(webDriver.getWindowHandles());
        newTab.remove(oldTab);
        int numTabs = newTab.size();
        if (numTabs > 2) {
            String message = "Too many tabs. Expected 2 but there are " + numTabs;
            logger.error(message);
            throw new IllegalStateException(message);
        }
        // change focus to new tab
        webDriver.switchTo().window(newTab.get(0));
    }

    /**
     * Closes all but the main tab.
     */
    public void closeTabs() {
        String originalHandle = webDriver.getWindowHandle();
        for (String handle : webDriver.getWindowHandles()) {
            if (!handle.equals(originalHandle)) {
                webDriver.switchTo().window(handle);
                webDriver.close();
            }
        }

        webDriver.switchTo().window(originalHandle);
    }

    /**
     * Clicks the last occurrence of the text, which may contain single quotes, and <code>{key}</code> format
     * data keys, but not double quotes.
     * @param linkText  The text used to find the text
     */
    public void clickLastText(String linkText) {
        List<WebElement> spans = findSpans(linkText);
        if (spans.size() == 0) {
            String message = "Text not found: " + linkText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            clickAndWaitForPageLoad(spans.get(spans.size() - 1));
        }
    }

    /**
     * Creates a new PDF document from the request handler and the url.
     * @param url                       the URL of the PDF document
     * @return                          the PDF Document
     * @throws PdfException             error loading PDF document
     */
    private PdfDocument createPdfDocument(String url) throws PdfException {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setSortByPosition(true);

            return new PdfDocument(requestHandler.getPdfDocument(url));
        } catch (IOException ioException) {
            throw new PdfException("Unable to find or open PDF", ioException);
        }
    }

    /**
     * Checks whether a PDF contains expected values.
     * @param linkText  The link to the PDF
     * @param values    Array of values to check are contained
     * @return          Whether the PDF contains all expected values
     */
    public boolean pdfContains(String linkText, List<String> values) {
        // find any "a" elements with text containing the link text
        List<WebElement> links = findLinks(linkText);

        if (links.size() == 0) {
            String message = "No links found with text: " + linkText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            try {
                PdfDocument pdfDocument = createPdfDocument(links.get(0).getAttribute("href"));

                return pdfDocument.contains(values);
            } catch (PdfException ex) {
                logger.error("Unable to load PDF document", ex);
                throw new RuntimeException("Error processing PDF document", ex);
            }
        }
    }

    /**
     * Creates a CSV document from the URL provided.
     * @param       url of the target document
     * @return      parsed CSV containing the url output
     */
    private CsvDocument createCsvDocument(String url) {
        try {
            return requestHandler.getCsvDocument(url);
        } catch (CsvException ex) {
            logger.error(String.format("Failed to load CSV document from %s.", url), ex);
            throw new RuntimeException("Error processing CSV document", ex);
        }
    }

    /**
     * Checks whether a CSV contains expected values.
     * @param linkText  The link to the PDF
     * @param values    Array of values to check are contained
     * @return          Whether the CSV contains all expected values
     */
    public boolean csvContains(String linkText, List<String> values) {
        // find any "a" elements with text containing the link text
        List<WebElement> links = findLinks(linkText);

        if (links.size() == 0) {
            String message = "No links found with text: " + linkText;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            CsvDocument csvDocument = createCsvDocument(links.get(0).getAttribute("href"));

            return csvDocument.contains(values);
        }
    }

    /**
     * Pauses the tests for the required amount of seconds.
     * @param time  The amount of seconds to wait
     */
    public void timeWait(Integer time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException ex) {
            // called if trying to shutdown the test suite
            String message = "Wait for the web browser was interrupted";
            logger.error(message, ex);

            // propagate a fatal error so testsuite shuts down
            throw new RuntimeException(message, ex);
        }
    }

    /**
     * Clicks the specified accordion.
     * @param accordionId  The accordion ID
     */
    public void accordionClick(String accordionId) {
        List<WebElement> accordions =  webDriver.findElements(By.xpath("//p[@id = '" + accordionId + "']"));
        if (accordions.size() == 0) {
            String message = "No accordions found with ID name: " + accordionId;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else if (accordions.size() > 1) {
            String message = "Several accordions found with ID name: " + accordionId;
            logger.error(message);
            throw new IllegalArgumentException(message);

        } else {
            clickAndWaitForPageLoad(accordions.get(0));
        }
    }

    /**
     * Clicks the last help text dropdown specified.
     * @param helpText The text for the help dropdown
     */
    public void helptextClick(String helpText) {
        // Need to slow down the test so the browser can click on the correct element.
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            // called if trying to shutdown the test suite
            String message = "Wait for the web browser was interrupted";
            logger.error(message, ex);

            // propagate a fatal error so testsuite shuts down
            throw new RuntimeException(message, ex);
        }

        List<WebElement> spans = findSpans(helpText);
        if (spans.size() == 0) {
            String message = "No span elements found with text: " + helpText;
            logger.error(message);
            throw new IllegalArgumentException(message);
        } else {
            clickAndWaitForPageLoad(spans.get(spans.size() - 1));
        }
    }

    /**
     * Gets the current URL of the page.
     * @return the current URL
     */
    public String getCurrentUrl() {
        return this.webDriver.getCurrentUrl();
    }

    /**
     * Gets a list of web elements and returns the text as a string array list.
     * @param className the class name of the elements to be found
     * @param relativePath the relative path for the elements
     * @return Strings contained in the web elements
     */
    public ArrayList<String> getTextFromElementsByClass(String className, String relativePath) {
        ArrayList<String> elementsText = new ArrayList<>();

        for (WebElement element : webDriver.findElements(By.xpath(
                String.format("//ul[contains(@class, '%s')]/%s", className, relativePath)))) {
            elementsText.add(element.getText().replaceAll("\n", " "));
        }

        return elementsText;
    }

    /**
     * Sets the starting url using a key for the config file.
     * @param startingUrlKey the name of the starting url key in the config
     */
    public void setStartingUrl(String startingUrlKey) {
        this.startingUrl = env.getRequiredProperty(startingUrlKey);
        logger.debug("Switched starting url to: " + startingUrl);
    }

    /**
     * Gets the current number of open tabs in the driver.
     * @return return the number of open tabs
     */
    public int getCurrentTabsCount() {
        return webDriver.getWindowHandles().size();
    }

    /**
     * Switches the current webdriver context to a specified tab.
     * @param tabNumber the tab number to switch to
     */
    public void switchToTab(int tabNumber) {
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tabNumber));
    }

    /**
     * wait for the specified button.
     *
     * @param buttonText The button text
     */
    public void waitForButton(Integer seconds, Integer refresh, String buttonText) {

        for (int i = 0; i < refresh; i++) {
            List<WebElement> button = findLinks(buttonText);

            if (button.size() == 1) {
                String message = "button found with text: " + buttonText;
                logger.info(message);

            } else if (button.size() == 0) {
                webDriver.navigate().refresh();
                timeWait(seconds);

            } else {
                String message = "button not found in time allocated: " + buttonText;
                logger.warn(message);
                throw new IllegalArgumentException(message);
            }
        }
    }
}
