package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Step definitions specific to the CPMS tests.
 */
public class CpmsStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(CpmsStepDefinitions.class);

    /** The Webdriver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The webdriver to use
     */
    @Inject
    public CpmsStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating CpmsStepDefinitions");
        this.driverWrapper = driverWrapper;

        Then("^The summary line contains \"([^\"]*)\"$", (String text) -> {
            checkIfLabelChanged(text);
        });

        When("^I click the \"([^\"]*)\" button$", (String buttonText) -> {
            driverWrapper.clickButton(buttonText);
        });

        And("^I click the first link in the transaction table$", () -> {
            clickFirstLinkInTransactionTable();
        });

        Then("^The \"Print\" button is visible on the page$", () -> {
            checkIfPrintButtonIsVisible();
            checkIfPrintButtonContainsPrintFunction();
        });
    }

    /**
     * Check if summaryLine contains text.
     *
     * @param text Text to look for in summaryLine.
     */
    private void checkIfLabelChanged(String text) {
        assertTrue(driverWrapper.getElementText("summaryLine").contains(text));
    }

    /**
     * Click the first link in the transaction table.
     */
    private void clickFirstLinkInTransactionTable() {
        String transactionNumber = driverWrapper.getTextFromTableColumn("Transaction number");

        driverWrapper.clickLink(transactionNumber);
    }

    /**
     * Check if link is visible on the page.
     */
    private void checkIfPrintButtonIsVisible() {
        assertTrue(driverWrapper.isVisible("print"));
    }

    /**
     * Check if link contains print function.
     */
    private void checkIfPrintButtonContainsPrintFunction() {
        assertTrue(driverWrapper.getAttribute("print", "href").contains("print()"));
    }
}
