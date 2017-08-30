package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Handles CSCO steps.
 */
public class CscoStepDefintions implements En {

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    @Inject
    public CscoStepDefintions(WebDriverWrapper driverWrapper) {

        And("^I check the Authorised Examiner Business details AE ID is \\{([^\\}]+)\\}$",
                (String aeNumberKey) -> {
                    driverWrapper.getTextFromTableRow("Authorised Examiner ID")
                            .contains(driverWrapper.getData(aeNumberKey));
            });
        And("^I check the Site city/town details city is \\{([^\\}]+)\\}$",
                (String aTownKey) -> {
                    driverWrapper.getTextFromTableColumn("City/postcode")
                            .contains(driverWrapper.getData(aTownKey));
                });
        And("^I check the Site postcode details city is \\{([^\\}]+)\\}$",
                (String aPostcode) -> {
                    driverWrapper.getTextFromTableRow("Address")
                            .contains(driverWrapper.getData(aPostcode));
                });
    }
}
