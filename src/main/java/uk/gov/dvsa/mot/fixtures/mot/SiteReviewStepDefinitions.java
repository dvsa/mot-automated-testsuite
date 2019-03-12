package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.inject.Inject;

public class SiteReviewStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(SiteReviewStepDefinitions.class);

    /** The web driver to use. */
    private WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The webdriver to use
     */
    @Inject
    public SiteReviewStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating SiteReviewStepDefinitions");
        this.driverWrapper = driverWrapper;

        And("^I enter the date of site visit as (\\d+) days ago$",
                (Integer daysAgo) -> {
                    enterDateOfSiteVisit(daysAgo);
                });

        And("I find the \\{([^\\}]+)\\} site in the service reports$",
                (String siteNameKey) -> findSiteInServiceReports(siteNameKey));
    }

    /**
     * Populates the date of site visit with current date minus days ago visited.
     * @param daysAgo how many days ago site has been visited
     */
    private void enterDateOfSiteVisit(int daysAgo) {
        LocalDate theDate = LocalDate.now();
        theDate = theDate.minusDays(daysAgo);
        driverWrapper.enterIntoFieldWithId(theDate.getDayOfMonth(), "dateDay");
        driverWrapper.enterIntoFieldWithId(theDate.getMonthValue(), "dateMonth");
        driverWrapper.enterIntoFieldWithId(theDate.getYear(), "dateYear");

        String dateOfSiteVisit = theDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        driverWrapper.setData("dateOfSiteVisit", dateOfSiteVisit);
    }

    /**
     * Finds the site in the service report list.
     * @param siteNameKey The data key for the site name to find
     */
    private void findSiteInServiceReports(String siteNameKey) {
        boolean finished = false;

        while (!finished) {
            if (driverWrapper.hasLink(driverWrapper.getData(siteNameKey))) {
                finished = true;
            } else if (driverWrapper.hasLink("Next")) {
                driverWrapper.clickLink("Next");
            } else {
                logger.error("Site not found in service report list");
                finished = true;
            }
        }
    }
}
