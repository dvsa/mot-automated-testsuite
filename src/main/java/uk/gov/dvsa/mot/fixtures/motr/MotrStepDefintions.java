package uk.gov.dvsa.mot.fixtures.motr;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;


public class MotrStepDefintions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(MotrStepDefintions.class);

    /** The driverwrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The web driver wrapper to be used
     */
    @Inject
    public MotrStepDefintions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        When("^I enter \"([^\"]+)\" in the registration number field$", (String text) ->
                driverWrapper.enterIntoFieldWithId(text, "regNumber"));

        When("^I enter \\{([^\\}]+)\\} in the registration number field$", (String dataKey) ->
                driverWrapper.enterIntoFieldWithId(driverWrapper.getData(dataKey), "regNumber"));

    }

}