package uk.gov.dvsa.mot.fixtures.generic;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.inject.Inject;

/**
 * Step definitions for web test steps.
 */
public class B2BStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(B2BStepDefinitions.class);

    /** The current JSON response. */
    private JSONArray response;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param env               The environmental variables
     */
    @Inject
    public B2BStepDefinitions(WebDriverWrapper driverWrapper, Environment env) {

        logger.debug("Creating B2BStepDefinitions...");

        Given("^I invoke the b2b endpoint with registration \\{([^\\}]+)\\} and make \\{([^\\}]+)\\}$", (
                String registration, String model) -> {
            invokeB2b(env, driverWrapper.getData(registration), driverWrapper.getData(model));
        });

        Then("^The first MOT result contains \"([^\"]+)\" equals \\{([^\\}]+)\\}$", (String name, String value) -> {
            checkResultContains(0, name, driverWrapper.getData(value));
        });

        Then("^Any MOT result contains \"([^\"]+)\" equals \\{([^\\}]+)\\}$", (String name, String value) -> {
            checkAnyResultContains(name, driverWrapper.getData(value));
        });
    }

    /**
     * Checks whether the named field contains the expected value.
     * @param name      field name
     * @param value     field value
     */
    private void checkAnyResultContains(String name, String value) {
        boolean resultFound = false;
        try {
            for (int i = 0; i < response.length(); i++) {
                if (value.equals(response.getJSONObject(i).getString(name))) {
                    //Result found we can stop now
                    resultFound = true;
                    break;
                }
            }

            assertTrue("Expected result not found in any result " + name + ":" + value, resultFound);
        } catch (JSONException ex) {
            logger.error("Error checking response contains " + name + ":" + value, ex);
            throw new RuntimeException("Error checking response contains " + name + ":" + value, ex);
        }
    }

    /**
     * Checks whether the named field contains the expected value.
     * @param index     the index of the results to check (typically 0 which is the first most recent test)
     * @param name      field name
     * @param value     field value
     */
    private void checkResultContains(int index, String name, String value) {
        try {
            assertTrue("B2B response does not contain expected value " + name + ":" + value,
                    value.equals(response.getJSONObject(index).getString(name)));
        } catch (JSONException ex) {
            logger.error("Error checking result contains " + name + ":" + value, ex);
            throw new RuntimeException("Error checking result contains " + name + ":" + value, ex);
        }
    }

    /**
     * Invokes the B2B interface.
     * @param registration  The registration
     * @param make          The make
     */
    private void invokeB2b(Environment env, String registration, String make) {
        //Reset the response just in case
        response = null;

        String b2bUrl = env.getRequiredProperty("mothB2BUrl") + registration + "/" + make;

        try {
            URL url = new URL(b2bUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-Forwarded-For", "62.128.221.147");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String dataline;
            StringBuffer responseOutput = new StringBuffer();
            while ((dataline = br.readLine()) != null) {
                responseOutput.append(dataline);
            }

            logger.debug("Response: " + responseOutput.toString());
            response = new JSONArray(responseOutput.toString());

            conn.disconnect();

        } catch (MalformedURLException ex) {
            logger.error("MalformedURLException", ex);
            throw new RuntimeException("MalformedURLException", ex);
        } catch (IOException ex) {
            logger.error("IOException", ex);
            throw new RuntimeException("IOException", ex);
        } catch (JSONException ex) {
            logger.error("JSONException", ex);
            throw new RuntimeException("JSONException", ex);
        }
    }
}
