package uk.gov.dvsa.mot.fixtures.mot;

import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;

import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.inject.Inject;



/**
 * Step definitions specific to the <i>Tester does...</i> feature.
 */
public class TesterRecordsContingencyStepDefinitions implements En {

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
    public TesterRecordsContingencyStepDefinitions(WebDriverWrapper driverWrapper, WebDriverWrapper driverWrapper1) {
        this.driverWrapper = driverWrapper1;

        And("^I check the Test Information summary section of the test summary has \"([^\"]+)\" of \"([^\"]+)\"$",
                (String field, String value) ->
                        assertEquals(value, driverWrapper.getTextFromDefinitionList(field)));


        And("^I set time as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String hourKeyName, String minuteKeyName, String ampmKeyName) ->
                        setTime(hourKeyName, minuteKeyName, ampmKeyName));


        And("^I set the site \\{([^\\}]+)\\}$", (String siteNameKey) ->
                setSite(driverWrapper.getData(siteNameKey)));


    }

    /**
     * Sets the specified keys with the current date.
     * @param hourKeyName        The key to set with the current hour of the time (1..31)
     * @param minuteKeyName      The key to set with the current minute of the time (1..12)
     * @param ampmKeyName        The kwy to set with the current am or pm of the day
     */

    private void setTime(String hourKeyName, String minuteKeyName, String ampmKeyName) {
        LocalTime time = LocalTime.now().minusMinutes(1);
        DateTimeFormatter amPmFormatter = DateTimeFormatter.ofPattern("a"); // am / pm
        DateTimeFormatter twelveHourFormatter = DateTimeFormatter.ofPattern("h"); // 1 - 12

        driverWrapper.setData(hourKeyName, time.format(twelveHourFormatter));
        driverWrapper.setData(minuteKeyName, String.valueOf(time.getMinute()));
        driverWrapper.setData(ampmKeyName, time.format(amPmFormatter));
        driverWrapper.setData(ampmKeyName, String.valueOf(amPmFormatter));

    }


    /** To set the site.
     *
     * @param site        The site to use (for multi-site testers)
     */
    private void setSite(String site) {
        // if page title Select your current site
        if (driverWrapper.containsMessage("Location where the test was performed")) {
            // select the first site
            driverWrapper.selectRadio(site);
        }
    }


}