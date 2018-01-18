package uk.gov.dvsa.mot.browserstack;

import com.browserstack.local.Local;
import cucumber.api.Scenario;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.utils.config.TestsuiteConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class to encapsulate BrowserStack related functionality.
 */
public class BrowserStackManager {
    /** Logger used by this class. **/
    private static final Logger logger = LoggerFactory.getLogger(BrowserStackManager.class);

    /** BrowserStackLocal instance used by the application. **/
    private static Local localBrowserStackInstance = new Local();

    /** Command-line parameters passed to BrowserStackLocal when starting and stopping it. **/
    private static HashMap<String, String> commandParameters = new HashMap<>();

    /**
     * Method to start the BrowserStackLocal instance.
     */
    public static void start() {
        TestsuiteConfig testsuiteConfig = initialiseConfig();

        setCommandParameters(testsuiteConfig);

        logger.debug(String.format("Starting BrowserStackLocal %s local instance...",
                testsuiteConfig.getProperty("localIdentifier")));

        try {
            localBrowserStackInstance.start(commandParameters);
        } catch (Exception exception) {
            logger.error(String.format("Unable to start BrowserStackLocal instance. Stacktrace:\n %s",
                    exception.toString()));
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * Method to stop the BrowserStackLocal instance.
     */
    public static void stop() {
        TestsuiteConfig testsuiteConfig = initialiseConfig();

        setCommandParameters(testsuiteConfig);

        logger.debug(String.format("Stopping BrowserStackLocal %s local instance...",
                testsuiteConfig.getProperty("localIdentifier")));

        try {
            localBrowserStackInstance.stop(commandParameters);
        } catch (Exception exception) {
            logger.error(String.format("Unable to stop BrowserStackLocal instance.\nExeption message:\n%s",
                    exception.getMessage()));
            throw new RuntimeException(exception.getMessage());
        }
    }

    /**
     * Get local identifier for the current test session.
     *
     * @return string representing unique local identifier for the session.
     */
    public static String getLocalIdentifier() {
        TestsuiteConfig testsuiteConfig = initialiseConfig();
        StringBuilder localIdentifier = new StringBuilder();

        if (testsuiteConfig.isMobileConfig()) {
            localIdentifier.append(testsuiteConfig.getProperty("device"));
            localIdentifier.append('_');
            localIdentifier.append(testsuiteConfig.getProperty("os_version"));
        } else {
            localIdentifier.append(testsuiteConfig.getProperty("os"));
            localIdentifier.append('_');
            localIdentifier.append(testsuiteConfig.getProperty("browser"));
            localIdentifier.append('_');
            localIdentifier.append(testsuiteConfig.getProperty("browser_version"));
        }

        return swapSpaces(localIdentifier.toString());
    }

    /**
     * Swap spaces with hyphens.
     *
     * @param value to remove spaces from.
     * @return processed value.
     */
    private static String swapSpaces(String value) {
        return value.replace(' ', '-');
    }

    /**
     * This method initialises the config for the BrowserStackLocal.
     */
    private static TestsuiteConfig initialiseConfig() {
        TestsuiteConfig temporaryConfig;

        String targetConfig = System.getProperty("target_config");
        String configuration = System.getProperty("configuration");

        if (targetConfig != null) {
            temporaryConfig = TestsuiteConfig.loadConfig("testsuite",
                    "browserstack",
                    targetConfig);
        } else if (configuration != null) {
            temporaryConfig = TestsuiteConfig.loadConfigFromString(configuration);
        } else {
            temporaryConfig = TestsuiteConfig.loadConfig("testsuite");
        }

        return temporaryConfig;
    }

    /**
     * This method sets all command parameters for running BrowserStackLocal.
     * @param env to use.
     */
    private static void setCommandParameters(TestsuiteConfig env) {
        commandParameters.put("key", env.getProperty("automateKey"));

        String localIdentifier = getLocalIdentifier();
        commandParameters.put("localIdentifier", localIdentifier);

        // NOTE: this argument forces the BrowserStackLocal to start even if there is an instance running with the same
        // config.
        commandParameters.put("force", "true");

        // NOTE: this argument forces the BrowserStack instance to tunnel all requests through BrowserStackLocal.
        commandParameters.put("forcelocal", "true");
    }

    /**
     * Notifies BrowserStack's REST api of scenario failure.
     *
     * <p>IMPORTANT NOTE: This only sends the failure, as the final status is used to determine test
     * failure/timeout/success. So if a scenario failed during the test, and the last status sent to BrowserStack is
     * success, it will still appear green on the website, even though the build contains failed test scenarios.</p>
     *
     * @param driverWrapper to get the session ID from.
     * @param scenario that just finished running.
     */
    public static void sendStatusToBrowserStack(WebDriverWrapper driverWrapper, Scenario scenario) {
        if (!scenario.isFailed()) {
            return;
        }

        try {
            SessionId session = driverWrapper.getSessionId();
            URI uri = new URI(
                    "https://"
                            + driverWrapper.getConfig().getProperty("username")
                            + ":"
                            + driverWrapper.getConfig().getProperty("automateKey")
                            + "@www.browserstack.com/automate/sessions/"
                            + session.toString() + ".json");

            HttpPut putRequest = new HttpPut(uri);

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add((new BasicNameValuePair("reason", scenario.getStatus())));
            nameValuePairs.add((new BasicNameValuePair("status", "failed")));

            putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpClientBuilder.create().build().execute(putRequest);
        } catch (URISyntaxException uriSyntax) {
            logger.error(String.format("An error occured while trying to create an URI: %s.",
                    uriSyntax.getMessage()));
        } catch (UnsupportedEncodingException unsupportedEncoding) {
            logger.error(String.format("Requested URI uses different encoding: %s.",
                    unsupportedEncoding.getMessage()));
        } catch (IOException io) {
            logger.error(String.format("Failed to execute the connection: %s.",
                    io.getMessage()));
        }
    }
}
