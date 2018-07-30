package uk.gov.dvsa.mot.fixtures.generic;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import com.jayway.restassured.response.Response;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.util.HashMap;
import javax.inject.Inject;

public class JsonApiStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(JsonApiStepDefinitions.class);

    private Response response;

    /**
     * Creates a new instance.
     * @param env     The driver wrapper to use
     */
    @Inject
    public JsonApiStepDefinitions(WebDriverWrapper driverWrapper, Environment env) {
        Given("^I invoke the MOTH B2B API with registration \\{([^\\}]+)\\} and make \\{([^\\}]+)\\}$",
                (String registration, String make) -> {

                String url = env.getProperty("mothB2BUrl");

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Forwarded-For", "62.128.221.147");
                headers.put("Accept", "application/json");
                response = given().headers(headers).when().get(url + driverWrapper.getData(registration) + "/"
                        + driverWrapper.getData(make)).andReturn();

                logger.debug("RESPONSE:" + response.body().prettyPrint());
            });

        Given("^I invoke the Trade API search by test number with \\{([^\\}]+)\\}$",
                (String testNumber) -> {

                    String url = env.getProperty("tradeApiUrl");
                    String apiKey = env.getProperty("tradeApiKey");

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-api-key", apiKey);
                    headers.put("Accept", "application/json");
                    response = given().headers(headers).when().get(url + "?number="
                            + driverWrapper.getData(testNumber)).andReturn();

                    logger.debug("RESPONSE:" + response.body().prettyPrint());
                });

        Given("^I invoke the Trade API search by registration number with \\{([^\\}]+)\\}$",
                (String registrationNumber) -> {

                    String url = env.getProperty("tradeApiUrl");
                    String apiKey = env.getProperty("tradeApiKey");

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-api-key", apiKey);
                    headers.put("Accept", "application/json");
                    response = given().headers(headers).when().get(url + "?registration="
                            + driverWrapper.getData(registrationNumber)).andReturn();

                    logger.debug("RESPONSE:" + response.body().prettyPrint());
                });

        Given("^I invoke the Trade API search by page number with \"([^\"]+)\"$",
                (String pageNumber) -> {

                    String url = env.getProperty("tradeApiUrl");
                    String apiKey = env.getProperty("tradeApiKey");

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-api-key", apiKey);
                    headers.put("Accept", "application/json");
                    response = given().headers(headers).when().get(url + "?page="
                            + pageNumber).andReturn();

                    logger.debug("RESPONSE:" + response.body().prettyPrint());
                });

        Then("^The first response contains \"([^\"]+)\" equals \\{([^\\}]+)\\}$", (String name, String value) -> {
            response.then().body(name + "[0]", is(driverWrapper.getData(value)));
        });

        Then("^The response contains \"([^\"]+)\" equals \\{([^\\}]+)\\}$", (String name, String value) -> {
            response.then().body(name, is(driverWrapper.getData(value)));
        });
    }
}
