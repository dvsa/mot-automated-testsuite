package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.PendingException;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;


/**
 * Step definitions for duplicate and replacement certificates in the MOT application.
 */
public class DuplicateAndReplacementStepDefinitions implements En {

    /** Logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DuplicateAndReplacementStepDefinitions.class);

    /** Webdriver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public DuplicateAndReplacementStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating DuplicateAndReplacementStepDefinitions");
        this.driverWrapper = driverWrapper;

        And("^I search for certificates with reg \\{([^\\}]+)\\}$", (String regKey) -> {
            searchForCertificateWithReg(driverWrapper.getData(regKey));
        });

        And("^I search for certificates with vin \\{([^\\}]+)\\}$", (String vinKey) -> {
            searchForCertificateWithVin(driverWrapper.getData(vinKey));
        });

        And("^I update the odometer reading by (\\d+)$", (Integer addToOdometer) -> {
            updateOdometerReading(addToOdometer);
        });

        And("^I update the testing location to \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String testingLocationKey, String siteNameKey) -> {
                    updateTestingLocation(driverWrapper.getData(testingLocationKey),
                            driverWrapper.getData(siteNameKey));
            });

        And("^I update the expiry date by adding (\\d+) days$", (Integer daysToAdd) -> {
            updateExpiryDate(daysToAdd);
        });

        And("^I submit the certificate changes$", () -> {
            addReasonForReplacementAndSubmit();
        });

        And("^I check the odometer reading on the confirmation page is correct$", () -> {
            checkOdometerReadingOnConfirmationPage();
        });

        And("^I check the expiry date of the confirmation page is correct$", () -> {
            checkExpiryDateOnConfirmationPage();
        });

        And("^I check the vts information appears on the confirmation page$", () -> {
            checkTheTestingLocation();
        });

        And("^I edit the make \"([^\"]+)\" and model \"([^\"]+)\"", (String make, String model) -> {
            editCertificateMakeAndModel(make, model);
        });

        And("^I edit the primary colour \"([^\"]+)\" and secondary colour \"([^\"]+)\"$",
                (String primaryColour, String secondaryColour) -> {
                    editCertificateColours(primaryColour, secondaryColour);
                });

        And("^I edit the vehicle vin with \"([^\"]+)\"$", (String vin) -> {
            editCertificateVin(vin);
        });

        And("^I edit the vehicle registration with \"([^\"]+)\"$", (String registration) -> {
            editCertificateRegistration(registration);
        });

        And("^I edit the country of registration with \"([^\"]+)\"$", (String countryOfRegistration) -> {
            editCountryOfRegistration(countryOfRegistration);
        });

        And("^I check the registration on the confirmation page is \"([^\"]+)\"$", (String registration) -> {
            assertTrue("The registration is not as expected",
                    driverWrapper.getElementText("registrationNumber").contains(registration));
        });

        And("^I check the vin on the confirmation page is \"([^\"]+)\"$", (String vin) -> {
            assertTrue("The vin is not as expected",
                    driverWrapper.getElementText("vinChassisNumber").contains(vin));
        });

        And("^I check the make on the confirmation page is \"([^\"]+)\"$", (String make) -> {
            assertTrue("The make is not as expected", driverWrapper.getElementText("make").contains(make));
        });

        And("^I check the model on the confirmation page is \"([^\"]+)\"$", (String model) -> {
            assertTrue("The model is not as expected",
                    driverWrapper.getElementText("model").contains(model));
        });

        And("^I check the colours are correct \"([^\"]+)\" and \"([^\"]+)\"$",
                (String primaryColour, String secondaryColour) -> {
                    checkColoursOnConfirmationPage(primaryColour, secondaryColour);
                });
        And("^I check the PDF certificate contains correct data$", () -> {
            checkIfPdfMatchesWebpage();
        });
    }

    /**
     * Search for certificates by registration.
     * @param reg   The registration marked used in the search
     */
    private void searchForCertificateWithReg(String reg) {
        //Click the Duplicate/replacement certificate link
        driverWrapper.clickLink("Replacement/duplicate certificate");

        //Enter the registration into the input
        driverWrapper.enterIntoField(reg, "Registration mark");

        //Press the search button
        driverWrapper.clickButton("Search");
    }

    /**
     * Search for certificates by vin.
     * @param vin   The vin used in the search
     */
    private void searchForCertificateWithVin(String vin) {
        //Click the Duplicate/replacement certificate link
        driverWrapper.clickLink("Replacement/duplicate certificate");

        //Click the search by vin link
        driverWrapper.clickLink("Search by VIN");

        //Enter the vin into the search field
        driverWrapper.enterIntoField(vin, "VIN");

        //Press the search button
        driverWrapper.clickButton("Search");
    }

