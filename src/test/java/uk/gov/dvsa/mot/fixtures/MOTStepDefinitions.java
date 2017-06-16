package uk.gov.dvsa.mot.fixtures;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.otp.Generator;

import javax.inject.Inject;

/**
 * Step definitions specific to MOT screens.
 *
 * Place any logic specific to the MOT application in this class rather than in the <code>WebDriverWrapper</code>.
 */
public class MOTStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(MOTStepDefinitions.class);

    @Inject
    public MOTStepDefinitions(WebDriverWrapper driverWrapper, Environment env) {
        logger.debug("Creating MOTStepDefinitions...");

        Given("^I login with 2FA as <([^>]*)>$", (String dataKey) -> {
            loginWith2FA(driverWrapper, driverWrapper.getData(dataKey),
                    env.getRequiredProperty("password"), env.getRequiredProperty("seed"));
        });

        When("^I enter <([^>]*)> plus (\\d+) in the odometer field$", (String dataKey, Integer amount) -> {
            int newMileage = Integer.parseInt(driverWrapper.getData(dataKey)) + amount;
            driverWrapper.enterIntoFieldWithId(String.valueOf(newMileage), "odometer");
        });
    }

    /**
     * Logs a user into the application, using 2FA (password and pin).
     * @oaram driverWrapper The web driver wrapper to use
     * @param username      The username to use
     * @param password      The password to use
     * @param seed          The OTP seed to use
     */
    private void loginWith2FA(WebDriverWrapper driverWrapper, String username, String password, String seed) {
        logger.debug("Logging in as username {} and password {}", username, password);
        driverWrapper.browseTo("/login");

        // the visible versions of the username and password fields have dynamic ids ending in _tid1 or _tid2
        driverWrapper.enterIntoFieldWithIdSuffix(username, "_tid1");
        driverWrapper.enterIntoFieldWithIdSuffix(password, "_tid2");
        driverWrapper.pressButton("Sign in");

        // check we got to the 2FA PIN screen
        if (!(driverWrapper.getCurrentPageTitle().contains("Your security card PIN"))) {
            // password authentication must have failed
            String message = "password authentication failed for user: " + username;
            logger.error(message);
            throw new RuntimeException(message);
        }

        // seed taken from the test OTP generator, used on all test systems
        String pin = Generator.generatePin(seed);
        logger.debug("Using PIN {}", pin);

        driverWrapper.enterIntoField(pin, "Security card PIN");
        driverWrapper.pressButton("Sign in");

        // check we got to the home page
        if (driverWrapper.getCurrentPageTitle().contains("Your security card PIN")) {
            // 2FA PIN authentication failed
            String message = "2FA PIN authentication failed for user: " + username;
            logger.error(message);
            throw new RuntimeException(message);
        }
    }
}
