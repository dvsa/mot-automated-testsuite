package uk.gov.dvsa.mot.fixtures.generic;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.framework.csv.CsvDocument;

import javax.inject.Inject;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class CsvStepDefinitions implements En {
    private static final Logger logger = LoggerFactory.getLogger(CsvStepDefinitions.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    private CsvDocument csvDocument;

    @Inject
    public CsvStepDefinitions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        Then("^I download CSV file from \"([^\"]+)\" url$", (String url) -> {
            csvDocument = driverWrapper.getCsvFromUrl(url);
            assertFalse(csvDocument == null);
        });

        And("^I check if the CSV file contains \"([^\"]+)\"$", (String text) ->
                assertTrue(csvDocument.contains(text))
        );

        And("^I check if the ([0-9]+)th row of the CSV file contains \"([^\"]+)\"$", (String text) ->
                assertTrue(csvDocument.contains(text))
        );

        And("^I check if the ([0-9]+)th column of the CSV file equals \"([^\"]+)\"$", (String text) ->
                assertTrue(csvDocument.contains(text))
        );
    }
}
