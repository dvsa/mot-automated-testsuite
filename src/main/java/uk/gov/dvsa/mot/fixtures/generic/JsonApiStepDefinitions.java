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

    /**
     * Enum for the Trade API search types.
     */
    private enum TradeApiSearchType {
        TEST_NUMBER("number"), REGISTRATION_NUMBER("registration"), PAGE_NUMBER("page");

        private String parameter;

        public String getParameter() {
            return this.parameter;
        }

        TradeApiSearchType(String parameter) {
            this.parameter = parameter;
        }
    }

    private Response response;

    /**
     * Creates a new instance.
     * @param env     The driver wrapper to use
     */
    @Inject
    public JsonApiStepDefinitions(WebDriverWrapper driverWrapper, Environment env) {
        Given("^I invoke the MOTH B2B API with registration \\{([^\\}]+)\\} and make \\{([^\\}]+)\\}$",
                (String registration, String make) -> invokeMothB2B(driverWrapper.getData(registration),
                        driverWrapper.getData(make), env));

        Given("^I invoke the Trade API search by test number with \\{([^\\}]+)\\}$",
                (String testNumber) -> invokeTradeApi(TradeApiSearchType.TEST_NUMBER,
                        driverWrapper.getData(testNumber), env));

        Given("^I invoke the Trade API search by registration number with \\{([^\\}]+)\\}$",
                (String registrationNumber) -> invokeTradeApi(TradeApiSearchType.REGISTRATION_NUMBER,
                        driverWrapper.getData(registrationNumber), env));

        Given("^I invoke the Trade API search by page number with \"([^\"]+)\"$",
                (String pageNumber) -> invokeTradeApi(TradeApiSearchType.PAGE_NUMBER,
                        driverWrapper.getData(pageNumber), env));

        Then("^The first response contains \"([^\"]+)\" equals \\{([^\\}]+)\\}$", (String name, String value) -> {
            response.then().body(name + "[0]", is(driverWrapper.getData(value)));
        });

        Then("^The response contains \"([^\"]+)\" equals \\{([^\\}]+)\\}$", (String name, String value) -> {
            response.then().body(name, is(driverWrapper.getData(value)));
        });
    }

    /**
     * Invokes the MOTH B2B API endpoint with the given registration and make.
     * @param registration  The vehicle registration
     * @param make          The vehicle make
     * @param env           The environmental variables
     */
    private void invokeMothB2B(String registration, String make, Environment env) {
        String url = env.getProperty("mothB2BUrl");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-Forwarded-For", "62.128.221.147");
        headers.put("Accept", "application/json");
        response = given().headers(headers).when().get(url + registration + "/" + make).andReturn();

        logger.debug("RESPONSE:" + response.body().prettyPrint());
    }

    /**
     * Invokes the Trade API endpoint.
     * @param searchType    The type of Trade API search to initiate
     * @param value         The search value
     * @param env           The environmental variables
     */
    private void invokeTradeApi(TradeApiSearchType searchType, String value, Environment env) {
        String url = env.getProperty("tradeApiUrl");
        String apiKey = env.getProperty("tradeApiKey");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-api-key", apiKey);
        headers.put("Accept", "application/json");
        response = given().headers(headers).when().get(url + "?" + searchType.getParameter() + "=" + value).andReturn();

        logger.debug("RESPONSE:" + response.body().prettyPrint());
    }
}
