package uk.gov.dvsa.mot.framework;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Handles Cucumber lifecycle hooks (events).
 */
public class LifecycleHooks {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(LifecycleHooks.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    @Inject
    public LifecycleHooks(WebDriverWrapper driverWrapper) {
        logger.debug("Creating LifecycleHooks...");
        this.driverWrapper = driverWrapper;
    }

    /**
     * Runs before every Cucumber test scenario.
     * @param scenario      The test scenario being run
     */
    @Before
    public void startup(Scenario scenario) {
        // test initialisation goes here
        logger.debug("Before cucumber scenario: {}", scenario.getName());
    }

    /**
     * Runs after every Cucumber test scenario.
     * @param scenario      The test scenario that has run
     */
    @After
    public void teardown(Scenario scenario) {
        logger.debug("After cucumber scenario: {}", scenario.getName());

        if (scenario.isFailed()) {
            logger.debug("scenario failed");
            // could take extra action here (take screenshots?)
        }

        // could take extra action here (take screenshots, record ending URL, etc)

        // test cleanup goes here

        driverWrapper.reset();
    }
}
