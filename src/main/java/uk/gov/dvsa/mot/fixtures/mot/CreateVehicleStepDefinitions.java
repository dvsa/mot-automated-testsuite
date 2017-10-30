package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;

/**
 * Step definitions for creating a vehicle in the MOT application.
 */
public class CreateVehicleStepDefinitions implements En {

    /** Logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(CreateVehicleStepDefinitions.class);

    /** Web driver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public CreateVehicleStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("CreatingVehicleStepDefinitions...");
        this.driverWrapper = driverWrapper;

        And("I enter the date of first use as today minus (\\d+) years", (Integer years) -> {
            enterDateOfFirstUse(years);
        });

        And("I select Vehicle Class (\\d+)", (Integer vehicleClass) -> {
            selectVehicleClass(vehicleClass);
        });

        And("I enter reg \"([^\"]+)\" and vin \"([^\"]+)\"$", (String reg, String vin) -> {
            enterNewVinandNewReg(reg,vin);
        });

        And("I select make \"([^\"]+)\" and model \"([^\"]+)\"$", (String make, String model) -> {
            selectMakeAndModel(make, model);
        });

        And("I select primary colour \"([^\"]+)\" and secondary colour \"([^\"]+)\"$",
                (String primaryColour, String secondaryColour) -> {
                    selectVehicleColours(primaryColour, secondaryColour);
            });

        And("I select fuel type \"([^\"]+)\" and cylinder capacity (\\d+)",
                (String fuelType, Integer cylinderCapacity) -> {
                    selectFuelType(fuelType, cylinderCapacity);
            });

        And( "I select country of registration \"([^\"]+)\"$", (String countryOfRegistration) -> {
            selectCountryOfRegistration(countryOfRegistration);
        });

        //After creating the vehicle we cancel the mot test to keep the environment clean
        And("I cancel the mot test after creating the vehicle", () -> {
            //And I click the "Continue to home" link
            driverWrapper.clickLink("Continue to home");

            //And I click the "Enter test results" link
            driverWrapper.clickLink("Enter test results");

            //And I click the "Cancel test" link
            driverWrapper.clickLink("Cancel test");

            //And I select "Vehicle registered in error" radio button
            driverWrapper.clickElement("reasonForCancel28");

            //And I press the "Confirm and cancel test" button
            driverWrapper.pressButton("Cancel test");

            //Check the page title contains "MOT test aborted"
            driverWrapper.checkCurrentPageTitle("MOT test aborted");
        });

        And("I check the registration \"([^\"]+)\" and vin \"([^\"]+)\" is correct$",
                (String reg, String vin) -> {
                    checkConfirmationVinAndReg(reg, vin);
            });

        And("I check the make \"([^\"]+)\" and model \"([^\"]+)\" is correct$", (String make, String model) -> {
            checkConfirmationMakeAndModel(make, model);
        });

        And("I check the fuel type \"([^\"]+)\" and cylinder capacity (\\d+) is correct$",
                (String fuelType, Integer cylinderCapacity) -> {
                    checkFueltypeAndCylinderCapacity(fuelType, cylinderCapacity);
            });

        And("I check the vehicle class (\\d+) is correct", (Integer vehicleClass) -> {
            assertTrue("The vehicle class is incorrect",
                    driverWrapper.getTextFromTableRow("Test class").contains(String.valueOf(vehicleClass)));
        });

        And("I check the primary colour \"([^\"]+)\" and secondary colour \"([^\"]+)\" is correct$",
                (String primaryColour, String secondaryColour) -> {
                    checkVehicleColour(primaryColour, secondaryColour);
            });

        And("I check the date of first use from (\\d+) years ago is correct", (Integer years) -> {
            checkDateOfFirstUse(years);
        });

        And("I check the country of registration \"([^\"]+)\" is correct$", (String countryOfReg) -> {
            assertTrue("The country of registration is incorrect",
                     driverWrapper.getTextFromTableRow("Country").contains(countryOfReg));
        });
    }

    /**
     * Enters a first use date of today but removes years based on a parameter.
     * @param years number of years to deduct from the date today
     */
    private void enterDateOfFirstUse(int years) {
        //Create a java time object to supply the data
        LocalDate date = LocalDate.now().minusYears(years);

        //Check the page title contains "What is the vehicle's date of first use?"
        driverWrapper.checkCurrentPageTitle("What is the vehicle's date of first use?");

        //And I enter the day as today
        driverWrapper.enterIntoField(String.valueOf(date.getDayOfMonth()), "Day");

        //And I enter the month as this month
        driverWrapper.enterIntoField(String.valueOf(date.getMonthValue()), "Month");

        //And I enter the year minus <years> years
        driverWrapper.enterIntoField(String.valueOf(date.getYear()), "Year");

        //And I press the continue button
        driverWrapper.pressButton("Continue");
    }

