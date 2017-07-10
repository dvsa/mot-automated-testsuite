package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.util.Optional;
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

        And("^I enter an odometer reading in miles of \\{([^\\}]+)\\} plus (\\d+)$",
                (String dataKey, Integer amount) -> {
                    int startingMileage = Integer.parseInt(driverWrapper.getData(dataKey));
                    enterOdometerReading(OdometerJourney.EnterInMiles, startingMileage + amount);
            });

        And("^I enter an odometer reading in kilometres of (\\d+)$", (Integer amount) ->
                enterOdometerReading(OdometerJourney.EnterInKilometres, amount));

        And("^I enter odometer not present$", () ->
                enterOdometerReading(OdometerJourney.NotPresent, 0));

        And("^I click the \"Aborted by VE\" radio button$", () ->
                // unfortunately given no proper formed label etc we have to use the id
                driverWrapper.clickElement("reasonForCancel25"));

        And("^The MOT status is \"([^\"]+)\"$", (String status) ->
                // unfortunately given no proper formed label etc we have to use the id
                assertTrue("Wrong MOT status", driverWrapper.getElementText("testStatus").contains(status)));

        When("^I start an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$", (String regKey, String vinKey) ->
                startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                        false, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

        When("^I start an MOT retest for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$", (String regKey, String vinKey) ->
                startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey),
                        true, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

        When("^I start an MOT test for DVLA vehicle \\{([^\\}]+)\\}, \\{([^\\}]+)\\} as class (\\d+)$",
                (String regKey, String vinKey, Integer vehicleClass) ->
                startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey), false,
                        Optional.of(vehicleClass), Optional.empty(), Optional.empty(), Optional.empty()));

        When("^I start an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\} with colour changed to \"([^\"]+)\"$",
                (String regKey, String vinKey, String colour) ->
                        startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey), false,
                                Optional.empty(), Optional.of(colour), Optional.empty(), Optional.empty()));

        When("^I start an MOT test for \\{([^\\}]+)\\}, \\{([^\\}]+)\\} with engine changed to \"([^\"]+)\""
                + " with capacity (\\d+)$", (String regKey, String vinKey, String fuelType, Integer capacity) ->
                        startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey), false,
                                Optional.empty(), Optional.empty(), Optional.of(fuelType), Optional.of(capacity)));

        And("^I browse for a \"([^\"]+)\" defect of \\(\"([^\"]+)\", \"([^\"]+)\"\\) "
                + "with comment \"([^\"]+)\"$", (String defectType, String category, String defect, String comment) ->
                    browseForDefect(defectType, category, Optional.empty(), defect, comment));

        And("^I browse for a \"([^\"]+)\" defect of \\(\"([^\"]+)\", \"([^\"]+)\", \"([^\"]+)\"\\) "
                + "with comment \"([^\"]+)\"$", (String defectType, String category, String subcategory, String defect,
                    String comment) -> browseForDefect(defectType, category, Optional.of(subcategory), defect,
                        comment));

        And("^I search for a \"([^\"]+)\" defect of \"([^\"]+)\" with comment \"([^\"]+)\"$",
                this::searchForDefect);

        And("^I add a manual advisory of \"([^\"]+)\"$", this::addManualAdvisory);

        And("^I edit the \"([^\"]+)\" defect of \"([^\"]+)\" with comment \"([^\"]+)\" and not dangerous$",
                (String defectType, String defect, String updatedComment) ->
                    editDefect(defectType, defect, updatedComment, false));

        And("^I edit the \"([^\"]+)\" defect of \"([^\"]+)\" with comment \"([^\"]+)\" and is dangerous$",
                (String defectType, String defect, String updatedComment) ->
                    editDefect(defectType, defect, updatedComment, true));

        And("^I remove the \"([^\"]+)\" defect of \"([^\"]+)\"$", this::removeDefect);

        And("^I enter decelerometer results of efficiency (\\d+)$", (Integer efficiency) ->
                enterDecelerometerBrakeResults(efficiency));

        And("^I enter decelerometer results of service brake (\\d+) and parking brake (\\d+)$",
                (Integer serviceBrake, Integer parkingBrake) ->
                        enterDecelerometerBrakeResults(serviceBrake, parkingBrake));

        And("^I enter decelerometer service brake result of (\\d+) and gradient parking brake result "
                + "of \"([^\"]+)\"$", this::enterDecelerometerAndGradientBrakeResults);

        And("^I mark the defect \"([^\"]+)\" as repaired$", this::markAsRepaired);

        And("^I search for a vehicle with \"([^\"]+)\", \"([^\"]+)\"$", this::searchForVehicle);

        And("^I check the \"Add brake test\" link is hidden$", () ->
                assertTrue(driverWrapper.getLinkClass("Add brake test").contains("hidden")));

        And("^I check the vehicle summary section of the test summary has \"([^\"]+)\" of \\{([^\\}]+)\\}$",
                (String field, String key) ->
                    assertEquals(driverWrapper.getData(key), driverWrapper.getTextFromDefinitionList(field)));

        And("^I check the vehicle summary section of the test summary has \"([^\"]+)\" of \"([^\"]+)\"$",
                (String field, String value) ->
                        assertEquals(value, driverWrapper.getTextFromDefinitionList(field)));

        And("^I check the test information section of the test summary is \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromHeading("Test information").contains(text)));

        And("^I check the brake results section of the test summary is \"([^\"]+)\"$", (String text) ->
                assertEquals(text, driverWrapper.getRelativeTextFromHeading("Brake results overall")));

        And("^I check the fails section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromUnorderedList("Fails").contains(text)));

        And("^I check the prs section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromUnorderedList("PRS").contains(text)));

        And("^I check the advisory section of the test summary has \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.getTextFromUnorderedList("Advisory text").contains(text)));

        And("^I check the fails section of the test summary does not have \"([^\"]+)\"$", (String text) ->
                assertFalse(driverWrapper.getTextFromUnorderedList("Fails").contains(text)));

        And("^I check the prs section of the test summary does not have \"([^\"]+)\"$", (String text) ->
                assertFalse(driverWrapper.getTextFromUnorderedList("PRS").contains(text)));

        And("^I check the advisory section of the test summary does not have \"([^\"]+)\"$", (String text) ->
                assertFalse(driverWrapper.getTextFromUnorderedList("Advisory text").contains(text)));
    }

    /**
     * Starts an MOT test for the specified vehicle. Refactored repeated cucumber steps, the original steps are
     * detailed below.
     * @param registration  The registration number to use
     * @param vin           The VIN to use
     * @param isRetest      Whether this is a retest
     * @param vehicleClass  The vehicle class to nominate (if any)
     * @param colour        The new primary colour to change to (if any)
     * @param fuelType      The new engine fuel type to change to (if any)
     * @param capacity      The new engine capacity to change to (if any)
     */
    private void startMotTest(String registration, String vin, boolean isRetest, Optional<Integer> vehicleClass,
                              Optional<String> colour, Optional<String> fuelType, Optional<Integer> capacity) {
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

            if (vehicleClass.isPresent()) {
                //And I click the "Change" link for the MOT test class
                driverWrapper.clickLink("th", "MOT test class", "../td/", "Change");

                //And I select the Class <n> radio button (by id as badly formed label)
                driverWrapper.selectRadioById("class" + vehicleClass.get());

                //And I press the "Continue" button
                driverWrapper.pressButton("Continue");
            }

            if (colour.isPresent()) {
                //And I click the "Change" link for the colour
                driverWrapper.clickLink("th", "Colour", "../td/", "Change");

                //And I select <colour> in the "Primary Colour" field
                driverWrapper.selectOptionInField(colour.get(), "Primary colour");

                //And I select "Not stated" in the "Secondary Colour" field
                driverWrapper.selectOptionInField("Not stated", "Secondary colour");

                //And I press the "Continue" button
                driverWrapper.pressButton("Continue");
            }

            if (fuelType.isPresent() || capacity.isPresent()) {
                //And I click the "Change" link for the engine
                driverWrapper.clickLink("th", "Engine", "../td/", "Change");

                if (fuelType.isPresent()) {
                    //And I select <fuel type> in the "Fuel type" field
                    driverWrapper.selectOptionInField(fuelType.get(), "Fuel type");
                }

                if (capacity.isPresent()) {
                    //And I enter <capacity> in the "Cylinder capacity" field
                    driverWrapper.enterIntoField(String.valueOf(capacity.get()), "Cylinder capacity");
                }

                //And I press the "Continue" button
                driverWrapper.pressButton("Continue");
            }

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

    /** Encapsulates the user journey for the Odometer screen. */
    private enum OdometerJourney {
        EnterInMiles, EnterInKilometres, NotPresent;
    }

    /**
     * Enter an odometer reading of the specified amount/type. Refactored repeated cucumber steps, the original steps
     * are detailed below.
     * @param journey   The user journey being taken
     * @param amount    The new odometer reading amount
     */
    private void enterOdometerReading(OdometerJourney journey, int amount) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add reading" link
        driverWrapper.clickLink("Add reading");

        // And The page title contains "Odometer reading"
        driverWrapper.checkCurrentPageTitle("Odometer reading");

        switch (journey) {
            case EnterInMiles:
                // And I enter <n> in the odometer field
                driverWrapper.enterIntoFieldWithId(String.valueOf(amount), "odometer");

                // And I select "Miles" in the "Measurement units" field
                driverWrapper.selectOptionInField("Miles", "Measurement units");
                break;

            case EnterInKilometres:
                // And I enter <n> in the odometer field
                driverWrapper.enterIntoFieldWithId(String.valueOf(amount), "odometer");

                // And I select "Kilometres" in the "Measurement units" field
                driverWrapper.selectOptionInField("Kilometres", "Measurement units");
                break;

            case NotPresent:
                // And I select the "Odometer is not present" radio button (using id as has badly formed label)
                driverWrapper.selectRadioById("noOdometer");
                break;

            default:
                String message = "Unknown Odometer journey: " + journey;
                logger.error(message);
                throw new IllegalArgumentException(message);
        }

        // And I press the "Update reading" button
        driverWrapper.pressButton("Update reading");
    }

    /**
     * Enter a Decelerometer brake test result (class 1). Refactored repeated cucumber steps, the original steps are
     * detailed below.
     * @param brakeResult    The brake result
     */
    private void enterDecelerometerBrakeResults(Integer brakeResult) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add brake test" link
        driverWrapper.clickLink("Add brake test");

        // And The page title contains "Brake test configuration"
        //defect in release 3.10, title missing - driverWrapper.checkCurrentPageTitle("Brake test configuration");
        // And I select "Decelerometer" in the "Brake test type" field
        driverWrapper.selectOptionInField("Decelerometer", "Brake test type");
        // And I press the "Next" button
        driverWrapper.pressButton("Next");

        // And The page title contains "Add brake test results"
        driverWrapper.checkCurrentPageTitle("Add brake test results");
        // And I enter <n> in the "Control one" field
        driverWrapper.enterIntoField(String.valueOf(brakeResult), "Control one");
        // And I enter <n> in the "Control two" field
        driverWrapper.enterIntoField(String.valueOf(brakeResult), "Control two");
        // And I press the "Submit" button
        driverWrapper.pressButton("Submit");

        // And The page title contains "Brake test summary"
        driverWrapper.checkCurrentPageTitle("Brake test summary");
        // And I click the "Done" link
        driverWrapper.clickLink("Done");
    }

    /**
     * Enter a Decelerometer brake test result. Refactored repeated cucumber steps, the original steps are detailed
     * below.
     * @param serviceBrakeResult    The service brake result
     * @param parkingBrakeResult    The parking brake result
     */
    private void enterDecelerometerBrakeResults(Integer serviceBrakeResult, Integer parkingBrakeResult) {
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
     * Enter a Decelerometer brake test result (service brake) and Gradient (parking brake). Refactored repeated
     * cucumber steps, the original steps are detailed below.
     * @param serviceBrakeResult    The service brake result
     * @param parkingBrakeResult    The parking brake result ("Pass" or "Fail")
     */
    private void enterDecelerometerAndGradientBrakeResults(Integer serviceBrakeResult, String parkingBrakeResult) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add brake test" link
        driverWrapper.clickLink("Add brake test");

        // And The page title contains "Brake test configuration"
        //defect in release 3.10, title missing - driverWrapper.checkCurrentPageTitle("Brake test configuration");
        // And I select "Decelerometer" in the "Service brake test type" field
        driverWrapper.selectOptionInField("Decelerometer", "Service brake test type");
        // And I select "Gradient" in the "Parking brake test type" field
        driverWrapper.selectOptionInField("Gradient", "Parking brake test type");
        // And I press the "Next" button
        driverWrapper.pressButton("Next");

        // And The page title contains "Add brake test results"
        driverWrapper.checkCurrentPageTitle("Add brake test results");
        // And I enter <n> in the "Service brake" field
        driverWrapper.enterIntoField(String.valueOf(serviceBrakeResult), "Service brake");
        switch (parkingBrakeResult) {
            case "Pass":
                // And I select the pass parking brake radio button (by id as label badly formed)
                driverWrapper.selectRadioById("parkingBrakeEfficiencyPassPass");
                break;

            case "Fail":
                // And I select the fail parking brake radio button (by id as label badly formed)
                driverWrapper.selectRadioById("parkingBrakeEfficiencyPassFail");
                break;

            default:
                String message = "Unknown Gradient parking brake result: " + parkingBrakeResult;
                logger.error(message);
                throw new IllegalArgumentException(message);
        }

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
     * Edits the specified defect, updating the comment, and possibly marking as dangerous. Refactored repeated
     * cucumber steps, the original steps are detailed below.
     * @param defectType        The defect type, must be "Advisory", "PRS" or "Failure"
     * @param defect            The defect
     * @param updatedComment    The updated comment
     * @param isDangerous       Whether this defect should be marked as dangerous
     */
    private void editDefect(String defectType, String defect, String updatedComment, boolean isDangerous) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");

        // edit the defect
        handleDefect(DefectJourney.EditFromSummary, defect, DefectType.fromString(defectType), updatedComment,
                isDangerous);

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Removes the specified defect. Refactored repeated cucumber steps, the original steps are detailed below.
     * @param defectType        The defect type, must be "Advisory", "PRS" or "Failure"
     * @param defect            The defect
     */
    private void removeDefect(String defectType, String defect) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");

        // And I click the "Remove" link for the specified defect
        driverWrapper.clickLink("h4", defect, "../../ul/", "Remove");

        // Note: page title is blank

        // And I press the "Remove <failure/PRS/advisory>" button
        driverWrapper.pressButton("Remove " + DefectType.fromString(defectType).description);

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Adds a defect to the current MOT test, by browsing through the specified category and optional sub-category.
     * Refactored repeated cucumber steps, the original steps are detailed below.
     * @param defectType    The defect type, must be "Advisory", "PRS" or "Failure"
     * @param category      The defect category
     * @param subcategory   The defect sub-category (if any)
     * @param defect        The defect
     * @param comment       The comment to use
     */
    private void browseForDefect(String defectType, String category, Optional<String> subcategory, String defect,
                                 String comment) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add a defect" link
        driverWrapper.clickLink("Add a defect");

        // And The page title contains "Defect categories"
        driverWrapper.checkCurrentPageTitle("Defect categories");
        // And I click the <category> link
        driverWrapper.clickLink(category);

        if (subcategory.isPresent()) {
            // And I click the <subcategory> link
            driverWrapper.clickLink(subcategory.get());
        }

        // add the defect
        handleDefect(DefectJourney.AddFromBrowse, defect, DefectType.fromString(defectType), comment, false);

        // And The page title contains "Defects"
        driverWrapper.checkCurrentPageTitle("Defects");
        // And I click the "Finish and return to MOT test results" link
        driverWrapper.clickLink("Finish and return to MOT test results");

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Adds a manual advisory defect to the current MOT test. Refactored repeated cucumber steps, the original steps
     * are detailed below.
     * @param comment       The comment to use
     */
    private void addManualAdvisory(String comment) {
        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
        // And I click the "Add a defect" link
        driverWrapper.clickLink("Add a defect");

        // And The page title contains "Defect categories"
        driverWrapper.checkCurrentPageTitle("Defect categories");
        // And I click the "Add a manual advisory" link
        driverWrapper.clickLink("Add a manual advisory");

        // note - page title is blank
        // And I enter <comment> into the "Give brief details of the defect" field
        driverWrapper.enterIntoField(comment, "Give brief details of the defect");
        // And I press the "Add manual advisory" button
        driverWrapper.pressButton("Add manual advisory");

        // And The page title contains "Defect categories"
        driverWrapper.checkCurrentPageTitle("Defect categories");
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

        // add the defect
        handleDefect(DefectJourney.AddFromSearch, defect, DefectType.fromString(defectType), comment, false);

        // And The page title contains "Search for a defect"
        driverWrapper.checkCurrentPageTitle("Search for a defect");
        // And I click the "Finish and return to MOT test results" link
        driverWrapper.clickLink("Finish and return to MOT test results");

        // And The page title contains "MOT test results"
        driverWrapper.checkCurrentPageTitle("MOT test results");
    }

    /**
     * Handles adding and editing defects of all types, from defect browse or search.
     * @param journey                   The user journey being followed
     * @param defect                    The defect description
     * @param defectType                The defect type
     * @param comment                   The comment to add/update
     * @param isDangerous               Whether to mark this defect as dangerous
     */
    private void handleDefect(DefectJourney journey, String defect, DefectType defectType, String comment,
                              boolean isDangerous) {
        switch (journey) {
            case AddFromSearch:
                // And I click the <Advisory> button for the specified defect
                // (note - need to ignore the defect listed as "You searched for...")
                driverWrapper.clickLink("div/strong", defect, "../../ul/", defectType.type);
                break;

            case AddFromBrowse:
                // And I click the Advisory button for the specified defect
                driverWrapper.clickLink("strong", defect, "../../ul/", defectType.type);
                break;

            case EditFromSummary:
                // And I click the "Edit" link for the specified defect
                driverWrapper.clickLink("h4", defect, "../../ul/", "Edit");
                break;

            default:
                break;
        }

        // Note: new page title at this point is not always populated

        // And I enter <comment> into the "Add any further comments if required" field
        driverWrapper.enterIntoField(comment, "Add any further comments if required");

        if (isDangerous) {
            // And I select the dangerous radio button (by id as label badly formed)
            driverWrapper.selectRadioById(defectType.type.toLowerCase() + "Dangerous");
        }

        // And I press the "<Add/Edit> <advisory>" button
        driverWrapper.pressButton(journey.action + " " + defectType.description);
    }

    /** Encapsulates the user journey being followed. */
    private enum DefectJourney {
        AddFromSearch("Add"), AddFromBrowse("Add"), EditFromSummary("Edit");

        /** The action being applied to the defect. */
        private final String action;

        /**
         * Creates a new instance.
         * @param action    The defect action
         */
        DefectJourney(String action) {
            this.action = action;
        }
    }

    /** Encapsulates the type of defect. */
    private enum DefectType {
        Failure("Failure", "failure"), PRS("PRS", "PRS"), Advisory("Advisory", "advisory");

        /** Capitalised defect type. */
        private final String type;

        /** Lower case (unless abbreviated) defect type. */
        private final String description;

        /**
         * Creates a new instance.
         * @param type              The Capitalised defect type.
         * @param description       The Lower case (unless abbreviated) defect type.
         */
        DefectType(String type, String description) {
            this.type = type;
            this.description = description;
        }

        /**
         * Converts a <code>String</code> to a <code>DefectType</code>.
         * @param type      The string
         * @return The defect type
         * @throws IllegalArgumentException If the string is invalid
         */
        public static DefectType fromString(String type) {
            switch (type) {
                case "Failure":
                    return Failure;

                case "PRS":
                    return PRS;

                case "Advisory":
                    return Advisory;

                default:
                    String message = "Unknown defect type: " + type;
                    logger.error(message);
                    throw new IllegalArgumentException(message);
            }
        }
    }
}
