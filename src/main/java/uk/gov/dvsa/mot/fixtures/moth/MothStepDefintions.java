package uk.gov.dvsa.mot.fixtures.moth;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;


public class MothStepDefintions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(MothStepDefintions.class);

    /** The driverwrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The web driver wrapper to be used
     */
    @Inject
    public MothStepDefintions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        When("^I enter \"([^\"]+)\" in the registration field$", (String text) ->
                driverWrapper.enterIntoFieldWithLabel(" Registration number (number plate) ", text));

        When("^I enter \\{([^\\}]+)\\} in the registration field$", (String dataKey) ->
                driverWrapper.enterIntoFieldWithLabel(" Registration number (number plate) ",
                        driverWrapper.getData(dataKey)));

        When("^I click the last \"([^\"]+)\" text$", (String linkText) ->
                driverWrapper.clickLastText(linkText));

        When("^I click the first \"([^\"]+)\" text$", (String linkText) ->
                driverWrapper.clickFirstText(linkText));

        When("^I go to the next tab$", () -> driverWrapper.goNextTab());

        When("^I close extra tabs$", () -> driverWrapper.closeTabs());

        When("^PDF is embedded in the page$", () -> assertTrue(driverWrapper.containsEmbeddedPdf()));
    }


}
