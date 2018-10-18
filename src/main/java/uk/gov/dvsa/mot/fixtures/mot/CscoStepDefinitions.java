package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

/**
 * Step definitions for the CSCO feature.
 */
public class CscoStepDefinitions implements En {

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    public CscoStepDefinitions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        When("^I perform a site search for \"([^\"]+)\" as \\{([^\\}]+)\\} and select \\{([^\\}]+)\\}$",
                (String fieldName, String fieldValueKey, String siteReferenceKey) ->
                    performSiteSerch(fieldName, fieldValueKey, siteReferenceKey));
    }

    /**
     * Perform a site search, and click on the expected site (if results screen returned).
     * @param fieldName             The site field to search by
     * @param fieldValueKey         The key of the search value
     * @param siteReferenceKey      The expected site reference
     */
    private void performSiteSerch(String fieldName, String fieldValueKey, String siteReferenceKey) {
        // When I enter <fieldValueKey> in the <fieldName> field
        driverWrapper.enterIntoField(driverWrapper.getData(fieldValueKey), fieldName);

        // Then I press the "Search" button
        driverWrapper.pressButton("Search");

        if (!driverWrapper.getCurrentPageTitle().contains("Vehicle testing station")) {
            // not gone straight into the site details screen, so assume we are on the search results screen
            // (which has no page title), so we need to select the first of the expected results
            // (note the same site can be listed several times with different phone numbers or address edits)
            driverWrapper.clickFirstLink(driverWrapper.getData(siteReferenceKey));
        }
    }
}
