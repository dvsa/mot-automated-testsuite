package uk.gov.dvsa.mot.fixtures.mot;

        import static org.junit.Assert.assertEquals;

        import cucumber.api.java8.En;

        import uk.gov.dvsa.mot.framework.WebDriverWrapper;

        import java.time.LocalDate;
        import java.time.LocalTime;
        import javax.inject.Inject;



/**
 * Step definitions specific to the <i>Tester does...</i> feature.
 */
public class FinanceStepDefinitions implements En {

    /**
     * The driver wrapper to use.
     */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     *
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public FinanceStepDefinitions(WebDriverWrapper driverWrapper, WebDriverWrapper driverWrapper1) {
        this.driverWrapper = driverWrapper1;

        And("^I click the link \"([^\"]+)\" with id \"([^\"]+)\"$", (String text, String id) ->
                driverWrapper.clickElement(id));
    }
}