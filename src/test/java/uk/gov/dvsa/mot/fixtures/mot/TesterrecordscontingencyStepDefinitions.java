package uk.gov.dvsa.mot.fixtures.mot;

import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;

import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.time.LocalDate;
import java.time.LocalTime;
import javax.inject.Inject;



/**
 * Step definitions specific to the <i>Tester does...</i> feature.
 */
public class TesterrecordscontingencyStepDefinitions implements En {

    /**
     * The driver wrapper to use.
     */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     *
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public TesterrecordscontingencyStepDefinitions(WebDriverWrapper driverWrapper, WebDriverWrapper driverWrapper1) {
        this.driverWrapper = driverWrapper1;

        And("^I check the Test Information summary section of the test summary has \"([^\"]+)\" of \"([^\"]+)\"$",
                (String field, String value) ->
                        assertEquals(value, driverWrapper.getTextFromDefinitionList(field)));
    }

}