    /**
     * Updates the odometer value by adding to it.
     * @param odometerAdd   the amount to add to the current odometer value
     */
    private void updateOdometerReading(int odometerAdd) {
        //Click the edit button for odometer reading
        driverWrapper.clickElement("dashboard-section-toggler-odometer");

        //Get the current odometer value
        String odometer = driverWrapper.getElementText("dashboard-section-header-value-odometer");
        int odometerValue = Integer.valueOf(odometer.split(" ")[0]) + odometerAdd;

        //Enter the new value
        driverWrapper.enterIntoFieldWithId(String.valueOf(odometerValue), "odometer");

        //Submit the new value
        driverWrapper.clickElement("section-odometer-submit");

        //Check the update value
        String newOdometer = driverWrapper.getElementText("dashboard-section-header-value-odometer");
        assertTrue("The odometer reading did not update succesfully",
                Integer.valueOf(newOdometer.split(" ")[0]) == odometerValue);

        //Add the new odometer reading to check later
        driverWrapper.setData("newOdometerReading", String.valueOf(newOdometer));
    }

    /**
     * Updates the testing location in the replacement certificate page.
     * @param newLocation   The new location to be changed on the replacement certificate
     * @param locationName  The new locations name
     */
    private void updateTestingLocation(String newLocation, String locationName) {
        //Click the testing location edit button
        driverWrapper.clickElement("dashboard-section-toggler-vts");

        //Enter the new testing location
        driverWrapper.enterIntoFieldWithId(newLocation,"input-vts");

        //Submit the new location value
        driverWrapper.clickElement("section-vts-submit");

        //Check the location updated successfully
        String confirmLocation = driverWrapper.getElementText("dashboard-section-header-value-vts");
        assertTrue("The location did not update successfully", confirmLocation.contains(locationName));
    }

    /**
     * Updates the expiry date by adding to the current expiry date.
     * @param daysToAdd The number of days to add to the current expiry date
     */
    private void updateExpiryDate(int daysToAdd) {
        //click the edit button
        driverWrapper.clickElement("dashboard-section-toggler-expiryDate");

        //Get the current expiry date
        String dateText = driverWrapper.getElementText("dashboard-section-header-value-expiryDate");
        logger.debug("Current expiry date is {}", dateText);

        //Create a formatter to match the application output
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMMM yyyy");
        LocalDate date = LocalDate.parse(dateText, df);
        logger.debug("Parsed date is {}", date.format(df));

        //Add the number of days to the date
        date = date.plusDays(daysToAdd);
        logger.debug("Updated date is {}", date.format(df));

        //And I enter the new expiry date
        driverWrapper.enterIntoFieldWithId(String.valueOf(date.getDayOfMonth()), "expiryDateDay");
        driverWrapper.enterIntoFieldWithId(String.valueOf(date.getMonthValue()), "expiryDateMonth");
        driverWrapper.enterIntoFieldWithId(String.valueOf(date.getYear()), "expiryDateYear");

        //And I click the submit button
        driverWrapper.clickElement("section-expiryDate-submit");

        //And I check the new expiry date is correct
        String newDateText = driverWrapper.getElementText("dashboard-section-header-value-expiryDate");
        logger.debug("New expiry date from screen is {}", newDateText);
        assertTrue("The expiry date did not update correctly", newDateText.equals(date.format(df)));

        //Add expiry date to the driver to check later
        driverWrapper.setData("expiryDate", date.format(df));
    }

    /**
     * Adds a reason for replacement comment and submits all the changes.
     */
    private void addReasonForReplacementAndSubmit() {
        //And I enter the reason for replacement
        driverWrapper.enterIntoFieldWithId("Generic Replacement Comment", "input-reason-for-replacement");

        //And I click the review changes button
        driverWrapper.pressButton("Review changes");

        //And I check the page title to ensure the form submitted
        driverWrapper.checkCurrentPageTitle("Replacement certificate review");
    }

    /**
     * Checks the odometer reading on the replacement confirmation page.
     */
    private void checkOdometerReadingOnConfirmationPage() {
        //Get the confirmation page odometer value
        String confirmationOdometer = driverWrapper.getElementText("odometerReading");

        //And I check the odometer reading
        assertTrue("The odometer on the confirmation page is incorrect",
                driverWrapper.getData("newOdometerReading").contains(confirmationOdometer));
    }

    /**
     * Checks the expiry date on the confirmation page.
     */
    private void checkExpiryDateOnConfirmationPage() {
        //Get the date from the page
        String confirmationExpiryDate = driverWrapper.getElementText("expiryDate");

        //Check the expiry date on the page
        assertTrue("The expiry date is not correct on the confirmation page",
                driverWrapper.getData("expiryDate").contains(confirmationExpiryDate));
    }

