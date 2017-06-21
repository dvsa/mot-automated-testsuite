package uk.gov.dvsa.mot.framework;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.data.DatabaseDataProvider;

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

    /** The take screenshots configuration setting. */
    private final boolean takeScreenshotsOnErrorOnly;

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

        String takeScreenshots = env.getProperty("takeScreenshots");
        if ("always".equals(takeScreenshots)) {
            this.takeScreenshotsOnErrorOnly = false;

        } else if ("onErrorOnly".equals(takeScreenshots)) {
            this.takeScreenshotsOnErrorOnly = true;

        } else {
            String message = "Unknown takeScreenshots setting: " + takeScreenshots;
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Runs before every Cucumber test scenario.
     * @param scenario      The test scenario being run
     */
    @Before
    public void startup(Scenario scenario) {
        // load the test datasets to use in the test(s)
        dataProvider.loadAllDatasets();

        logger.debug("Before cucumber scenario: ********** {} **********", scenario.getName());
    }

    /**
     * Runs after every Cucumber test scenario.
     * @param scenario      The test scenario that has run
     */
    @After
    public void teardown(Scenario scenario) {
        logger.debug("After cucumber scenario: ********** {} **********", scenario.getName());

        if ((takeScreenshotsOnErrorOnly && scenario.isFailed()) || !takeScreenshotsOnErrorOnly) {
            // take screenshot of the final page reached in the test
            byte[] screenshot = driverWrapper.takeScreenshot();
            if (screenshot != null ) {
                scenario.embed(screenshot, "image/png");
            }
        }

        // test cleanup
        driverWrapper.reset();
    }
}
