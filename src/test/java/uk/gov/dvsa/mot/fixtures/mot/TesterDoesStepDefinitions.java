package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Step definitions specific to the <i>Tester does...</i> feature.
 */
public class TesterDoesStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(TesterDoesStepDefinitions.class);

    /** The web driver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public TesterDoesStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating TesterDoesStepDefinitions...");
        this.driverWrapper = driverWrapper;

        And("^I enter an odometer reading of \\{([^\\}]+)\\} plus (\\d+)$",
                (String dataKey, Integer amount) -> {
                    enterOdometerReading(driverWrapper.getData(dataKey), amount);
            });

        And("^I enter odometer not present$", () -> {
            enterOdometerNotPresent();
        });

        And("^I click the \"Aborted by VE\" radio button$", () -> {
            // unfortunately given no proper formed label etc we have to use the id
            driverWrapper.clickElement("reasonForCancel25");
        });

        And("^The MOT status is \"([^\"]+)\"$", (String status) -> {
            // unfortunately given no proper formed label etc we have to use the id
            assertTrue("Wrong MOT status", driverWrapper.getElementText("testStatus").contains(status));
        });

        When("^I start an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$", (String regKey, String vinKey) -> {
            startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey), false);
        });

        When("^I start an MOT retest for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$", (String regKey, String vinKey) -> {
            startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey), true);
        });

        And("^I browse for a \"([^\"]+)\" defect of \\(\"([^\"]+)\", \"([^\"]+)\", \"([^\"]+)\"\\) "
                + "with comment \"([^\"]+)\"$", (String defectType, String category, String subcategory, String defect,
                    String comment) -> {
                        browseForDefect(defectType, category, subcategory, defect, comment);
            });

        And("^I search for a \"([^\"]+)\" defect of \"([^\"]+)\" with comment \"([^\"]+)\"$",
                (String defectType, String defect, String comment) -> {
                    searchForDefect(defectType, defect, comment);
            });

        And("^I enter decelerometer results of service brake (\\d+) and parking brake (\\d+)$",
                (Integer serviceBrakeResult, Integer parkingBrakeResult) -> {
                    enterDecelerometerBrakeResults(serviceBrakeResult, parkingBrakeResult);
            });

        And("^I mark the defect \"([^\"]+)\" as repaired$", (String defect) -> {
            markAsRepaired(defect);
        });

        And("^I search for a vehicle with \"([^\"]+)\", \"([^\"]+)\"$", (String reg, String vin) -> {
            searchForVehicle(reg, vin);
        });

        And("^I check the \"Add brake test\" link is hidden$", () -> {
            assertTrue(driverWrapper.getLinkClass("Add brake test").contains("hidden"));
        });

        And("^I check the vehicle summary section of the test summary has \"([^\"]+)\" of \\{([^\\}]+)\\}$",
                (String field, String key) -> {
                    assertEquals(driverWrapper.getData(key), driverWrapper.getTextFromDefinitionList(field));
            });

        And("^I check the test information section of the test summary is \"([^\"]+)\"$", (String text) -> {
            assertTrue(driverWrapper.getTextFromHeading("Test information").contains(text));
        });

        And("^I check the brake results section of the test summary is \"([^\"]+)\"$", (String text) -> {
            assertEquals(text, driverWrapper.getRelativeTextFromHeading("Brake results overall"));
        });

        And("^I check the fails section of the test summary has \"([^\"]+)\"$", (String text) -> {
            assertTrue(driverWrapper.getTextFromUnorderedList("Fails").contains(text));
        });

        And("^I check the prs section of the test summary has \"([^\"]+)\"$", (String text) -> {
            assertTrue(driverWrapper.getTextFromUnorderedList("PRS").contains(text));
        });

        And("^I check the advisory section of the test summary has \"([^\"]+)\"$", (String text) -> {
            assertTrue(driverWrapper.getTextFromUnorderedList("Advisory").contains(text));
        });
    }

    /**
     * Starts an MOT test for the specified vehicle. Refactored repeated cucumber steps, the original steps are
     * detailed below.
     * @param registration  The registration number to use
     * @param vin           The VIN to use
     * @param isRetest      Whether this is a retest
     */
    private void startMotTest(String registration, String vin, boolean isRetest) {
        //And I Search for a vehicle
        searchForVehicle(registration, vin);

        if (isRetest) {
            // check for the retest marker next to the select vehicle link
            String actual = driverWrapper.getElementText("a", "Select vehicle", "../span");
            assertEquals("Missing retest marker next to select vehicle link", "For retest", actual);

            //And I click the "Select vehicle" link
            driverWrapper.clickLink("Select vehicle");

            //And The page title contains "Confirm vehicle for retest"
            driverWrapper.checkCurrentPageTitle("Confirm vehicle for retest");

            //And I press the "Confirm and start retest" button
            driverWrapper.pressButton("Confirm and start retest");

            //And The page title contains "MOT test started"
            driverWrapper.checkCurrentPageTitle("MOT retest started");

        } else {
            //And I click the "Select vehicle" link
            driverWrapper.clickLink("Select vehicle");

            //And The page title contains "Confirm vehicle and start test"
            driverWrapper.checkCurrentPageTitle("Confirm vehicle and start test");

            //And I press the "Confirm and start test" button
            driverWrapper.pressButton("Confirm and start test");

            //And The page title contains "MOT test started"
            driverWrapper.checkCurrentPageTitle("MOT test started");
        }

        //And I click the "Continue to home" link
        driverWrapper.clickLink("Continue to home");
    }

    /**
     * Search for a vehicle from the trade user search.
     * @param registration  The registration of the vehicle to find
     * @param vin           The vin number of the vehicle to find
     */
    private void searchForVehicle(String registration, String vin) {
        // When I click the "Start MOT test" link
        driverWrapper.clickLink("Start MOT test");

        // if page title Select your current site
        if (driverWrapper.getCurrentPageTitle().contains("Select your current site")) {
            // select the first site (radios of name vtsId)
            driverWrapper.clickRadioButtonByText(driverWrapper.getData("site"));
            // press "Confirm" button
            driverWrapper.pressButton("Confirm");
        }

        // And The page title contains "Find a vehicle"
        driverWrapper.checkCurrentPageTitle("Find a vehicle");
        //And I enter <registration1> in the "Registration mark" field
        driverWrapper.enterIntoField(registration, "Registration mark");
        //And I enter <vin1> in the "VIN" field
        driverWrapper.enterIntoField(vin, "VIN");
        //And I press the "Search" button
        driverWrapper.pressButton("Search");

        //And The page title contains "Find a vehicle"
        driverWrapper.checkCurrentPageTitle("Find a vehicle");
    }

    /**
     * Enter an odometer reading of mileage plus the specified amount. Refactored repeated cucumber steps, the
     * original steps are detailed below.
     * @param mileage   The mileage
     * @param amount    The additional amount
     */
    private void enterOdometerReading(String mileage, int amount) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add reading" link
        driverWrapper.clickLink("Add reading");

        // And The page title contains "Odometer reading"
        driverWrapper.checkCurrentPageTitle("Odometer reading");
        // And I enter {mileage1} plus <n> in the odometer field
        int newMileage = Integer.parseInt(mileage) + amount;
        driverWrapper.enterIntoFieldWithId(String.valueOf(newMileage), "odometer");
        // And I press the "Update reading" button
        driverWrapper.pressButton("Update reading");
    }

    /**
     * Enter odometer not present. Refactored repeated cucumber steps, the original steps are detailed below.
     */
    private void enterOdometerNotPresent() {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add reading" link
        driverWrapper.clickLink("Add reading");

        // And The page title contains "Odometer reading"
        driverWrapper.checkCurrentPageTitle("Odometer reading");
        // And I select the "Odometer is not present" radio button (using id as has badly formed label)
        driverWrapper.selectRadioById("noOdometer");
        // And I press the "Update reading" button
        driverWrapper.pressButton("Update reading");
    }

    /**
     * Enter a Decelerometer brake test result. Refactored repeated cucumber steps, the original steps are detailed
     * below.
     * @param serviceBrakeResult    The service brake result
     * @param parkingBrakeResult    The parking brake result
     */
    private void enterDecelerometerBrakeResults(int serviceBrakeResult, int parkingBrakeResult) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add brake test" link
        driverWrapper.clickLink("Add brake test");

        // And The page title contains "Brake test configuration"
        //defect in release 3.10, title missing - driverWrapper.checkCurrentPageTitle("Brake test configuration");
        // And I select "Decelerometer" in the "Service brake test type" field
        driverWrapper.selectOptionInField("Decelerometer", "Service brake test type");
        // And I select "Decelerometer" in the "Parking brake test type" field
        driverWrapper.selectOptionInField("Decelerometer", "Parking brake test type");
        // And I press the "Next" button
        driverWrapper.pressButton("Next");

        // And The page title contains "Add brake test results"
        driverWrapper.checkCurrentPageTitle("Add brake test results");
        // And I enter <n> in the "Service brake" field
        driverWrapper.enterIntoField(String.valueOf(serviceBrakeResult), "Service brake");
        // And I enter <n> in the "Parking brake" field
        driverWrapper.enterIntoField(String.valueOf(parkingBrakeResult), "Parking brake");
        // And I press the "Submit" button
        driverWrapper.pressButton("Submit");

        // And The page title contains "Brake test summary"
        driverWrapper.checkCurrentPageTitle("Brake test summary");
        // And I click the "Done" link
        driverWrapper.clickLink("Done");
    }

    /**
     * Marks the specified defect as repaired.
     * @param defect    The defect
     */
    private void markAsRepaired(String defect) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I press the "Mark as repaired" button for the specified defect
        driverWrapper.pressButtonWithSiblingElement(
                "Mark as repaired","input", "value", defect);
    }

    /**
     * Adds a defect to the current MOT test, by browsing through the specified category and sub-category. Refactored
     * repeated cucumber steps, the original steps are detailed below.
     * @param defectType    The defect type, must be "Advisory", "PRS" or "Failure"
     * @param category      The defect category
     * @param subcategory   The defect sub-category
     * @param defect        The defect
     * @param comment       The comment to use
     */
    private void browseForDefect(String defectType, String category, String subcategory, String defect,
                                 String comment) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add a defect" link
        driverWrapper.clickLink("Add a defect");

        // And The page title contains "Defect categories"
        driverWrapper.checkCurrentPageTitle("Defect categories");
        // And I click the <category> link
        driverWrapper.clickLink(category);
        // And I click the <subcategory> link
        driverWrapper.clickLink(subcategory);
        switch (defectType) {
            case "Failure":
                // And I click the Failure button for the specified defect
                driverWrapper.clickLink("strong", defect, "../../ul/", "Failure");
                // And The page title contains "Add a failure"
                driverWrapper.checkCurrentPageTitle("Add a failure");
                // And I enter <comment> into the "Add any further comments if required" field
                driverWrapper.enterIntoField(comment, "Add any further comments if required");
                // And I press the "Add failure" button
                driverWrapper.pressButton("Add failure");
                break;

            case "PRS":
                break;

            case "Advisory":
                break;

            default:
                String message = "Unknown defect type: " + defectType;
                logger.error(message);
                throw new IllegalArgumentException(message);
        }

        // And The page title contains "Defects"
        driverWrapper.checkCurrentPageTitle("Defects");
        // And I click the "Finish and return to MOT test results" link
        driverWrapper.clickLink("Finish and return to MOT test results");

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Adds a defect to the current MOT test, by searching for the specified defect text. Refactored repeated cucumber
     * steps, the original steps are detailed below.
     * @param defectType    The defect type, must be "Advisory", "PRS" or "Failure"
     * @param defect        The defect
     * @param comment       The comment to use
     */
    private void searchForDefect(String defectType, String defect, String comment) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Search for a defect" link
        driverWrapper.clickLink("Search for a defect");

        // And The page title contains "Search for a defect"
        driverWrapper.checkCurrentPageTitle("Search for a defect");
        // And I enter <text> into the <search-main> field (has no label)
        driverWrapper.enterIntoFieldWithId(defect, "search-main");
        // And I press the "Search" button
        driverWrapper.pressButton("Search");
        switch (defectType) {
            case "Failure":
                // And I click the Failure button for the specified defect
                driverWrapper.clickLink("div/strong", defect, "../../ul/", "Failure");
                // And The page title contains "Add a failure"
                driverWrapper.checkCurrentPageTitle("Add a failure");
                // And I enter <comment> into the "Add any further comments if required" field
                driverWrapper.enterIntoField(comment, "Add any further comments if required");
                // And I press the "Add failure" button
                driverWrapper.pressButton("Add failure");
                break;

            case "PRS":
                break;

            case "Advisory":
                break;

            default:
                String message = "Unknown defect type: " + defectType;
                logger.error(message);
                throw new IllegalArgumentException(message);
        }

        // And The page title contains "Search for a defect"
        driverWrapper.checkCurrentPageTitle("Search for a defect");
        // And I click the "Finish and return to MOT test results" link
        driverWrapper.clickLink("Finish and return to MOT test results");

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }
}
