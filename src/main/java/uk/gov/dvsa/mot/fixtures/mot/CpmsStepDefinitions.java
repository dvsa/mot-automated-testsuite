package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;

import org.openqa.selenium.By;
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

        And("^I record the current slot count as \"([^\"]+)\"$", (String arg1) -> {
            storeCurrentSlotCount(arg1);
        });

        And("^I check the slot count \"([^\"]+)\" is reduced by (\\d+)$",
                (String variableName, Integer slotsChange) -> {
                    checkSlotCountUpdated(variableName, slotsChange);
                });

        And("^The table field \"([^\"]+)\" has value \"([^\"]+)\"$", (String tableField, String tableValue) -> {
            assertTrue("Table does not contain expected value", driverWrapper.checkTextFromAnyTableRow(
                    tableField, tableValue));
        });

        And("^The table field \"([^\"]+)\" has value \\{([^\\}]+)\\}$", (String tableField, String tableValue) -> {
            assertTrue("Table does not contain expected value", driverWrapper.checkTextFromAnyTableRow(
                    tableField, driverWrapper.getData(tableValue)));
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
        assertTrue(driverWrapper.isVisible(By.id("print")));
    }

    /**
     * Check if link contains print function.
     */
    private void checkIfPrintButtonContainsPrintFunction() {
        assertTrue(driverWrapper.getAttribute("print", "href").contains("print()"));
    }

    /**
     * Stores the current slot count for comparison later.
     * @param variableName  The name of the variable to store the count under
     */
    private void storeCurrentSlotCount(String variableName) {
        driverWrapper.setData(variableName, driverWrapper.getElementText("slot-count")
                .split("\n")[0]);
    }

    /**
     * Verifies that the slot count has been reduced as expected.
     * @param variableName  The variable storing the original value
     * @param slotsChange   The number of slots the slot count should have been reduced by
     */
    private void checkSlotCountUpdated(String variableName, Integer slotsChange) {

        Integer currentCount = Integer.valueOf(
                driverWrapper.getElementText("slot-count").split("\n")[0].replace(",", ""));

        Integer previousCount = Integer.valueOf(driverWrapper.getData(variableName).replace(",", ""));

        assertTrue("Slot count is not as expected", previousCount - slotsChange == currentCount);
    }
}
