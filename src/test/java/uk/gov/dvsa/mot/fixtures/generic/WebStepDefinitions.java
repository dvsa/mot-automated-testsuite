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

        When("^I browse to (\\S+)$", (String relativePath) ->
                driverWrapper.browseTo(relativePath));

        When("^I press the \"([^\"]+)\" button$", (String buttonText) ->
                driverWrapper.pressButton(buttonText));

        When("^I click the \"([^\"]+)\" link$", (String linkText) ->
                driverWrapper.clickLink(linkText));

        When("^I click the text \"([^\"]+)\"$", (String text) ->
                driverWrapper.clickText(text));

        When("^I click the \\{([^\\}]+)\\} link$", (String linkKey) ->
                driverWrapper.clickLink(driverWrapper.getData(linkKey)));

        When("^I click the \"([^\"]+)\" radio button$", (String labelText) ->
                driverWrapper.selectRadio(labelText));

        When("^I click the \\{([^\\}]+)\\} radio button$", (String labelTextKey) ->
                driverWrapper.selectRadio(driverWrapper.getData(labelTextKey)));

        When("^I click the \"([^\"]+)\" radio button in fieldset \"([^\"]+)\"$",
                (String labelText, String fieldsetLegend) ->
                driverWrapper.selectRadioInFieldset(fieldsetLegend, labelText));

        When("^I click the \\{([^\\}]+)\\} radio button in fieldset \"([^\"]+)\"$",
                (String labelTextKey, String fieldsetLegend) ->
                        driverWrapper.selectRadioInFieldset(fieldsetLegend, driverWrapper.getData(labelTextKey)));

        When("^I click the \"([^\"]+)\" radio button in fieldset \"([^\"]+)\" in fieldset \"([^\"]+)\"$",
                (String labelText, String nestedLegend, String outerLegend) ->
                        driverWrapper.selectRadioInNestedFieldset(outerLegend, nestedLegend, labelText));

        When("^I click the \\{([^\\}]+)\\} radio button in fieldset \"([^\"]+)\" in fieldset \"([^\"]+)\"$",
                (String labelTextKey, String nestedLegend, String outerLegend) ->
                        driverWrapper.selectRadioInNestedFieldset(
                                outerLegend, nestedLegend, driverWrapper.getData(labelTextKey)));

        When("^I click the \"([^\"]+)\" checkbox$", (String labelText) ->
                driverWrapper.selectCheckbox(labelText));

        When("^I enter \"([^\"]+)\" in the \"([^\"]+)\" field$", (String text, String label) ->
                driverWrapper.enterIntoField(text, label));

        When("^I enter \\{([^\\}]+)\\} in the \"([^\"]+)\" field$", (String dataKey, String label) ->
                driverWrapper.enterIntoField(driverWrapper.getData(dataKey), label));

        When("^I enter the next available test email address in the \"([^\"]+)\" field$", (String label) ->
                driverWrapper.enterNextTestEmailIntoField(label));

        When("^I enter the generated username in the \"([^\"]+)\" field$", (String label) ->
                driverWrapper.enterGeneratedUsernameIntoField(label));

        When("^I enter \"([^\"]+)\" in the field with id \"([^\"]+)\"$", (String text, String id) ->
                driverWrapper.enterIntoFieldWithId(text, id));

        When("^I enter \\{([^\\}]+)\\} in the field with id \"([^\"]+)\"$", (String dataKey, String id) ->
                driverWrapper.enterIntoFieldWithId(driverWrapper.getData(dataKey), id));

        When("^I select \"([^\"]+)\" in the \"([^\"]+)\" field$", (String optionText, String label) ->
                driverWrapper.selectOptionInField(optionText, label));

        When("^I select \"([^\"]+)\" in the field with id \"([^\"]+)\"$", (String optionText, String id) ->
                driverWrapper.selectOptionInFieldById(optionText, id));

        Then("^The page contains \"([^\"]+)\"$", (String expected) ->
                driverWrapper.containsMessage(expected));

        Then("^The page title contains \"([^\"]+)\"$", (String expected) ->
                driverWrapper.checkCurrentPageTitle(expected));

        And("^I check there is a \"([^\"]+)\" link$", (String link) ->
                assertTrue(driverWrapper.hasLink(link)));

        And("^I check there is no \"([^\"]+)\" link$", (String link) ->
                assertFalse(driverWrapper.hasLink(link)));

        And("^I check the \"([^\"]+)\" button is disabled$", (String buttonText) ->
                assertTrue(driverWrapper.isButtonDisabled(buttonText)));

        And("^I check the \"([^\"]+)\" button is enabled$", (String buttonText) ->
                assertFalse(driverWrapper.isButtonDisabled(buttonText)));

        And("^I click the first \"([^\"]+)\" link$", (String linkText) ->
                driverWrapper.clickFirstLink(linkText));

        And("^I enter \"([^\"]+)\" into all fields with id prefix \"([^\"]+)\"$",
                (String text, String idPrefix) -> driverWrapper.enterIntoAllFieldsWithIdPrefix(idPrefix, text));

        And("^I check there is no \"([^\"]+)\" button$", (String buttonText) ->
                assertFalse("Button found with text: " + buttonText,
                        driverWrapper.checkButtonExists(buttonText)));

        And("^I click the last \"([^\"]+)\" link$", (String linkText) ->
                driverWrapper.clickLastLink(linkText));
    }
}
