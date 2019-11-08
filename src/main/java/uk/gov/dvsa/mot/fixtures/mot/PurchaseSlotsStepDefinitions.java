package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.inject.Inject;

public class PurchaseSlotsStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(PurchaseSlotsStepDefinitions.class);

    /** The webdriver to use. */
    private WebDriverWrapper driverWrapper;

    /** The cost per slot. */
    private double slotCost = 2.05;

    /**
     * Create a new instance.
     * @param driverWrapper The web driver to be used
     */
    @Inject
    public PurchaseSlotsStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating PurchaseSlotsStepDefinitions");
        this.driverWrapper = driverWrapper;

        And("^I order (\\d+) slots$", (Integer slots) -> {
            orderSlots(slots);
        });

        And("^I enter card details from csv \"([^\"]+)\"$", (String csvName) -> {
            enterCardDetailsFromCsv(csvName);
        });

        And("^I enter the card holders name as \"([^\"]+)\"$", (String cardHolderName) -> {
            enterCardholdersName(cardHolderName);
        });

        And("^I make the payment for card from csv \"([^\"]+)\"$", (String csvName) -> {
            makePayment(csvName);
        });

        And("^I make an orphan payment for card from csv \"([^\"]+)\"$", (String csvName) -> {
            makePayment(csvName);
        });

        And("^I check that (\\d+) slots were bought successfully$", (Integer slots) -> {
            checkTheSummaryInformation(slots);
        });
    }

    /**
     * Enters the amount of slots to be purchased.
     * @param slots The number of slots to be purchased
     */
    private void orderSlots(int slots) {
        //Current slot balance from page
        int slotsBalance = Integer.parseInt(driverWrapper.getElementText("slot-count")
                .split("\n")[0].replace(",", ""));

        //Set the number of current slots in the driver
        driverWrapper.setData("slotsBalance", Integer.toString(slotsBalance));

        //Set the number of slots purchased
        driverWrapper.setData("slotsAdditon", Integer.toString(slots));

        //Set the new total of slots purchased
        driverWrapper.setData("slotsNewTotal", Integer.toString(slots + slotsBalance));

        //And I click the buy slots link
        driverWrapper.clickLink("Buy slots");

        //And I enter slots into the slots field
        driverWrapper.enterIntoField(String.valueOf(slots), "How many slots do you want to buy?");

        //And I press the calculate costs button
        driverWrapper.pressButton("Calculate cost");

        //And I check the order summary is correct
        String amount = driverWrapper.getTextFromTableRow("Amount ordered");
        String cost = driverWrapper.getTextFromTableRow("At a total cost of");
        DecimalFormat df = new DecimalFormat("#,###.00");

        assertTrue("The amount of slots ordered is incorrect", amount.contains(String.valueOf(slots)));
        assertTrue("The total cost is incorrect", cost.contains(df.format(slots * slotCost)));

        //And I click the continue link
        driverWrapper.clickLink("Continue");
    }

    /**
     * Enters the card details in the card payment screen with the data given in the csv.
     * @param csvName       The name of the CSV which will correspond to the CSV file name in resources
     */
    private void enterCardDetailsFromCsv(String csvName) {

        for (CSVRecord record: processCsvData(csvName)) {

            LocalDate expiryDate = LocalDate.now();
            expiryDate = expiryDate.plusYears(1);

            //And I check i am on the CPMS page
            driverWrapper.checkCurrentPageTitle("Customer Payment Management System");

            //And I enter the card number
            driverWrapper.enterIntoField(record.get(0), "Card Number*");

            //And I enter the expiry month
            driverWrapper.enterIntoFieldWithId("12", "scp_cardPage_expiryDate_input");

            //And I enter the expiry year
            driverWrapper.enterIntoFieldWithId(expiryDate.getYear(), "scp_cardPage_expiryDate_input2");

            //And I enter the security code of the card
            driverWrapper.enterIntoField(record.get(1), "Security Code*");

            //And I press the continue button
            driverWrapper.clickButton("Continue");
        }
    }

    /**
     * Processes a csv file to provide the data in a particular csv file.
     * @param csvFile the csv file to be processed
     * @return returns an array list of csv records
     */
    private ArrayList<CSVRecord> processCsvData(String csvFile) {
        ArrayList<CSVRecord> records = new ArrayList<>();

        File csvData = new File("src/main/resources/csv/" + csvFile + ".csv");

        try {
            CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.EXCEL);
            records.addAll(parser.getRecords());
        } catch (IOException exception) {
            logger.error("Unable to parse CSV file: " + exception.getMessage());
        }

        return records;
    }

    /**
     * Enters the cardholders name.
     * @param cardHolderName    The name to enter into the field
     */
    private void enterCardholdersName(String cardHolderName) {
        //And I enter the Cardholders name
        driverWrapper.enterIntoFieldWithId(cardHolderName, "scp_additionalInformationPage_cardholderName_input");

        //And I click the continue button
        driverWrapper.clickButton("Continue");
    }

    /**
     * Clicks the make payment button and enters the password for the card.
     * @param csvName    The csv name that contains card the number used in the payment
     */
    private void makePayment(String csvName) {
        //And I click the make payment button
        driverWrapper.clickButton("Make Payment");

        //Check the page title
        System.out.print(driverWrapper.getCurrentPageTitle());

        for (CSVRecord record: processCsvData(csvName)) {

            // If 3 D Secure Authorisation screen present fill it in
            if (driverWrapper.containsMessage("3 D Secure Authorisation")) {
                //Switch to password frame
                driverWrapper.switchToFrame("scp_threeDSecure_iframe");

                String cardNumber = record.get(0);

                String passwordPrefix = "Test_";
                String passwordSuffix = cardNumber.substring(cardNumber.length() - 4);

                String password = passwordPrefix.concat(passwordSuffix);

                //And I enter the password into the input
                driverWrapper.enterIntoField(password, "Password");

                //And I click the continue button
                driverWrapper.clickButton("Continue");
            }
        }
    }

    private void checkTheSummaryInformation(int slots) {
        //And I check the slots balance update successfully
        int newSlotTotal = Integer.valueOf(driverWrapper.getData("slotsBalance")) + slots;
        assertTrue("The slot balance total is incorrect",
                driverWrapper.getElementText("successMessage").contains(String.valueOf(newSlotTotal)));

        //And I check the slots are correct
        assertTrue("The slots purchased are incorrect",
                driverWrapper.getElementText("amountOrdered").contains(String.valueOf(slots)));

        //And I check the total cost is correct
        DecimalFormat df = new DecimalFormat("#,###.00");
        String totalCost = df.format(slots * slotCost);
        assertTrue("The total cost was incorrect",
                driverWrapper.getElementText("totalCost").contains(totalCost));
    }
}
