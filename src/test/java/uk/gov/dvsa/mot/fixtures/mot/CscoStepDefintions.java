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

    }
}
