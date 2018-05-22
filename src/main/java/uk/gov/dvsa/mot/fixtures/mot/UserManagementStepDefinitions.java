package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.data.DataProvider;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Step definitions for the various user registration/password management features.
 */
public class UserManagementStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(UserManagementStepDefinitions.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /** The data provider to use. */
    private final DataProvider dataProvider;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param dataProvider      The data provider to use
     */
    public UserManagementStepDefinitions(WebDriverWrapper driverWrapper, DataProvider dataProvider) {
        this.driverWrapper = driverWrapper;
        this.dataProvider = dataProvider;

        When("^I generate a unique email address as \\{([^\\}]+)\\}$", (String emailKeyName) ->
                generateUniqueEmail(emailKeyName));

        When("^I enter the generated username in the \"([^\"]+)\" field$", (String label) ->
                enterGeneratedUsernameIntoField(label));
    }

    /**
     * Generates a unique email address of the form <code>success+test123456@simulator.amazonses.com</code>,
     * by loading the maximum previously entered email (if any) and adding 1 to the number.
     * @param emailKeyName      The data key to set with the generated value
     */
    private void generateUniqueEmail(String emailKeyName) {
        // load the maximum previously entered email (or a default starting at "000000")
        List<String> results = dataProvider.getUncachedDatasetEntry("LAST_USED_TEST_EMAIL");
        if (results.size() != 1) {
            String message = "Expected LAST_USED_TEST_EMAIL to return 1 result, but received: " + results.size();
            logger.error(message);
            throw new IllegalStateException(message);
        }

        // extract the number from the email
        String previouslyUsedEmail = results.get(0);
        Pattern regex = Pattern.compile("^success\\+test(\\d{6})@simulator.amazonses\\.com$");
        Matcher matcher = regex.matcher(previouslyUsedEmail);
        if (matcher.matches()) {
            int previouslyUsedNumber = Integer.parseInt(matcher.group(1));

            // generate the new email address by adding 1...
            String newEmailAddress = "success+test" + String.format("%06d", previouslyUsedNumber + 1)
                    + "@simulator.amazonses.com";
            logger.debug("Using new email address: {}", newEmailAddress);
            driverWrapper.setData(emailKeyName, newEmailAddress);

        } else {
            String message = "Expected previously used test email to be of the form: "
                    + "success+test123456@simulator.amazonses.com, but received: " + previouslyUsedEmail;
            logger.error(message);
            throw new IllegalStateException(message);
        }
    }

    /**
     * Calculates the usename that will have been generated and fills in a given field with the value.
     * @param label The field label
     */
    private void enterGeneratedUsernameIntoField(String label) {
        Integer lastUsedSerial;
        String lastUsedUsername = driverWrapper.getData("lastuser");

        Pattern regex = Pattern.compile("^TEST(\\d+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(lastUsedUsername);

        if (matcher.matches()) {
            lastUsedSerial = Integer.parseInt(matcher.group(1), 10);
        } else {
            lastUsedSerial = 0;
        }

        String generatedUsername = "TEST" + String.format("%04d", lastUsedSerial + 1);

        driverWrapper.enterIntoField(generatedUsername, label);
    }
}
