package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;



/**
 * Step definitions specific to the <i>Tester does...</i> feature.
 */
public class FinanceStepDefinitions implements En {

    /**
     * The driver wrapper to use.
     */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     *
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public FinanceStepDefinitions(WebDriverWrapper driverWrapper, WebDriverWrapper driverWrapper1) {
        this.driverWrapper = driverWrapper1;

        And("^I click the link \"([^\"]+)\" with id \"([^\"]+)\"$", (String text, String id) ->
                driverWrapper.clickElement(id));

        When("^I enter the last 8 characters of \\{([^\\}]+)\\} in the field with id \"([^\"]+)\"$",
                (String dataKey, String id) -> enterLast8CharsIntoFieldWithId(driverWrapper.getData(dataKey), id));

    }

    /**
     * Enters the last 8 chars of the payment/invoice reference string.
     * @param reference the payment/invoice reference string
     * @param id        the id of the field to enter the value into
     */
    private void enterLast8CharsIntoFieldWithId(String reference, String id) {
        driverWrapper.enterIntoFieldWithId(reference.substring(reference.length() - 8, reference.length()), id);
    }

}