    /**
     * Checks the update site information appears on the page.
     */
    private void checkTheTestingLocation() {
        System.out.print(driverWrapper.getElementText("testInformation"));

        //And I check the testing location number is on the confirmation page
        assertTrue("Testing location number does not appear on the confirmation page",
                driverWrapper.getElementText("testInformation").contains(driverWrapper.getData("siteNumber")));

        //And I check the testing location name is on the confirmation page
        assertTrue("Testing Location name does not appear on the confirmation page",
                driverWrapper.getElementText("testInformation").contains(driverWrapper.getData("siteName")));
    }

    /**
     * This will edit the make and model of a vehicle on a certificate replacement.
     * @param make  The make to be used
     * @param model The model to be used
     */
    private void editCertificateMakeAndModel(String make, String model) {
        //Check if make or model is other
        if (make.equals("Other") || model.equals("Other")) {
            //And I press the edit button
            driverWrapper.clickElement("dashboard-section-toggler-make");

            //And i enter the make as other
            driverWrapper.selectOptionInFieldByName("Other", "make");

            //And I submit the new make
            driverWrapper.clickElement("section-make-submit");

            //And I enter a custom make
            driverWrapper.enterIntoFieldWithId("CUSTOM MAKE", "input-make");

            //And I enter a custom model
            driverWrapper.enterIntoFieldWithId("CUSTOM MODEL", "input-model");

            //And I press the Change make and model button
            driverWrapper.pressButton("Change make and model");
        } else {
            //And I press the edit button
            driverWrapper.clickElement("dashboard-section-toggler-make");

            //And I enter the make
            driverWrapper.selectOptionInFieldByName(make, "make");

            //And I submit the new make
            driverWrapper.clickElement("section-make-submit");

            //And I press the edit button
            driverWrapper.clickElement("dashboard-section-toggler-model");

            //And I enter the model
            driverWrapper.selectOptionInFieldByName(model, "model");

            //And I submit the new model
            driverWrapper.clickElement("section-model-submit");
        }
    }

    /**
     * Edits the primary and secondary colour on the replacement certificate.
     * @param primaryColour     The primary colour to be used
     * @param secondaryColour   The secondary colour to be used
     */
    private void editCertificateColours(String primaryColour, String secondaryColour) {
        //And I click the edit button for colours
        driverWrapper.clickElement("dashboard-section-toggler-vehicle-colour");

        //And I select the primary colour
        driverWrapper.selectOptionInFieldByName(primaryColour, "primaryColour");

        //And I select the secondary colour
        driverWrapper.selectOptionInFieldByName(secondaryColour, "secondaryColour");

        //And I click the submit button
        driverWrapper.clickElement("section-vehicle-colour-submit");
    }

    /**
     * Edits the vehicle vin on replacement certificate.
     * @param vin   The vin to be used
     */
    private void editCertificateVin(String vin) {
        //And I click the edit button for vin
        driverWrapper.clickElement("dashboard-section-toggler-vin");

        //And I enter the vin
        driverWrapper.enterIntoFieldWithId(vin, "input-vin");

        //And I submit the new vin
        driverWrapper.clickElement("section-vin-submit");
    }

    /**
     * Edits the vehicle registration on replacement certificate.
     * @param registration  The registration to be used
     */
    private void editCertificateRegistration(String registration) {
        //And I click the edit button for registration
        driverWrapper.clickElement("dashboard-section-toggler-vrm");

        //And I enter the registration
        driverWrapper.enterIntoFieldWithId(registration, "input-vrm");

        //And I submit the new registration
        driverWrapper.clickElement("section-vrm-submit");
    }

    /**
     * Edits the country of registration on replacement certificate.
     * @param countryOfRegistration The country of registration to be used
     */
    private void editCountryOfRegistration(String countryOfRegistration) {
        //And I click the edit button for country of registration
        driverWrapper.clickElement("dashboard-section-toggler-cor");

        //And I select the country of registration
        driverWrapper.selectOptionInFieldByName(countryOfRegistration, "cor");

        //And I submit the new country of registration
        driverWrapper.clickElement("section-cor-submit");
    }

    /**
     * Checks the colour is correct on the confirmation page.
     * @param primaryColour     The primary colour to check
     * @param secondaryColour   The secondary colour to check
     */
    private void checkColoursOnConfirmationPage(String primaryColour, String secondaryColour) {
        String colourString;
        //Check if either colour is "No other colour"
        if (primaryColour.equals("No other colour")) {
            colourString = secondaryColour;
        } else if (secondaryColour.equals("No other colour")) {
            colourString = primaryColour;
        } else {
            colourString = primaryColour + " and " + secondaryColour;
        }

        //Check the colours appear correctly
        assertTrue("The vehicle colours are not as expected",
                driverWrapper.getElementText("colour").contains(colourString));
    }

    private void checkIfPdfMatchesWebpage() {
        String url = driverWrapper.getAttribute("reprint-certificate", "href");

        logger.debug(url);

        String pdfText = driverWrapper.parsePdfFromUrl(url);
        logger.debug(pdfText);
    }
}
