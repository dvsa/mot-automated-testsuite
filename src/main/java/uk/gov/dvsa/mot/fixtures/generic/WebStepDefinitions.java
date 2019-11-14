package uk.gov.dvsa.mot.fixtures.generic;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.junit.Assert;
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

        When("^I go back to the previous page$", () -> driverWrapper.goBack());

        When("^I press the \"([^\"]+)\" button$", (String buttonText) ->
                driverWrapper.pressButton(buttonText));

        When("^I press the first \"([^\"]+)\" button$", (String buttonText) ->
                driverWrapper.pressFirstButton(buttonText));

        When("^I press the first \\{([^\\}]+)\\} button$", (String buttonText) ->
                driverWrapper.pressFirstButton(driverWrapper.getData(buttonText)));

        When("^I click the button which contains text \"([^\"]+)\"$", (String buttonText) ->
                driverWrapper.clickButtonWithSiblingText(buttonText));

        When("^I click the button with class name \"([^\"]+)\"$", (String className) ->
                driverWrapper.clickButtonByClassName(className));

        When("^I click the button with id \"([^\"]+)\"$", (String buttonId) ->
                driverWrapper.clickElement(buttonId));

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

        When("^I click the id \"([^\"]+)\" checkbox$", (String labelText) ->
                driverWrapper.selectCheckboxById(labelText));

        When("^I clear the \"([^\"]+)\" checkbox$", (String labelText) ->
                driverWrapper.clearCheckbox(labelText));

        When("^I enter \"([^\"]+)\" in the \"([^\"]+)\" field$", (String text, String label) ->
                driverWrapper.enterIntoField(text, label));

        When("^I enter \"([^\"]+)\" in the \\{([^\\}]+)\\} field$", (String text, String labelKey) ->
                driverWrapper.enterIntoField(text, driverWrapper.getData(labelKey)));

        When("^I enter \\{([^\\}]+)\\} in the \"([^\"]+)\" field$", (String dataKey, String label) ->
                driverWrapper.enterIntoField(driverWrapper.getData(dataKey), label));

        When("^I enter \\{([^\\}]+)\\} in the \\{([^\\}]+)\\} field$", (String dataKey, String labelKey) ->
                driverWrapper.enterIntoField(driverWrapper.getData(dataKey), driverWrapper.getData(labelKey)));

        When("^I enter \"([^\"]+)\" in the \"([^\"]+)\" field in fieldset \"([^\"]+)\"$",
                (String text, String fieldLabel, String fieldsetLabel) ->
                        driverWrapper.enterIntoFieldInFieldset(text, fieldLabel, fieldsetLabel));

        When("^I enter \\{([^\\}]+)\\} in the \"([^\"]+)\" field in fieldset \"([^\"]+)\"$",
                (String dataKey, String fieldLabel, String fieldsetLabel) ->
                    driverWrapper.enterIntoFieldInFieldset(driverWrapper.getData(dataKey), fieldLabel, fieldsetLabel));

        When("^I enter \"([^\"]+)\" in the field with id \"([^\"]+)\"$", (String text, String id) ->
                driverWrapper.enterIntoFieldWithId(text, id));

        When("^I enter \\{([^\\}]+)\\} in the field with id \"([^\"]+)\"$", (String dataKey, String id) ->
                driverWrapper.enterIntoFieldWithId(driverWrapper.getData(dataKey), id));

        When("^I select \"([^\"]+)\" in the \"([^\"]+)\" field$", (String optionText, String label) ->
                driverWrapper.selectOptionInField(optionText, label));

        When("^I select \"([^\"]+)\" in the field with id \"([^\"]+)\"$", (String optionText, String id) ->
                driverWrapper.selectOptionInFieldById(optionText, id));

        When("^I select \\{([^\\}]+)\\} in the field with id \"([^\"]+)\"$", (String optionText, String id) ->
                driverWrapper.selectOptionInFieldById(driverWrapper.getData(optionText), id));

        Then("^The page contains \"([^\"]+)\"$", (String expected) ->
                assertTrue(driverWrapper.containsMessage(expected)));

        Then("^The page contains \"([^\"]+)\" or \"([^\"]+)\"$", (String expected1, String expected2) ->
                assertTrue(driverWrapper.containsMessage(expected1)
                        || driverWrapper.containsMessage(expected2)));

        Then("^The page does not contain \"([^\"]+)\"$", (String notExpected) ->
                assertTrue(driverWrapper.doesNotContainMessage(notExpected)));

        Then("^The page title contains \"([^\"]+)\"$", (String expected) ->
                driverWrapper.checkCurrentPageTitle(expected));

        Then("^I create an accessibility report \"([^\"]*)\"$", (String filename) ->
                driverWrapper.generateAccessibilityReport(filename));

        And("^I check there is a \"([^\"]+)\" link$", (String link) ->
                assertTrue(driverWrapper.hasLink(link)));

        And("^I check there is no \"([^\"]+)\" link$", (String link) ->
                assertFalse(driverWrapper.hasLink(link)));

        And("^I check the \"([^\"]+)\" button is disabled$", (String buttonText) ->
                assertTrue(driverWrapper.isButtonDisabled(buttonText)));

        And("^I check the \"([^\"]+)\" button is enabled$", (String buttonText) ->
                assertFalse(driverWrapper.isButtonDisabled(buttonText)));

        And("^I check the table with heading \"([^\"]+)\" has at least (\\d+) rows$",
                (String headingText, Integer rows) ->
                assertTrue(driverWrapper.checkTableHasRows(headingText, rows)));

        And("^I click the first \"([^\"]+)\" link$", (String linkText) ->
                driverWrapper.clickFirstLink(linkText));

        And("^I click the first \\{([^\\}]+)\\} link$", (String linkTextKey) ->
                driverWrapper.clickFirstLink(driverWrapper.getData(linkTextKey)));

        And("^I enter \"([^\"]+)\" into all fields with id prefix \"([^\"]+)\"$",
                (String text, String idPrefix) -> driverWrapper.enterIntoAllFieldsWithIdPrefix(idPrefix, text));

        And("^I check there is no \"([^\"]+)\" button$", (String buttonText) ->
                assertFalse("Button found with text: " + buttonText,
                        driverWrapper.checkButtonExists(buttonText)));

        And("^I click the last \"([^\"]+)\" link$", (String linkText) ->
                driverWrapper.clickLastLink(linkText));

        And("^I click the last \\{([^\\}]+)\\} link$", (String linkTextKey) ->
                driverWrapper.clickLastLink(driverWrapper.getData(linkTextKey)));

        And("^I click the \"([^\"]+)\" icon$", (String iconName) ->
                driverWrapper.clickIcon(iconName));

        And("^I check the \"([^\"]+)\" field row has value \"([^\"]+)\"$",
                (String fieldName, String value) -> Assert.assertTrue("Wrong field value",
                        driverWrapper.getTextFromTableRow(fieldName).contains(value)));

        And("^I check the row with value \\{([^\\}]+)\\} also has value \"([^\"]+)\"$",
                (String firstValue, String secondValue) ->
                        Assert.assertTrue("Row does not contain expected value",
                        driverWrapper.checkTextFromTableRowNoHeader(driverWrapper.getData(firstValue),
                                secondValue)));

        And("^I check the \"([^\"]+)\" field row has value \\{([^\\}]+)\\}$",
                (String fieldName, String valueKey) -> Assert.assertTrue("Wrong field value",
                        driverWrapper.getTextFromTableRow(fieldName).contains(driverWrapper.getData(valueKey))));

        And("^I check the \"([^\"]+)\" field column has value \"([^\"]+)\"$",
                (String fieldName, String value) -> Assert.assertTrue("Wrong field value",
                        driverWrapper.getTextFromTableColumn(fieldName).contains(value)));

        And("^I check the \"([^\"]+)\" field column has value \\{([^\\}]+)\\}$",
                (String fieldName, String valueKey) -> Assert.assertTrue("Wrong field value",
                        driverWrapper.getTextFromTableColumn(fieldName).contains(driverWrapper.getData(valueKey))));

        And("^I click the \"([^\"]+)\" link for the \"([^\"]+)\" field row$",
                (String linkText, String fieldName) ->
                        driverWrapper.clickLink("th", fieldName, "../td/", linkText));

        And("^I accept the alert popup$", () -> driverWrapper.acceptAlert());

        And("^I dismiss the alert popup$", () -> driverWrapper.dismissAlert());

        And("^I check the alert popup contains \"([^\"]+)\"$", (String text) ->
                assertTrue("Alert popup does not contain the expected text",
                        driverWrapper.getAlertText().contains(text)));

        And("^I check the \"([^\"]+)\" cookie is set$", (String cookieName) ->
                assertTrue("Cookie " + cookieName + " should have been set",
                        driverWrapper.isCookieSet(cookieName)));

        And("^I delete the \"([^\"]+)\" cookie$", (String cookieName) -> driverWrapper.deleteCookie(cookieName));

        And("^I wait for \"([^\"]+)\" seconds$", (Integer time) -> driverWrapper.timeWait(time));

        And("^I wait \"([^\"]+)\" seconds then \"([^\"]+)\" the page until \"([^\"]+)\" button displays$",
                (Integer seconds, Integer refresh, String buttonText) ->
                        driverWrapper.waitForButton(seconds, refresh, buttonText));

        And("^I set the starting url key as \"([^\"]+)\"$", (String startingUrlKey) ->
                driverWrapper.setStartingUrl(startingUrlKey));

        And("^I check the \"([^\"]+)\" field column does not have the value \\{([^\\}]+)\\}$",
                (String fieldName, String valueKey) -> Assert.assertFalse("Wrong field value",
                        driverWrapper.getTextFromTableColumn(fieldName).contains(driverWrapper.getData(valueKey))));
    }
}
