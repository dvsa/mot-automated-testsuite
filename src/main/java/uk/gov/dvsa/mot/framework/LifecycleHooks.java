package uk.gov.dvsa.mot.framework;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import javax.inject.Inject;

/**
 * Handles Cucumber lifecycle hooks (events).
 */
public class LifecycleHooks {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(LifecycleHooks.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /** The configuration settings to use. */
    private final Environment env;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param env               The configuration settings
     */
    @Inject
    public LifecycleHooks(WebDriverWrapper driverWrapper, Environment env) {
        logger.debug("Creating LifecycleHooks...");
        this.driverWrapper = driverWrapper;
        this.env = env;
    }

    /**
     * Runs before every Cucumber test scenario.
     * @param scenario      The test scenario being run
     */
    @Before
    public void startup(Scenario scenario) {
        logger.debug("Before cucumber scenario: ********** {} **********", scenario.getName());
    }

    /**
     * Runs after every Cucumber test scenario.
     * @param scenario      The test scenario that has run
     */
    @After
    public void teardown(Scenario scenario) {
        logger.debug("After cucumber scenario: ********** {} **********", scenario.getName());

        // add to the test report
        outputDataUse(scenario);

        // For all non-B2B scenarios take screenshot, HTML and sign out at the end
        if (!scenario.getSourceTagNames().contains("@mothb2b")) {
            outputFinalScreenshot(scenario);
            outputFinalHtml(scenario);

            // log the current user out, if they are logged in
            if (driverWrapper.hasLink("Sign out")) {
                logger.debug("Logging the current user out...");
                driverWrapper.clickLink("Sign out");
            }
        }

        // test cleanup (note: this will clear permanent cookies but not HTTP/Session cookies)
        driverWrapper.reset();

        // close any extra tabs in the browser
        driverWrapper.closeTabs();
    }

    /**
     * Output the values of any dataset entries used in this test scenario, useful for investigating test failures and
     * as a record of what exactly was tested. The output gets picked up by the Cucumber reports and plugins.
     * @param scenario  The scenario just completed
     */
    private void outputDataUse(Scenario scenario) {
        List<String> keys = driverWrapper.getAllDataKeys();
        if (keys.size() > 0) {
            scenario.write("The following data values were used in this test run:");
            for (String key: keys) {
                scenario.write("{" + key + "} => " + driverWrapper.getData(key));
            }
        }
    }

    /**
     * Output a screenshot of the final web page reached in this test scenario, useful for investigating test
     * failures and as a record of what exactly was tested. The output gets picked up by the Cucumber reports and
     * plugins.
     * <p>Uses the <i>takeScreenshots</i> configuration setting.</p>
     * @param scenario  The scenario just completed
     */
    private void outputFinalScreenshot(Scenario scenario) {
        String takeScreenshots = env.getRequiredProperty("takeScreenshots");
        switch (takeScreenshots) {
            case "onErrorOnly":
                if (scenario.isFailed()) {
                    takeScreenshot(scenario);
                }
                break;

            case "always":
                takeScreenshot(scenario);
                break;

            case "never":
                break;

            default:
                String message = "Unknown takeScreenshots setting: " + takeScreenshots;
                logger.error(message);
                throw new IllegalArgumentException(message);
        }
    }

    /**
     * Output the raw HTML of the final web page reached in this test scenario, useful for investigating test
     * failures and as a record of what exactly was tested. The files are output to the same directory as the reports.
     * <p>Uses the <i>outputHtml</i> configuration setting.</p>
     * @param scenario  The scenario just completed
     */
    private void outputFinalHtml(Scenario scenario) {
        String outputHtml = env.getRequiredProperty("outputHtml");
        switch (outputHtml) {
            case "onErrorOnly":
                if (scenario.isFailed()) {
                    writeHtml(scenario);
                }
                break;

            case "always":
                writeHtml(scenario);
                break;

            case "never":
                break;

            default:
                String message = "Unknown outputHtml setting: " + outputHtml;
                logger.error(message);
                throw new IllegalArgumentException(message);
        }
    }

    /**
     * Take a screenshot of the current browser window, and outputs it to the report, which gets picked up by the
     * Cucumber default reports and reporting plugins.
     * <p>Note: most Selenium web drivers, especially Chrome, only output the visible portion of the screen.</p>
     * @param scenario  The current scenario
     */
    private void takeScreenshot(Scenario scenario) {
        byte[] screenshot = driverWrapper.takeScreenshot();
        if (screenshot != null ) {
            scenario.embed(screenshot, "image/png");
        }
    }

    /**
     * Writes the raw HTML to a file.
     * @param scenario  The current scenario
     */
    private void writeHtml(Scenario scenario) {
        String html = driverWrapper.getHtml();

        File dir = new File("target/html");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = null;
        try {
            // Convert to a safe filename
            String safeScenarioName = scenario.getName().replaceAll("/", "-");
            file = new File(dir + File.separator + safeScenarioName + ".html");
            file.createNewFile();

        } catch (IOException ex) {
            String message = "Error creating HTML file: " + ex.getMessage();
            logger.error(message, ex);
            // swallow error, abort the write
            return;
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "UTF-8"))) {
            out.write(html);

        } catch (IOException ex) {
            String message = "Error writing HTML file: " + ex.getMessage();
            logger.error(message, ex);
            // swallow error and continue
        }
    }
}
