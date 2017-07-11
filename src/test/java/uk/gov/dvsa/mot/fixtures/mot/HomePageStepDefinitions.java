package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Step definitions specific to the homepage of the mot app.
 */
public class HomePageStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(HomePageStepDefinitions.class);

    /** The Webdriver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The webdriver to use
     */
    @Inject
    public HomePageStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating HomePageStepDefinitions");
        this.driverWrapper = driverWrapper;

        And("^I click the organisation link \\{([^\\}]+)\\}$", (String organisationKey) -> {
            clickOrganisationLink(organisationKey);
        });
    }

    /**
     * Clicks on the organisation link on the homepage.
     * @param organisationKey   The key for the organisation name to link
     */
    private void clickOrganisationLink(String organisationKey) {
        //And I click the organisation link {ORGANISATION}
        driverWrapper.clickLink(driverWrapper.getData(organisationKey));

        //And i check the page title
        driverWrapper.checkCurrentPageTitle("Authorised Examiner");
    }

}
