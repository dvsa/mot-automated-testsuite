package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

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

        When("^I enter <([^>]*)> plus (\\d+) in the odometer field$", (String dataKey, Integer amount) -> {
            int newMileage = Integer.parseInt(driverWrapper.getData(dataKey)) + amount;
            driverWrapper.enterIntoFieldWithId(String.valueOf(newMileage), "odometer");
        });

        And("^I click the \"Aborted by VE\" radio button$", () -> {
            // unfortunately given no proper formed label etc we have to use the id
            driverWrapper.clickElement("reasonForCancel25");
        });

        And("^The MOT status is \"([^\"]*)\"$", (String status) -> {
            // unfortunately given no proper formed label etc we have to use the id
            assertTrue("Wrong MOT status", driverWrapper.getElementText("testStatus").contains(status));
        });

        When("^I start an MOT test for <([^>]*)>, <([^>]*)>$", (String regKey, String vinKey) -> {
            startMotTest(driverWrapper.getData(regKey), driverWrapper.getData(vinKey));
        });
    }

    /**
     * Starts an MOT test for the specified vehicle. Refactored repeated cucumber steps, the original steps are
     * detailed below.
     * @param registration  The registration number to use
     * @param vin           The VIN to use
     */
    private void startMotTest(String registration, String vin) {
        // When I click the "Start MOT test" link
        driverWrapper.clickLink("Start MOT test");

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
        //And I click the "Select vehicle" link
        driverWrapper.clickLink("Select vehicle");

        //And The page title contains "Confirm vehicle and start test"
        driverWrapper.checkCurrentPageTitle("Confirm vehicle and start test");
        //And I press the "Confirm and start test" button
        driverWrapper.pressButton("Confirm and start test");

        //And The page title contains "MOT test started"
        driverWrapper.checkCurrentPageTitle("MOT test started");
        //And I click the "Home" link
        driverWrapper.clickLink("Home");
    }
}
