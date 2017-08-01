package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.time.LocalDate;

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
        And("^I search for AE information with \\{([^\\}]+)\\}$", (String aeRefKey) -> {
            searchForAeInformation(aeRefKey);
        });

        And("^I check the AE name is \\{([^\\}]+)\\}$", (String aeNameKey) -> {
            assertTrue("AE Name is incorrect", driverWrapper.getTextFromTableRow("Name")
                    .contains(driverWrapper.getData(aeNameKey)));
        });

        And("^I search for Site information by site number with \\{([^\\}]+)\\}$", (String siteNumberKey) -> {
            searchForSiteInformationByNumber(siteNumberKey);
        });

        And("^I check the site name is \\{([^\\}]+)\\}$", (String siteNameKey) -> {
            assertTrue("The site name is incorrect", driverWrapper.getTextFromTableRow("Name")
                    .contains(driverWrapper.getData(siteNameKey)));
        });

        And("^I search for user with username \\{([^\\}]+)\\}$", (String usernameKey) -> {
            searchForUserByUsername(usernameKey);
        });

        And("^I check the slot usage for the past (\\d+) days is \\{([^\\}]+)\\}$",
                (Integer days, String slotUsageKey) -> {
                    checkSlotUsageForCustomDateRange(days, slotUsageKey);
                });

        And("^I abort the active mot test at site for reg \\{([^\\}]+)\\}, vin \\{([^\\}]+)\\}$",
                (String regKey, String vinKey) -> {
                    abortActiveMotTestOnSite(regKey, vinKey);
                });

        And("^I check the reg \\{([^\\}]+)\\}, vin \\{([^\\}]+)\\} on vehicle information",
                (String regKey, String vinKey) -> {
                    checkRegAndVinForVehicleInformation(regKey, vinKey);
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

    /**
     * Searches for AE information from the registered company number.
     * @param aeRefKey  The data key for the registered company number
     */
    private void searchForAeInformation(String aeRefKey) {
        //And I click the AE information link
        driverWrapper.clickLink("AE information");

        //And I enter the AE number into the field
        driverWrapper.enterIntoField(driverWrapper.getData(aeRefKey), "AE Number");

        //And I press the Search button
        driverWrapper.pressButton("Search");

        //And I check the page title contains Authorised Examiner
        driverWrapper.checkCurrentPageTitle("Authorised Examiner");
    }

    /**
     * Searches for site information using the site's number.
     * @param siteNumberKey The key for the site number to search with
     */
    private void searchForSiteInformationByNumber(String siteNumberKey) {
        //And I click the Site information link
        driverWrapper.clickLink("Site information");

        //And I enter the site number
        String siteNumber = driverWrapper.getData(siteNumberKey);
        driverWrapper.enterIntoField(siteNumber, "Site ID");

        //And I press the search button
        driverWrapper.pressButton("Search");

        //Check if multiple records were returned
        if (driverWrapper.hasLink(siteNumber)) {
            driverWrapper.clickFirstLink(siteNumber);
        }
    }

    /**
     * Search for a user by their username.
     * @param usernameKey   The key for the username data to be used
     */
    private void searchForUserByUsername(String usernameKey) {
        //And I click the User Search link
        driverWrapper.clickLink("User search");

        //And I enter the username into the field
        driverWrapper.enterIntoField(driverWrapper.getData(usernameKey), "Username");

        //And i press the search button
        driverWrapper.pressButton("Search");
    }

    /**
     * Enters a custom date range from a specified number of days ago.
     * @param days        The number of days to check back
     * @param slotUsageKey  The date key for slot usage
     */
    private void checkSlotUsageForCustomDateRange(int days, String slotUsageKey) {
        //And I enter the from date
        LocalDate today = LocalDate.now();
        LocalDate fromDate = LocalDate.now().minusDays(days);

        driverWrapper.enterTextInFieldWithName("dateFrom[day]", String.valueOf(fromDate.getDayOfMonth()));
        driverWrapper.enterTextInFieldWithName("dateFrom[month]", String.valueOf(fromDate.getMonthValue()));
        driverWrapper.enterTextInFieldWithName("dateFrom[year]",String.valueOf(fromDate.getYear()));

        //And I enter the to date
        driverWrapper.enterTextInFieldWithName("dateTo[day]", String.valueOf(today.getDayOfMonth()));
        driverWrapper.enterTextInFieldWithName("dateTo[month]", String.valueOf(today.getMonthValue()));
        driverWrapper.enterTextInFieldWithName("dateTo[year]", String.valueOf(today.getYear()));

        //And i press the update results button
        driverWrapper.clickButton("Update results");

        //Create the message if plural or not
        String message;

        if (Integer.valueOf(driverWrapper.getData(slotUsageKey)) == 1) {
            message = " slot used";
        } else {
            message = " slots used";
        }

        //And I check the slot usage is correct
        assertTrue("The slot usage is not correct",
                driverWrapper.getElementText("summaryLine")
                        .contains(driverWrapper.getData(slotUsageKey) + message));
    }

    /**
     * Aborts an mot test on the site information page.
     * @param regKey    The reg key for the link
     * @param vinKey    The vin key for checking the page
     */
    private void abortActiveMotTestOnSite(String regKey, String vinKey) {
        //And I click the reg link
        driverWrapper.clickLink(driverWrapper.getData(regKey));

        //Check the vin number
        String vin = driverWrapper.getTextFromDefinitionList("Vehicle Identification Number");
        assertTrue("The vin is incorrect on view active mot",
                vin.contains(driverWrapper.getData(vinKey)));

        //And I click the abort mot test link
        driverWrapper.clickLink("Abort MOT Test");

        //And I select the aborted by ve reason
        driverWrapper.selectRadio("Aborted by VE");

        //And I press the abort mot test button
        driverWrapper.pressButton("Abort MOT test");

        //And the page title contains mot test aborted
        driverWrapper.checkCurrentPageTitle("MOT test aborted");

        //And I check the print VT30 button is present
        assertTrue("The print VT30 button is missing", driverWrapper.hasLink("Print VT30"));
    }

    /**
     * Checks the vin and reg on the vehicle information page.
     * @param regKey    The data key for the reg to check
     * @param vinKey    The data key for the vin to check
     */
    private void checkRegAndVinForVehicleInformation(String regKey, String vinKey) {
        //And I check the vehicle reg on the site information page
        assertTrue("The registration is not correct",
                driverWrapper.getTextFromTableRow("Registration mark")
                        .contains(driverWrapper.getData(regKey)));

        //And I check the vehicle vin on the site information page
        assertTrue("The vin is not correct", driverWrapper.getTextFromTableRow("VIN")
                .contains(driverWrapper.getData(vinKey)));
    }
}