    /**
     * This will select the make and model of the new vehicle.
     * @param make  The make fo the new vehicle
     * @param model The model of the new vehicle
     */
    private void selectMakeAndModel(String make, String model) {
        //Check the page title contains "What is the vehicle's make?"
        driverWrapper.checkCurrentPageTitle("What is the vehicle's make?");

        //And I select <make> in the "Make" field
        driverWrapper.selectOptionInField(make, "Make");

        //If make is other, enter a new make
        if (make.equals("Other")) {
            //And I enter the make details
            driverWrapper.enterTextInFieldWithName("Other", make);
        }

        //And I press the "Continue" button
        driverWrapper.pressButton("Continue");

        //Check the page title contains "What is the vehicle's model?"
        driverWrapper.checkCurrentPageTitle("What is the vehicle's model?");

        //And I select <model> in the "Model" field
        driverWrapper.selectOptionInField(model, "Model");

        //If model is other, enter a new make
        if (model.equals("Other")) {
            //And I enter the model details
            driverWrapper.enterTextInFieldWithName("Other", model);
        }

        //And I press the "Continue" button
        driverWrapper.pressButton("Continue");
    }

    /**
     * Selects the fuel type and enters the cylinderCapacity of required.
     * @param fuelType          The fuel type of the new vehicle
     * @param cylinderCapacity  The cylinder capacity of the new vehicle
     */
    private void selectFuelType(String fuelType, int cylinderCapacity) {
        //Check th page title contains "Engine and fuel type"
        driverWrapper.checkCurrentPageTitle("Engine and fuel type");

        //And I select <fuelType> in the "Fuel Type" field
        driverWrapper.selectOptionInField(fuelType, "Fuel type");

        //if the cylinder capacity is greater than 0 enter the value
        if (cylinderCapacity > 0) {
            //And I enter <cylinderCapacity> in the "Cylinder Capacity" field
            driverWrapper.enterIntoFieldWithId(String.valueOf(cylinderCapacity), "cylinder-capacity-input");
        }

        //And I press the "Continue" button
        driverWrapper.pressButton("Continue");
    }

    /**
     * Select the vehicle class radio button.
     * @param vehicleClass  The vehicle class to select
     */
    private void selectVehicleClass(int vehicleClass) {
        //Check the page title contains "What is the vehicle's test class?"
        driverWrapper.checkCurrentPageTitle("What is the vehicle's test class?");

        //And I select the Class <vehicleClass> radio button
        driverWrapper.clickElement(String.format("testClass%d", vehicleClass));

        //And I press the "Continue" button
        driverWrapper.pressButton("Continue");
    }

    /**
     * Selects the primary colour and secondary colour of the vehicle.
     * @param primaryColour     The primary colour of the new vehicle
     * @param secondaryColour   The Secondary colour of the new vehicle
     */
    private void selectVehicleColours(String primaryColour, String secondaryColour) {
        //Check the page title contains "What is the vehicle's colour?"
        driverWrapper.checkCurrentPageTitle("What is the vehicle's colour?");

        //And I select <primaryColour> in the "Primary Colour" field
        driverWrapper.selectOptionInField(primaryColour, "Primary colour");

        //And I select <secondaryColour> in the "Secondary colour" field
        driverWrapper.selectOptionInFieldByName(secondaryColour, "secondaryColours");

        //And I press the "Continue" button
        driverWrapper.pressButton("Continue");
    }

    /**
     * Selects the new vehicles country of registration.
     * @param countryOfRegistration The new vehicles country of registration
     */
    private void selectCountryOfRegistration(String countryOfRegistration) {
        //Check the page title contains "What is the vehicle's country of registration?"
        driverWrapper.checkCurrentPageTitle("What is the vehicle's country of registration?");

        //And I select <countryOfRegistration> in the "Country of Registration" field
        driverWrapper.selectOptionInField(countryOfRegistration, "Country of registration");

        //And I press the "continue" button
        driverWrapper.pressButton("Continue");
    }

