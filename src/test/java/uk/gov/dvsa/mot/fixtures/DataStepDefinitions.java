package uk.gov.dvsa.mot.fixtures;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.data.DataProvider;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Step definitions for test data loading steps.
 */
public class DataStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataStepDefinitions.class);

    @Inject
    public DataStepDefinitions(WebDriverWrapper driverWrapper, DataProvider dataProvider) {
        logger.debug("Creating DataStepDefinitions...");

        When("^I load \"([^\"]*)\" as \"([^\"]*)\"$", (String type, String keyName) -> {
            driverWrapper.setData(keyName, dataProvider.loadUser(type));
        });
    }
}
