package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

public class AedmStepDefintions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(AedmStepDefintions.class);

    /**
     * Create a new instance.
     * @param driverWrapper The web driver wrapper to be used
     */
    @Inject
    public AedmStepDefintions(WebDriverWrapper driverWrapper) {

        And("^I check the organisation role assignment confirmation message for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String usernameKey, String nameKey) -> {
                    String message = String.format("A role notification has been sent to %s '%s'",
                            driverWrapper.getData(nameKey), driverWrapper.getData(usernameKey));
                    assertTrue("Role notification message incorrect",
                            driverWrapper.getElementText("validation-message--success").contains(message));
                });
    }
}