    /**
     * Enters the registration and vin number for the new vehicle.
     * @param reg the registration number of the new vehicle
     * @param vin the Vin number of the new vehicle
     */
    private void enterNewVinandNewReg(String reg, String vin) {
        //Check the page title contains "What are the vehicle's registration mark and VIN?"
        driverWrapper.checkCurrentPageTitle("What are the vehicle's registration mark and VIN?");

        //If the registration is empty click the cannot provide registration checkbox checkbox
        if (reg.equals("Not provided")) {
            //Click the checkbox for leaving the registration mark blank
            driverWrapper.clickElement("leavingRegBlank");
        } else {
            //And I enter <reg> in the "Registration mark" field
            driverWrapper.enterIntoField(reg, "Registration mark");
        }

        //If the registration is empty click the cannot provide vin checkbox
        if (vin.equals("Not provided")) {
            //Click the checkbox for leaving the vin blank
            driverWrapper.clickElement("leavingVINBlank");
        } else {
            //And I enter <vin> in the "VIN" field
            driverWrapper.enterIntoField(vin, "VIN");
        }

        //And I press the "Continue" button
        driverWrapper.pressButton("Continue");
    }

    /**
     * Check that the Reg and vin entered when creating the vehicle are the same on the confirmation page.
     * @param reg   The registration used when creating the vehicle
     * @param vin   The vin used when creating the vehicle
     */
    private void checkConfirmationVinAndReg(String reg, String vin) {
        //And I check that the reg is correct for the details entered
        assertTrue("The registration is incorrect",
                driverWrapper.getTextFromTableRow("Registration mark").contains(reg));

        //And I check the vin is correct for the details entered
        assertTrue("The vin is incorrect",
                driverWrapper.getTextFromTableRow("Registration mark").contains(vin));
    }

    /**
     * Check that the make and model entered when creating the vehicle are the same on the confirmation page.
     * @param make      The make used when creating the vehicle
     * @param model     The model used when creating the vehicle
     */
    private void checkConfirmationMakeAndModel(String make, String model) {
        //And I check the make of the vehicle
        assertTrue("The make is incorrect", driverWrapper.getTextFromTableRow("Make").contains(make));

        //And I check the model of the vehicle
        assertTrue("The model is incorrect",
                driverWrapper.getTextFromTableRow("model").contains(model));
    }

    /**
     * Check that the fuel type and cylinder capacity when creating the vehicle are the same on the confirmation page.
     * @param fuelType          The fuel type used when creating the vehicle
     * @param cylinderCapacity  The cylinder capacity of the vehicle
     */
    private void checkFueltypeAndCylinderCapacity(String fuelType, int cylinderCapacity) {
        //And i check the fuel type of the vehicle
        assertTrue("The fuel type is incorrect",
                driverWrapper.getTextFromTableRow("fuel type").contains(fuelType));

        //Check the cylinder capacity only if it's greater than 0
        if (cylinderCapacity > 0) {
            //And I check the cylinder capacity of the vehicle
            assertTrue("The cylinder capacity is incorrect",
                    driverWrapper.getTextFromTableRow("Engine").contains(String.valueOf(cylinderCapacity)));
        }
    }

    /**
     * Checks the vehicle colours used when creating the vehicle are the same on the confirmation page.
     * @param primaryColour     The primary colour used when creating the vehicle
     * @param secondaryColour   The secondary colour used when creating the vehicle
     */
    private void checkVehicleColour(String primaryColour, String secondaryColour) {
        //And I check the primary colour of the vehicle
        assertTrue("The primary colour is incorrect",
                driverWrapper.getTextFromTableRow("Colour").contains(primaryColour));

        //Check if the secondary colour should appear
        if (!secondaryColour.equals("No other colour")) {
            //And I check the secondary colour of the vehicle
            assertTrue("The secondary colour is incorrect",
                    driverWrapper.getTextFromTableRow("Colour").contains(secondaryColour));
        }
    }

    /**
     * Deducts the same years used in the test to check the date of first use.
     * @param years     The years to deduct to get the year of first use
     */
    private void checkDateOfFirstUse(int years) {
        //Create the date instance and deduct the number of years
        LocalDate date = LocalDate.now().minusYears(years);

        //Create a date time formatter that the page produces
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMMM yyyy");

        //Format the date into a string to be compared
        String dateString = date.format(df);

        //And I check the date of first use is correct
        assertTrue("The date of first use is incorrect: " + dateString,
                driverWrapper.getTextFromTableRow("Date of first use").contains(dateString));

    }
}
