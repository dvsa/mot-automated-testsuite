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

    /** The driverwrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The web driver wrapper to be used
     */
    @Inject
    public AedmStepDefintions(WebDriverWrapper driverWrapper) {

        this.driverWrapper = driverWrapper;

        And("^I check the organisation role assignment confirmation message for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String usernameKey, String nameKey) -> {
                    String message = String.format("A role notification has been sent to %s '%s'",
                            driverWrapper.getData(nameKey), driverWrapper.getData(usernameKey));
                    assertTrue("Role notification message incorrect",
                            driverWrapper.getElementText("validation-message--success").contains(message));
                });

        And("^I check the site test log has the recent test \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String regKey, String testerUsernameKey) -> {
                    checkSiteTestLog(regKey, testerUsernameKey);
                });
    }

    /**
     * Checks the latest row in the test log a test completed.
     * @param regKey            The data key for the vehicle reg
     * @param testerUsernameKey The data key for the tester's username
     */
    private void checkSiteTestLog(String regKey, String testerUsernameKey) {
        //And I check the registration is correct
        assertTrue("The reg does not match the test log",
                driverWrapper.getTextFromTableColumn("VRM")
                        .contains(driverWrapper.getData(regKey)));

        //And I check the tester username key is correct
        assertTrue("The tester username is incorrect",
                driverWrapper.getTextFromTableColumn("User/Site Id")
                        .contains(driverWrapper.getData(testerUsernameKey)));
    }
}
