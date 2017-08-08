package uk.gov.dvsa.mot.framework;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.data.DatabaseDataProvider;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    /** The data provider to use. */
    private final DatabaseDataProvider dataProvider;

    /** The configuration settings to use. */
    private final Environment env;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param env               The configuration settings
     * @param dataProvider      The data provider to use
     */
    @Inject
    public LifecycleHooks(WebDriverWrapper driverWrapper, Environment env, DatabaseDataProvider dataProvider) {
        logger.debug("Creating LifecycleHooks...");
        this.driverWrapper = driverWrapper;
        this.dataProvider = dataProvider;
        this.env = env;
    }

    /**
     * Runs before every Cucumber test scenario.
     * @param scenario      The test scenario being run
     */
    @Before
    public void startup(Scenario scenario) {
        logger.debug("Before cucumber scenario: ********** {} **********", scenario.getName());

        // load the test datasets to use in the test(s)
        dataProvider.loadAllDatasets();
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
        outputFinalScreenshot(scenario);
        outputFinalHtml(scenario);

        // test cleanup
        driverWrapper.reset();
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
