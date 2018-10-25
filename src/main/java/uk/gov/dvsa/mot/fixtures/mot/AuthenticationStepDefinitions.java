package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.data.DataProvider;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.otp.Generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

/**
 * Step definitions specific to authenticating within the MOT application.
 */
public class AuthenticationStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationStepDefinitions.class);

    /** The web driver to use. */
    private final WebDriverWrapper driverWrapper;

    /** The data provider to use. */
    private final DataProvider dataProvider;

    /** The login data set cached for use with login retries. */
    private List<List<String>> dataSet;

    /** Whether we are filtering data. */
    private final boolean isFilteringEnabled;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param dataProvider      The data provider to use
     * @param env               The config settings to use
     */
    @Inject
    public AuthenticationStepDefinitions(WebDriverWrapper driverWrapper, DataProvider dataProvider,
                                         Environment env) {
        logger.debug("Creating AuthenticationStepDefinitions...");
        this.driverWrapper = driverWrapper;
        this.dataProvider = dataProvider;
        this.isFilteringEnabled = Boolean.parseBoolean(env.getProperty("dataFiltering", "false"));
        logger.info("Filtering enabled: {}", isFilteringEnabled);

        Given("^I login with 2FA using \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String usernameKey, String key2) ->
                    loginWith2fa(dataSetName, usernameKey,
                            env.getRequiredProperty("password"), env.getRequiredProperty("seed"),
                            Optional.empty(), Optional.empty(),
                            env.getRequiredProperty("maxLoginRetries", Integer.class), key2));

        Given("^I login with 2FA using \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String usernameKey, String key2, String key3) ->
                    loginWith2fa(dataSetName, usernameKey,
                            env.getRequiredProperty("password"), env.getRequiredProperty("seed"),
                            Optional.empty(), Optional.empty(),
                            env.getRequiredProperty("maxLoginRetries", Integer.class), key2, key3));

        Given("^I login with 2FA using \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\},"
                + " \\{([^\\}]+)\\}$",
                (String dataSetName, String usernameKey, String key2, String key3, String key4) ->
                    loginWith2fa(dataSetName, usernameKey,
                            env.getRequiredProperty("password"), env.getRequiredProperty("seed"),
                            Optional.empty(), Optional.empty(),
                            env.getRequiredProperty("maxLoginRetries", Integer.class), key2, key3, key4));

        Given("^I login with 2FA using \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\},"
                        + " \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String usernameKey, String key2, String key3, String key4, String key5) ->
                    loginWith2fa(dataSetName, usernameKey,
                            env.getRequiredProperty("password"), env.getRequiredProperty("seed"),
                            Optional.empty(), Optional.empty(),
                            env.getRequiredProperty("maxLoginRetries", Integer.class), key2, key3, key4, key5));

        Given("^I login with 2FA using \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\},"
                        + " \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String usernameKey, String key2, String key3, String key4, String key5,
                 String key6) -> loginWith2fa(dataSetName, usernameKey,
                        env.getRequiredProperty("password"), env.getRequiredProperty("seed"),
                        Optional.empty(), Optional.empty(),
                        env.getRequiredProperty("maxLoginRetries", Integer.class), key2, key3, key4, key5, key6));

        Given("^I login with 2FA as \\{([^\\}]+)\\}$", (String usernameKey) ->
                loginWith2fa(usernameKey, env.getRequiredProperty("password"),
                    env.getRequiredProperty("seed")));

        Given("^I login without 2FA using \"([^\"]+)\" as \\{([^\\}]+)\\}$",
                (String dataSetName, String usernameKey) ->
                    loginWithout2fa(dataSetName, usernameKey, env.getRequiredProperty("password"),
                            env.getRequiredProperty("maxLoginRetries", Integer.class)));

        Given("^I login without 2FA as \\{([^\\}]+)\\}$", (String usernameKey) ->
                        loginWithout2fa(usernameKey, env.getRequiredProperty("password")));

        Given("^I login with 2FA and drift ([\\+|\\-]\\d+) using \"([^\"]+)\" "
                        + "as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String drift, String dataSetName, String usernameKey, String lastDriftKey, String key3, String key4) ->
                    loginWith2fa(dataSetName, usernameKey,
                            env.getRequiredProperty("password"), env.getRequiredProperty("seed"),
                            Optional.of(Integer.parseInt(drift)), Optional.of(lastDriftKey),
                            env.getRequiredProperty("maxLoginRetries", Integer.class), key3, key4));

        Given("^I login and click \"([^\"]+)\" card using \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String forgotOrLost, String dataSetName, String usernameKey, String key2, String key3, String key4) ->
                        loginAndClickForgottenCard(dataSetName, usernameKey, env.getRequiredProperty("password"),
                                env.getRequiredProperty("maxLoginRetries", Integer.class), forgotOrLost, key2,
                                key3, key4));

        Given("^I generate 2FA PIN with drift ([\\+|\\-]\\d+) as \\{([^\\}]+)\\}$",
                (String drift, String pinKey) ->
                    driverWrapper.setData(pinKey,
                        generatePin(env.getRequiredProperty("seed"), Integer.parseInt(drift), 0)));

        Given("^I generate 2FA PIN with previous drift \\{([^\\}]+)\\} as \\{([^\\}]+)\\}$",
                (String lastDriftKey, String pinKey) ->
                        driverWrapper.setData(pinKey,
                                generatePin(env.getRequiredProperty("seed"), 0,
                                        Integer.parseInt(driverWrapper.getData(lastDriftKey)))));
    }

    /** Encapsulates the possible results from the login journey. */
    private enum LoginOutcome {
        PasswordFailed, PasswordSuccessful, PinFailed, PinSuccessful;
    }

    /**
     * Logs a user into the application, using 2FA (password and pin).
     * <p>Note: doesn't tolerate failed logins caused by a user password being manually changed.</p>
     * @param usernameKey       The username data key to set
     * @param password          The password to use
     * @param seed              The OTP seed to use
     */
    private void loginWith2fa(String usernameKey, String password, String seed) {
        switch (handlePasswordAndPinScreens(
                driverWrapper.getData(usernameKey), password, seed, 0, 0)) {
            case PinSuccessful:
                // check if any special notices need clearing down
                if (driverWrapper.hasLink("Read and acknowledge")) {
                    clearDownSpecialNotices();
                }
                break;

            default:
                String message = "Login failed";
                logger.error(message);
                throw new IllegalStateException(message);
        }
    }

    /**
     * Logs a user into the application, using 2FA (password and pin).
     * <p>Handles failed logins (password or pin rejected) by trying again with another user.</p>
     * @param dataSetName       The data set to get users from
     * @param usernameKey       The username data key to set
     * @param password          The password to use
     * @param seed              The OTP seed to use
     * @param driftOffset       The drift period offset to use (defaults to +0)
     * @param lastDriftKey      The last drift period key to load then use, if any
     * @param maxLoginRetries   The number of times to retry login with a different user before failing the test
     * @param keys              The extra data keys to set
     */
    private void loginWith2fa(String dataSetName, String usernameKey, String password, String seed,
                              Optional<Integer> driftOffset, Optional<String> lastDriftKey,
                              int maxLoginRetries, String... keys) {
        int loginAttempts = 0;
        while (loginAttempts < maxLoginRetries) {
            loginAttempts++;

            // load username from the dataset, populate the data keys and values
            List<String> dataKeys = new ArrayList<>();
            dataKeys.add(usernameKey);
            lastDriftKey.ifPresent(dataKeys::add);
            Collections.addAll(dataKeys, keys);
            loadData(dataSetName, dataKeys, loginAttempts > 1, maxLoginRetries);

            // get the loaded username
            String username = driverWrapper.getData(usernameKey);

            // try to login
            switch (handlePasswordAndPinScreens(username, password, seed, driftOffset.orElse(0),
                    lastDriftKey
                            .map((key) -> Integer.parseInt(driverWrapper.getData(key)))
                            .orElse(0))) {
                case PinSuccessful:
                    // check if any special notices need clearing down
                    if (driverWrapper.hasLink("Read and acknowledge")) {
                        clearDownSpecialNotices();
                    }

                    // all successful
                    return;

                case PinFailed:
                    if (driftOffset.isPresent() || lastDriftKey.isPresent()) {
                        // if using drift settings then we return if PIN rejected
                        return;
                    } else {
                        // login (pin) failed, loop around to try again
                        break;
                    }

                default:
                    // login (password or pin) failed, loop around to try again
                    break;
            }
        }

        String message = "Login failed after trying " + loginAttempts + " users";
        logger.error(message);
        throw new IllegalStateException(message);
    }

    /**
     * Browses to the login screen, handles Login then the security Pin screen (if password accepted).
     * @param username      The username to use
     * @param password      The password to use
     * @param seed          The OTP seed to use
     * @param driftOffset   The drift offset to apply to the previous drift
     * @param lastDrift     The previous drift on last login
     * @return The outcome
     */
    private LoginOutcome handlePasswordAndPinScreens(String username, String password, String seed, int driftOffset,
            int lastDrift) {
        LoginOutcome outcome = handlePasswordScreen(username, password);

        if (outcome.equals(LoginOutcome.PasswordSuccessful)) {
            outcome = handlePinScreen(seed, driftOffset, lastDrift);
        }

        return outcome;
    }

    /**
     * Browses to the login screen (clears all cookies), enters the user name and password and submits the form.
     * @param username  The username to use
     * @param password  The password to use
     * @return The outcome
     */
    private LoginOutcome handlePasswordScreen(String username, String password) {
        return handlePasswordScreen(username, password, true);
    }

    /**
     * Browses to the login screen, enters the user name and password and submits the form.
     * @param username                  The username to use
     * @param password                  The password to use
     * @param browseAndClearCookies     Whether to clear all cookies and browse to the login screen (if not assumes
     *                                  we are already at the login screen)
     * @return The outcome
     */
    private LoginOutcome handlePasswordScreen(String username, String password, boolean browseAndClearCookies) {
        logger.debug("Logging in as username {} and password {}", username, password);

        if (browseAndClearCookies) {
            driverWrapper.browseTo("/login");
        }

        driverWrapper.enterIntoField(username, "User ID");
        driverWrapper.enterIntoField(password, "Password");
        driverWrapper.pressButton("Sign in");

        // message received after successful login if user has ordered new card, on page with title "Sign in"
        String orderCardMessage = "You have ordered a new card. Until you receive and activate the card, "
                + "sign in with your security questions.";

        // message for when the account is locked
        String accountNearlyLockedMessage = "You have tried to sign in 4 times";
        String accountLockedMessage = "Your account is locked";

        if (driverWrapper.containsMessage(accountNearlyLockedMessage)
                || driverWrapper.containsMessage(accountLockedMessage)) {
            //Account is locked or about to be locked
            return LoginOutcome.PasswordFailed;

        } else if (!driverWrapper.getCurrentPageTitle().contains("Sign in")
                || driverWrapper.containsMessage(orderCardMessage)) {
            return LoginOutcome.PasswordSuccessful;

        } else {
            // password rejected, still on the login screen
            return LoginOutcome.PasswordFailed;

        }
    }

    /**
     * Enters the OTP pin into the security PIN screen, and submits the form.
     * @param seed          The OTP seed to use
     * @param driftOffset   The drift offset to apply to the previous drift
     * @param lastDrift     The previous drift on last login
     * @return The outcome
     */
    private LoginOutcome handlePinScreen(String seed, int driftOffset, int lastDrift) {
        String pin = generatePin(seed, driftOffset, lastDrift);
        driverWrapper.enterIntoField(pin, "Security card PIN");
        driverWrapper.pressButton("Sign in");

        if (driverWrapper.getCurrentPageTitle().contains("Your security card PIN")) {
            // pin rejected, still on the security PIN screen
            return LoginOutcome.PinFailed;

        } else {
            return LoginOutcome.PinSuccessful;
        }
    }

    /**
     * Logins in as a user that does not require 2FA authentication.
     * <p>Handles failed logins (password rejected) by trying again with another user.</p>
     * @param dataSetName       The name of the data set to use
     * @param usernameKey       The key that the username is stored under
     * @param password          The password to use
     * @param maxLoginRetries   The max number of login retries
     */
    private void loginWithout2fa(String dataSetName, String usernameKey, String password, int maxLoginRetries)  {
        int loginAttempts = 0;
        while (loginAttempts < maxLoginRetries) {
            loginAttempts++;

            // load username from the dataset, populate the data keys and values
            List<String> dataKeys = new ArrayList<>();
            dataKeys.add(usernameKey);
            loadData(dataSetName, dataKeys, loginAttempts > 1, maxLoginRetries);

            // get the loaded username
            String username = driverWrapper.getData(usernameKey);

            // try to login
            switch (handlePasswordScreen(username, password)) {
                case PasswordFailed:
                    // login failed, loop around to try again
                    break;

                default:
                    // check if any special notices need clearing down
                    if (driverWrapper.hasLink("Read and acknowledge")) {
                        clearDownSpecialNotices();
                    }

                    // all successful
                    return;
            }
        }

        String message = "Login failed after trying " + loginAttempts + " users";
        logger.error(message);
        throw new IllegalStateException(message);
    }

    /**
     * Logs a user into the application, using password only.
     * <p>Note: doesn't tolerate failed logins caused by a user password being manually changed.</p>
     * @param usernameKey       The username data key to set
     * @param password          The password to use
     */
    private void loginWithout2fa(String usernameKey, String password) {
        switch (handlePasswordScreen(driverWrapper.getData(usernameKey), password, false)) {
            case PasswordSuccessful:
                return;

            default:
                String message = "Login failed";
                logger.error(message);
                throw new IllegalStateException(message);
        }
    }

    /**
     * Logs a user into the application, using password only, and clicks the forgotten card link.
     * <p>Handles failed logins (password rejected) by trying again with another user.</p>
     * @param dataSetName       The data set to get users from
     * @param usernameKey       The username data key to set
     * @param password          The password to use
     * @param maxLoginRetries   The number of times to retry login with a different user before failing the test
     * @param journey           The user journey being taken
     * @param keys              The extra data keys to set
     */
    private void loginAndClickForgottenCard(String dataSetName, String usernameKey, String password,
                                int maxLoginRetries, String journey, String... keys) {
        int loginAttempts = 0;
        while (loginAttempts < maxLoginRetries) {
            loginAttempts++;

            // load username from the dataset, populate the data keys and values
            List<String> dataKeys = new ArrayList<>();
            dataKeys.add(usernameKey);
            Collections.addAll(dataKeys, keys);
            loadData(dataSetName, dataKeys, loginAttempts > 1, maxLoginRetries);

            // get the loaded username
            String username = driverWrapper.getData(usernameKey);

            // try to login
            switch (handlePasswordScreen(username, password)) {
                case PasswordFailed:
                    // login failed, loop around to try again
                    break;

                default:

                    // selects the correct link for the 2fa card problem journeys
                    switch (journey) {
                        case "lost":
                            // And I click the "Lost or damaged security card" link
                            driverWrapper.clickLink("Lost or damaged security card");
                            break;
                        case "temporary":
                            // And I click the "Temporary sign in" link
                            driverWrapper.clickLink("Temporary sign in");
                            break;
                        default:
                            String message = "Unknown card issue journey " + journey;
                            logger.error(message);
                            throw new IllegalArgumentException(message);
                    }

                    // all successful
                    return;
            }
        }

        String message = "Login failed after trying " + loginAttempts + " users";
        logger.error(message);
        throw new IllegalStateException(message);
    }

    /**
     * Loads a data set, and populates the scenario test data, for the specified keys.
     * @param dataSetName       The name of the data set
     * @param keys              The keys to populate
     * @param isRetry           Whether this is a login retry, which needs to use cached data
     * @param maxLoginRetries   The number of times to retry login with a different user before failing the test
     */
    private void loadData(String dataSetName, List<String> keys, boolean isRetry, int maxLoginRetries) {
        List<String> entry;
        if (isFilteringEnabled) {
            /*
             * If filtering is enabled then we simply read from the cache as needed. The filtering will ensure each
             * test receives a different user, so data should not be invalidated...
             */
            entry = dataProvider.getCachedDatasetEntry(dataSetName);


        } else {
            if (!isRetry) {
                /*
                 * Load the data set immediately rather than using caching, because otherwise the data might be
                 * invalidated by the time we come to use it. For example, my dataset might be an application user
                 * with role <x>, and that same application user might be put through a test to remove role <x>
                 * before getting to this scenario...
                 */
                dataSet = dataProvider.getUncachedDataset(dataSetName, maxLoginRetries);

            } else if (dataSet.size() == 0) {
                String message = "No more data available to retry login, data set " + dataSetName
                        + " loaded less than " + maxLoginRetries + "entries";
                logger.error(message);
                throw new IllegalStateException(message);
            }

            // use the next entry in the data set
            entry = dataSet.get(0);
            dataSet.remove(0);
        }

        logger.debug("Using {} from dataset {}", entry, dataSetName);

        // check the number of items in the data set matches the number of keys in the test step
        assertEquals("Expected data set " + dataSetName + " to contain " + keys.size() + " data items, "
                        + "but it contained " + entry.size() + " data items. Please check your scenario",
                keys.size(), entry.size());

        for (int i = 0; i < keys.size(); i++) {
            driverWrapper.setData(keys.get(i), entry.get(i));
        }
    }

    /**
     * Clears down any special notices, then returns to the home page.
     */
    private void clearDownSpecialNotices() {
        logger.debug("Special notices need clearing down");

        driverWrapper.clickLink("Read and acknowledge");
        driverWrapper.checkCurrentPageTitle("Special Notices");

        // note that the label has two spaces - View--and!
        while (driverWrapper.hasLink(
                "span", "View  and acknowledge", "../../")) {
            logger.debug("Special notice found, acknowledging");

            driverWrapper.clickLink(
                    "span", "View  and acknowledge", "../../");
            driverWrapper.pressFirstButton("Acknowledge");
        }
        driverWrapper.clickLink("Back");

        // back on "Your home"
        driverWrapper.checkCurrentPageTitle("Your home");

        // check special notices warning has gone
        assertFalse("Expected special notices to be cleared down",
                driverWrapper.hasLink("Read and acknowledge"));
    }

    /**
     * Generate a 2FA PIN using the specified seed and drift (<code>lastDrift + driftString</code>).
     * @param seed          The OTP algorithm seed
     * @param driftOffset   The drift offset (e.g. <code>-2</code>) to apply to the last drift
     * @param lastDrift     The last drift that was used
     * @return The PIN
     */
    private String generatePin(String seed, int driftOffset, int lastDrift) {
        // timeoffset is current time plus drift period (each is 30 mins) converted to milliseconds
        long timeoffset = System.currentTimeMillis() + ((lastDrift + driftOffset) * 30 * 1000);

        String pin = Generator.generatePin(seed, timeoffset);
        logger.debug("Using PIN {} for drift offset {} and last drift {}", pin, driftOffset, lastDrift);

        return pin;
    }
}
