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

        And("^I update the odometer reading by (\\d+)", (Integer addToOdometer) -> {
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
    }

    /**
     * Search for certificates by registration.
     * @param reg   The registration marked used in the search
     */
    private void searchForCertificateWithReg(String reg) {
        //Click the Duplicate/replacement certificate link
        driverWrapper.clickLink("Replacement/duplicate certificate");

        //Check that we are on the replacement / duplicate certificate page
        //driverWrapper.checkCurrentPageTitle("Search by registration mark");

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

        //Create a formatter to match the application output
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMMM yyyy");
        LocalDate date = LocalDate.parse(dateText, df);

        //Add the number of days to the date
        date.plusDays(daysToAdd);

        //And I enter the new expiry date
        driverWrapper.enterIntoFieldWithId(String.valueOf(date.getDayOfMonth()), "expiryDateDay");
        driverWrapper.enterIntoFieldWithId(String.valueOf(date.getMonthValue()), "expiryDateMonth");
        driverWrapper.enterIntoFieldWithId(String.valueOf(date.getYear()), "expiryDateYear");

        //And I click the submit button
        driverWrapper.clickElement("section-expiryDate-submit");

        //And I check the new expiry date is correct
        String newDateText = driverWrapper.getElementText("dashboard-section-header-value-expiryDate");
        assertTrue("The expiry date did not update correctly", newDateText.equals(date.format(df).toString()));

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
}
