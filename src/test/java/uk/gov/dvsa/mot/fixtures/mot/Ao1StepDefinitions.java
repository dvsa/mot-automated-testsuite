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

        And("^I check the \"([^\"]+)\" field row has value \"([^\"]+)\"$",
                (String fieldName, String value) -> assertTrue("Wrong field value",
                    driverWrapper.getTextFromTableRow(fieldName).contains(value)));
    }
}
