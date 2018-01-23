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

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    @Inject
    public SiteAdminStepDefinitions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        And("^I check the role summary has a new role of \"([^\"]+)\"$", (String newRole) ->
                assertTrue("Wrong new role listed",
                    driverWrapper.getTextFromTableRow("New role").contains(newRole)));

        And("^I check there is pending \"([^\"]+)\" role listed for \\{([^\\}]+)\\}$",
                (String pendingRole, String nameKey) -> assertTrue("Pending role not listed",
                        driverWrapper.checkTextFromAnyTableRow(
                            driverWrapper.getData(nameKey), pendingRole + "\nPending")));

        And("^I check the VTS default for \"([^\"]+)\" is \\{([^\\}]+)\\}$",
                (String brakeType, String testTypeKey) -> assertTrue("VTS default not listed",
                        driverWrapper.getTextFromTableRow(brakeType).contains(driverWrapper.getData(testTypeKey))));

        And("^I choose different brake defaults for \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\} "
                + "as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$", this::chooseAllDifferentBrakeDefaults);

        When("^I select the site \\{([^\\}]+)\\} - \\{([^\\}]+)\\} at AE \\{([^\\}]+)\\}",
                (String siteNumber, String siteName, String aeName) -> selectSite(driverWrapper.getData(siteNumber),
                        driverWrapper.getData(siteName), driverWrapper.getData(aeName)));
    }

    /**
     * Selects a site either directly from the homepage if a link exists else through the AE details.
     * @param siteNumber    the site number
     * @param siteName      the site name
     * @param aeName        the AE name
     */
    private void selectSite(String siteNumber, String siteName, String aeName) {
        if (driverWrapper.hasLink("(" + siteNumber + ") " + siteName)) {
            // If there is a direct link to the site select it
            driverWrapper.clickLink("(" + siteNumber + ") " + siteName);
        } else {
            // Else select the AE
            driverWrapper.clickLink(aeName);
            // And select the site from there
            driverWrapper.clickLink(siteName);
        }
    }

    /**
     * Choose different brake test defaults, to use in the test, and check for at the end.
     * <p>This technique is used to cope with the very small amount of suitable test sites available, rather than
     * using default brake test settings explicitly set in the Gherkin test steps.</p>
     * @param startingGroupABrakeDefault            The data key name of the initial Group A brake default
     * @param startingGroupBServiceBrakeDefault     The data key name of the initial Group B service brake default
     * @param startingGroupBParkingBrakeDefault     The data key name of the initial Group B parking brake default
     * @param newGroupABrakeDefault                 The data key name of the new Group A brake default
     * @param newGroupBServiceBrakeDefault          The data key name of the new Group B service brake default
     * @param newGroupBParkingBrakeDefault          The data key name of the new Group B parking brake default
     */
    private void chooseAllDifferentBrakeDefaults(String startingGroupABrakeDefault,
            String startingGroupBServiceBrakeDefault, String startingGroupBParkingBrakeDefault,
            String newGroupABrakeDefault, String newGroupBServiceBrakeDefault, String newGroupBParkingBrakeDefault) {

        driverWrapper.setData(newGroupABrakeDefault,
                chooseDifferentBrakeDefaults(driverWrapper.getData(startingGroupABrakeDefault)));

        driverWrapper.setData(newGroupBServiceBrakeDefault,
                chooseDifferentBrakeDefaults(driverWrapper.getData(startingGroupBServiceBrakeDefault)));

        driverWrapper.setData(newGroupBParkingBrakeDefault,
                chooseDifferentBrakeDefaults(driverWrapper.getData(startingGroupBParkingBrakeDefault)));
    }

    /**
     * Chooses a different brake test default setting.
     * @param startingBrakeDefault  The brake test default
     * @return A different setting
     */
    private String chooseDifferentBrakeDefaults(String startingBrakeDefault) {
        if (startingBrakeDefault == null || startingBrakeDefault.trim().length() == 0) {
            return "Decelerometer";
        } else {
            switch (startingBrakeDefault) {
                case "1": // 1 = Decelerometer
                    return "Plate";

                case "4": // 4 = Plate
                    return "Roller";

                case "5": // 5 = Roller
                default:
                    return "Decelerometer";
            }
        }
    }
}
