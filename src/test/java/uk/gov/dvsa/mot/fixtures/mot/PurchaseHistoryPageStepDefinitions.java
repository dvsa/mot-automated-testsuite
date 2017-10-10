package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Step definitions specific to the purchase history page of the mot app.
 */
public class PurchaseHistoryPageStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(HomePageStepDefinitions.class);

    /** The Webdriver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The webdriver to use
     */
    @Inject
    public PurchaseHistoryPageStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating PurchaseHistoryPageStepDefinitions");
        this.driverWrapper = driverWrapper;

        Then("^The summary line contains \"([^\"]*)\"$", (String text) -> {
            checkIfLabelChanged(text);
        });

        When("^I click the \"([^\"]*)\" button$", (String buttonText) -> {
            driverWrapper.clickButton(buttonText);
        });
    }

    /**
     * Check if summaryLine contains text.
     * @param text Text to look for in summaryLine.
     */
    private void checkIfLabelChanged(String text) {
        assertTrue(driverWrapper.getElementText("summaryLine").contains(text));
    }
}
