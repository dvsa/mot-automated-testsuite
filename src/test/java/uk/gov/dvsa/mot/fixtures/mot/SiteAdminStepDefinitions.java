package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;

/**
 * Step definitions specific to the <i>Site Manager and Site Admin</i> feature.
 */
public class SiteAdminStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(SiteAdminStepDefinitions.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    @Inject
    public SiteAdminStepDefinitions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        And("^I click on the \\{([^\\}]+)\\}, \\{([^\\}]+)\\} site$", (String sitenameKey, String sitenumberKey) -> {
            driverWrapper.clickLink("(" + driverWrapper.getData(sitenumberKey) + ") "
                    + driverWrapper.getData(sitenameKey));
        });
    }
}
