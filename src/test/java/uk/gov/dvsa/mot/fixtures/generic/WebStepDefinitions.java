package uk.gov.dvsa.mot.fixtures.generic;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Step definitions for web test steps.
 */
public class WebStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(WebStepDefinitions.class);

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    @Inject
    public WebStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating WebStepDefinitions...");

        When("^I browse to (\\S+)$", (String relativePath) -> {
            driverWrapper.browseTo(relativePath);
        });

        When("^I press the \"([^\"]+)\" button$", (String buttonText) -> {
            driverWrapper.pressButton(buttonText);
        });

        When("^I click the \"([^\"]+)\" link$", (String linkText) -> {
            driverWrapper.clickLink(linkText);
        });

        When("^I enter \"([^\"]+)\" in the \"([^\"]+)\" field$", (String text, String label) -> {
            driverWrapper.enterIntoField(text, label);
        });

        When("^I enter \\{([^\\}]+)\\} in the \"([^\"]+)\" field$", (String dataKey, String label) -> {
            driverWrapper.enterIntoField(driverWrapper.getData(dataKey), label);
        });

        When("^I enter \"([^\"]+)\" in the field with id \"([^\"]+)\"$", (String text, String id) -> {
            driverWrapper.enterIntoFieldWithId(text, id);
        });

        When("^I select \"([^\"]+)\" in the \"([^\"]+)\" field$", (String optionText, String label) -> {
            driverWrapper.selectOptionInField(optionText, label);
        });

        Then("^The page title contains \"([^\"]+)\"$", (String expected) -> {
            driverWrapper.checkCurrentPageTitle(expected);
        });

        And("^I check there is a \"([^\"]+)\" link$", (String link) -> {
            assertTrue(driverWrapper.hasLink(link));
        });

        And("^I check there is no \"([^\"]+)\" link$", (String link) -> {
            assertFalse(driverWrapper.hasLink(link));
        });
    }
}
