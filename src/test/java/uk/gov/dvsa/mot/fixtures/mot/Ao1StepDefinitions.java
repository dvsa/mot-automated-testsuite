package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import cucumber.api.java8.En;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

/**
 * Handles steps for Area Office 1 (AO1) test scenarios.
 */
public class Ao1StepDefinitions implements En {

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    public Ao1StepDefinitions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        And("^I search for site by reference \\{([^\\}]+)\\}$", (String siteReferenceKey) ->
                searchSiteByReference(siteReferenceKey));

        And("^I click the \"([^\"]+)\" link for the \"([^\"]+)\" field row$",
                (String linkText, String fieldName) ->
                    driverWrapper.clickLink("th", fieldName, "../td/", linkText));

        And("^I click the remove role link for \\{([^\\}]+)\\}$", (String nameKey) ->
                driverWrapper.clickLink("a", driverWrapper.getData(nameKey),
                    "../../td/", "Remove"));

        And("^I click the remove site association link for \\{([^\\}]+)\\}$", (String nameKey) ->
                driverWrapper.clickLink("a", driverWrapper.getData(nameKey),
                        "../../td/", "Remove"));

        And("^I check the \"([^\"]+)\" field row has value \"([^\"]+)\"$",
                (String fieldName, String value) -> assertTrue("Wrong field value",
                    driverWrapper.getTextFromTableRow(fieldName).contains(value)));

        And("^I check for assign AE role notification message for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String usernameKey, String nameKey) ->
                    assertTrue("Message not found",
                        driverWrapper.containsMessage("A role notification has been sent to "
                            + driverWrapper.getData(nameKey) + " '" + driverWrapper.getData(usernameKey) + "'.")));

        And("^I check for remove AE role notification message for \\{([^\\}]+)\\}$", (String nameKey) ->
                 assertTrue("Message not found",
                     driverWrapper.containsMessage("You have removed the role of Authorised"
                         + " examiner designated manager from " + driverWrapper.getData(nameKey))));

        And("^I check for assign site role notification message for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String usernameKey, String nameKey) ->
                        assertTrue("Message not found",
                                driverWrapper.containsMessage("You have assigned a role to "
                                        + driverWrapper.getData(nameKey) + ", " + driverWrapper.getData(usernameKey)
                                        + ". They have been sent a notification.")));

        And("^I check for site association for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String siteReferenceKey, String siteNameKey) -> assertTrue("Site association not found",
                    driverWrapper.getTextFromTableRowWithLink(driverWrapper.getData(siteNameKey))
                        .contains(driverWrapper.getData(siteReferenceKey))));

        And("^I check there is no site association for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String siteReferenceKey, String siteNameKey) ->
                    assertFalse("Site association should not be found",
                            driverWrapper.getTextFromTableRowWithLink(driverWrapper.getData(siteNameKey))
                                    .contains(driverWrapper.getData(siteReferenceKey))));

        And("^I check for a \"([^\"]+)\" role assignment for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String role, String usernameKey, String nameKey) ->
                    assertTrue("Role assignment not found",
                        driverWrapper.getTextFromTableRow(
                            driverWrapper.getData(nameKey) + "(" + driverWrapper.getData(usernameKey) + ")")
                                .contains(role)));
    }

    /**
     * Performs a site search by site reference, and opens the VTS summary screen.
     * @param siteReferenceKey      The key of the site reference to search for
     */
    private void searchSiteByReference(String siteReferenceKey) {
        String siteReference = driverWrapper.getData(siteReferenceKey);

        // When I click the "Site information" link
        driverWrapper.clickLink("Site information");

        // And I enter {siteReference} in the "Site ID" field
        driverWrapper.enterIntoField(siteReference, "Site ID");

        // And I press the "Search" button
        driverWrapper.pressButton("Search");

        /*
         * The MOT application has an issue that entities (such as sites) with edited addresses appear twice in search
         * results. So sometimes a site search by site reference (which should be unique) goes straight into the single
         * search result, and sometime you will get a search result screen with the same site listed several times, and
         * you need to click on one of the search results.
         */
        if (driverWrapper.hasLink(siteReference)) {
            // And I click the first {siteReference} link
            driverWrapper.clickFirstLink(siteReference);
        }

        // And The page title contains "Vehicle Testing Station"
        driverWrapper.checkCurrentPageTitle("Vehicle Testing Station");
    }
}
