package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

public class VehicleExaminerStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(VehicleExaminerStepDefinitions.class);

    /** The web driver to use. */
    private WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The webdriver to use
     */
    @Inject
    public VehicleExaminerStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating VehicleExaminerStepDefinitions");
        this.driverWrapper = driverWrapper;

        And("^I search for an mot by \"([^\"]+)\" with \\{([^\\}]+)\\}$",
                (String searchType, String searchTextKey) -> {
                    searchForMotTests(searchType, driverWrapper.getData(searchTextKey));
                });

        And("^I start a \"([^\"]+)\" test$", (String testType) -> {
            startTestTypeOnMot(testType);
        });

        And("^I search for vehicle information by \"([^\"]+)\" with \\{([^\\}]+)\\}$",
                (String searchType, String searchTextKey) -> {
                    vehicleInformationSearch(searchType, driverWrapper.getData(searchTextKey));
                });

        And("^I perform a test comparison with outcome \"([^\"]+)\" and justification \"([^\"]+)\"$",
                (String caseOutcome, String justification) -> {
                    compareTestResults(caseOutcome, justification);
                });
        And("^I check the case outcome \"([^\"]+)\" is saved$", (String expectedOutcome) -> {
            checkCaseOutcome(expectedOutcome);
        });

        And("^I click the view certificate link for test number \\{([^\\}]+)\\}$",
                (String testNumberKey) -> {
                    clickViewCertificateForTestNumber(testNumberKey);
                });
    }

    /**
     * Searches for MOT tests by a search type.
     * @param searchType    The search type to use from the drop down
     * @param searchText    The text to search for
     */
    private void searchForMotTests(String searchType, String searchText) {
        //And I select the search type from the dropdown
        driverWrapper.selectOptionInFieldByName(searchType, "type");

        //And I enter the search text into the field
        driverWrapper.enterIntoFieldWithId(searchText, "vts-search");

        //And I press the search button
        driverWrapper.clickElement("item-selector-btn-search");
    }

    /**
     * Starts a test type on an previous mot test.
     * @param testType  The test type to be performed
     */
    private void startTestTypeOnMot(String testType) {
        //And I select the test type
        driverWrapper.selectOptionInFieldByName(testType, "motTestType");

        //And I start the test
        driverWrapper.pressButton("Start inspection");
    }

    /**
     * Searches for vehicle information by vin or vrm.
     * @param searchType    The search to be done
     * @param searchText    The text to search for
     */
    private void vehicleInformationSearch(String searchType, String searchText) {
        //And I select the search type from the dropdown
        driverWrapper.selectOptionInFieldByName(searchType, "type");

        //And I enter the search text
        driverWrapper.enterIntoFieldWithId(searchText, "vehicle-search");

        //And I search for the vehicle
        driverWrapper.clickElement("item-selector-btn-search");
    }

    /**
     * Submits an assessment in an mot comparison.
     * @param caseOutcome   The case outcome to select
     * @param justification The justification text to be entered
     */
    private void compareTestResults(String caseOutcome, String justification) {
        //And I fill out any other justification fields on the page
        driverWrapper.enterIntoAllFieldsWithIdPrefix("-justification", justification);

        //And Select the caseOutcome
        driverWrapper.selectOptionInFieldByName(caseOutcome, "caseOutcome");

        //And I enter the justification text
        driverWrapper.enterIntoFieldWithId(justification, "finalJustification");

        //And i press the record assessment button
        driverWrapper.clickButton("Record Assessment");
    }

    /**
     * Checks the case outcome on the assessment details saved page.
     * @param expectedOutcome   The expected case outcome from the test
     */
    private void checkCaseOutcome(String expectedOutcome) {
        //And I check the case outcome is as expected
        assertTrue("The case outcome is different than expected",
                driverWrapper.getTextFromTableRow("Outcome").contains(expectedOutcome));
    }

    /**
     * Clicks a view certificate link for a particular mot test number.
     * @param testNumberKey The browser key for the mot test number
     */
    private void clickViewCertificateForTestNumber(String testNumberKey) {
        //And I click the link for mot test
        driverWrapper.clickLinkContainingHrefValue(driverWrapper.getData(testNumberKey));
    }
}
