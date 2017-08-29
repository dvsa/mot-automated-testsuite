package uk.gov.dvsa.mot.fixtures.mot;

import static org.junit.Assert.assertTrue;

import cucumber.api.java8.En;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

/**
 * Handles steps for Area Office 1 (AO1) test scenarios.
 */
public class Ao1StepDefinitions implements En {

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    public Ao1StepDefinitions(WebDriverWrapper driverWrapper) {

        And("^I click the \"([^\"]+)\" link for the \"([^\"]+)\" field row$",
                (String linkText, String fieldName) ->
                    driverWrapper.clickLink("th", fieldName, "../td/", linkText));

        And("^I click the remove role link for \\{([^\\}]+)\\}$", (String nameKey) ->
                driverWrapper.clickLink("a", driverWrapper.getData(nameKey),
                    "../../td/", "Remove"));

        And("^I check the \"([^\"]+)\" field row has value \"([^\"]+)\"$",
                (String fieldName, String value) -> assertTrue("Wrong field value",
                    driverWrapper.getTextFromTableRow(fieldName).contains(value)));

        And("^I check for assign role notification message for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String usernameKey, String nameKey) ->
                    assertTrue("Message not found",
                            driverWrapper.containsMessage("A role notification has been sent to "
                                + driverWrapper.getData(nameKey) + " '" + driverWrapper.getData(usernameKey) + "'.")));

        And("^I check for remove role notification message for \\{([^\\}]+)\\}$",
                (String nameKey) ->
                     assertTrue("Message not found",
                         driverWrapper.containsMessage("You have removed the role of Authorised"
                             + " examiner designated manager from " + driverWrapper.getData(nameKey))));
    }
}
