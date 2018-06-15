package uk.gov.dvsa.mot.fixtures.generic;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
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
    private JSONObject response;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param env               The environmental variables
     */
    @Inject
    public B2BStepDefinitions(WebDriverWrapper driverWrapper, Environment env) {

        logger.debug("Creating B2BStepDefinitions...");

        Given("^I invoke the b2b endpoint with registration \\{([^\\}]+)\\} and model \\{([^\\}]+)\\}$", (
                String registration, String model) -> {
            // Write code here that turns the phrase above into concrete actions
            invokeB2b(env, driverWrapper.getData(registration), driverWrapper.getData(model));
        });

        Then("^The response contains \"([^\"]+)\" equals \\{([^\\}]+)\\}$", (String name, String value) -> {
            // Write code here that turns the phrase above into concrete actions
            checkResponseContains(name, driverWrapper.getData(value));
        });

        Then("^The vehicle make and model is \\{([^\\}]+)\\}$", (String makeAndModel) -> {
            // Write code here that turns the phrase above into concrete actions
            checkResponseMakeAndModel(driverWrapper.getData(makeAndModel));
        });
    }

    /**
     * Checks whether the make and model match expected value.
     * @param value  the make and model separated by a space
     */
    private void checkResponseMakeAndModel(String value) {
        try {
            String[] makeAndModel = value.split(" ");

            assertTrue("B2B response for does not contain expected value " + makeAndModel[0],
                    makeAndModel[0].equals(response.getString("vehicle-make")));

            assertTrue("B2B response for does not contain expected value " + makeAndModel[1],
                    makeAndModel[1].equals(response.getString("vehicle-model")));
        } catch (JSONException ex) {
            throw new RuntimeException("Error checking make and model", ex);
        }
    }

    /**
     * Checks whether the named field contains the expected value.
     * @param name      field name
     * @param value     field value
     */
    private void checkResponseContains(String name, String value) {
        try {
            assertTrue("B2B response does not contain expected value " + name + ":" + value,
                    value.equals(response.getString(name)));
        } catch (JSONException ex) {
            throw new RuntimeException("Error checking response value: " + name + ":" + value, ex);
        }
    }

    /**
     * Invokes the B2B interface.
     * @param registration  The registration
     * @param makeModel     The make and model separated by a space
     */
    private void invokeB2b(Environment env, String registration, String makeModel) {
        //Reset the response just in case
        response = null;

        String[] makeAndModel = makeModel.split(" ");

        String b2bUrl = env.getRequiredProperty("mothB2BUrl") + registration + "/" + makeAndModel[0];

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

            Json.

            // Trims off the leading and trailing square brackets as this is invalid
            String output = responseOutput.toString().trim().substring(
                    1, responseOutput.toString().trim().length() - 1);

            logger.debug("Response: " + output);

            response = new JSONObject(output);

            conn.disconnect();

        } catch (MalformedURLException ex) {
            throw new RuntimeException("MalformedURLException", ex);
        } catch (IOException ex) {
            throw new RuntimeException("IOException", ex);
        } catch (JSONException ex) {
            throw new RuntimeException("JSONException", ex);
        }
    }
}
