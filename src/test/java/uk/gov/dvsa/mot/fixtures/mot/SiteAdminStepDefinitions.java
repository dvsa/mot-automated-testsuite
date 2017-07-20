package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

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

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    @Inject
    public SiteAdminStepDefinitions(WebDriverWrapper driverWrapper) {

        And("^I click on the \\{([^\\}]+)\\}, \\{([^\\}]+)\\} site$",
                (String sitenameKey, String sitenumberKey) ->
                    driverWrapper.clickLink("(" + driverWrapper.getData(sitenumberKey) + ") "
                        + driverWrapper.getData(sitenameKey)));

        And("^I check the role summary has a new role of \"([^\"]+)\"$", (String newRole) ->
                assertTrue("Wrong new role listed",
                    driverWrapper.getTextFromTableRow("New role").contains(newRole)));

        And("^I check there is a role assignment confirmation message for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String usernameKey, String nameKey) -> assertTrue("Wrong new role listed",
                        driverWrapper.containsMessage(
                            "You have assigned a role to " + driverWrapper.getData(nameKey) + ", "
                                + driverWrapper.getData(usernameKey) + ". They have been sent a notification.")));

        And("^I check there is pending \"([^\"]+)\" role listed for \\{([^\\}]+)\\}$",
                (String pendingRole, String nameKey) -> assertTrue("Pending role not listed",
                        driverWrapper.getTextFromTableRow(driverWrapper.getData(nameKey))
                                .contains(pendingRole + "\nPending")));
    }
}
