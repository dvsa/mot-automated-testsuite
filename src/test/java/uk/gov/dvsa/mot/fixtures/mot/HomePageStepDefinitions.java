package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        And("^I get the slot count from the homepage for site \\{([^\\}]+)\\}$", (String siteKey) -> {
            getSlotsCountFromHomePage(siteKey);
        });

        And("^I check a slot was successfully used for site \\{([^\\}]+)\\}$", (String siteKey) -> {
            checkSlotsBalanceDecreased(siteKey);
        });

        And("^I get the site number \\{([^\\}]+)\\} by name \\{([^\\}]+)\\}$",
                (String siteNumberKey, String siteNameKey) -> {
                    getSiteNumberByName(siteNumberKey, siteNameKey);
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

    /**
     * Gets the slot count for a select site from the homepage.
     * @param siteKey   The key the site name is stored under
     */
    private void getSlotsCountFromHomePage(String siteKey) {
        //And I get the slot count from the home page
        String slots = driverWrapper.getElementText("a", driverWrapper.getData(siteKey),
                        "../../../../div[2]/span");

        //And I set the slots in the browser for later comparision
        driverWrapper.setData("slotCount", slots);
    }

    /**
     * Checks that the slot balance of an AE decreased by 1.
     * @param siteKey   The key of the site name to be used
     */
    private void checkSlotsBalanceDecreased(String siteKey) {
        //And I check the slot balance decreased by 1
        String slots = driverWrapper.getElementText("a", driverWrapper.getData(siteKey),
                "../../../../div[2]/span");
        int decreeasedSlots = Integer.valueOf(driverWrapper.getData("slotCount")) - 1;
        assertTrue("The slot balance did not decrease", slots.contains(String.valueOf(decreeasedSlots)));
    }

    /**
     * Gets the site number from the homepage by using the site name.
     * @param siteNumberKey Site number key to store the value
     * @param siteNameKey   The site name key to be used
     */
    private void getSiteNumberByName(String siteNumberKey, String siteNameKey) {
        String siteName = driverWrapper.getData(siteNameKey);
        String siteString = driverWrapper.getElementText("a", siteName, ".");
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(siteString);

        if (matcher.find()) {
            driverWrapper.setData(siteNumberKey, matcher.group(1));
        }
    }
}
