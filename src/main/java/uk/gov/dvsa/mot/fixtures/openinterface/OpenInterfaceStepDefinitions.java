package uk.gov.dvsa.mot.fixtures.openinterface;

import static org.junit.Assert.assertTrue;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.framework.xml.XmlDocument;
import uk.gov.dvsa.mot.reporting.OpenInterfaceReportBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class OpenInterfaceStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(OpenInterfaceStepDefinitions.class);

    /** The driverwrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /** The configuration to use by the testsuite. */
    private final Environment env;

    /**
     * Create a new instance.
     * @param driverWrapper The web driver wrapper to be used
     */
    @Inject
    public OpenInterfaceStepDefinitions(WebDriverWrapper driverWrapper, Environment env) {
        this.driverWrapper = driverWrapper;
        this.env = env;

        Then("^I check if a \"([^\\}]+)\", with \\{([^\\}]+)\\} vrm, contains xpath values:$",
                (String queryName, String registration, DataTable table) -> {
                    String response = getResponse(registration);
                    XmlDocument document = getXmlDocument(response);

                    Map<String, String> values = expandDataTableXpath(table);
                    values = processXpathValues(values);
                    Map<String,String> responseValues =
                            getResponseValues(document, new ArrayList<>(values.keySet()));

                    writeHtmlxPath(queryName, driverWrapper.getData(registration), response, values, responseValues);
                });
    }

    /**
     * Get values from the open interface response.
     *
     * @param xmlDocument response xml to extract the values from
     * @param xpathList list of xpath queries to locate in the response
     * @return map of xpath to values extracted from the response
     */
    private HashMap<String, String> getResponseValues(XmlDocument xmlDocument, List<String> xpathList) {
        HashMap<String, String> responseValues = new HashMap<>();

        for (String xpathString : xpathList) {
            try {
                responseValues.put(xpathString, xmlDocument.getxPath(xpathString));
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }

        return responseValues;
    }

    /**
     * Get the response from open interface for a vrm.
     *
     * @param registration used to query the endpoint
     * @return xml response from the open interface server
     */
    private String getResponse(String registration) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder( "curl", "-v",
                    "--insecure", "--connect-timeout 10",
                    "--tlsv1.2", "-E ../openif-test-cert/dvlaclienttest.pem",
                    env.getProperty("openInterfaceUrl") + "dvla/servlet/ECSODDispatcher?VRM=" + registration);

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder rawXml = new StringBuilder();
            String content;

            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((content = bufferedReader.readLine()) != null) {
                rawXml.append(content);
            }

            return rawXml.toString();
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to check the mot history, %s", ex.getMessage()));
        }
    }

    private XmlDocument getXmlDocument(String rawXml) {
        try {
            return new XmlDocument(rawXml);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to create new XmlDocument, %s", ex.getMessage()));
        }
    }

    private Map<String,String> expandDataTableXpath(DataTable table) {
        return table.asMap(String.class, String.class);
    }

    private List<String> expandDataTable(DataTable table) {
        return table.asList(String.class);
    }

    private boolean valueMatchesDataKey(String value) {
        return driverWrapper.getAllDataKeys().contains(value);
    }

    private List<String> processValues(List<String> values) {
        List<String> processedList = new ArrayList<>();

        for (String value : values) {
            if (valueMatchesDataKey(value)) {
                processedList.add(driverWrapper.getData(value));
            } else {
                processedList.add(value);
            }
        }

        return processedList;
    }

    private Map<String, String> processXpathValues(Map<String, String> values) {
        Map<String, String> processedMap = new HashMap<>();

        for (int i = 0; i < values.size(); ++i) {
            String key = (String) values.keySet().toArray()[i];
            String value = (String) values.values().toArray()[i];

            if (valueMatchesDataKey(value)) {
                processedMap.put(key, driverWrapper.getData(value));
            } else {
                processedMap.put(key, value);
            }
        }

        return processedMap;
    }

    private void writeHtmlxPath(String queryName, String registration, String response,
                                Map<String, String> expectedValues, Map<String, String> responseValues) {
        boolean scenarioResult = true;

        Map<String, Boolean> results = new HashMap<>();

        for (String key : expectedValues.keySet()) {
            String expectedValue = expectedValues.get(key);
            String responseValue = responseValues.get(key);

            boolean comparisonResult = expectedValue.equals(responseValue);

            results.put(key, comparisonResult);

            if (!comparisonResult) {
                scenarioResult = false;
            }
        }

        OpenInterfaceReportBuilder.generateResponseReport(queryName, registration, response,
                expectedValues, responseValues, results);

        assertTrue(scenarioResult);
    }
}
