package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import cucumber.api.PendingException;
import cucumber.api.java8.En;
import net.sf.cglib.core.Local;
import org.openqa.selenium.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.inject.Inject;

/**
 * Step definitions specific to the purchase history page of the mot app.
 */
public class PurchaseHistoryPageStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(HomePageStepDefinitions.class);

    /** The Webdriver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The webdriver to use
     */
    @Inject
    public PurchaseHistoryPageStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating PurchaseHistoryPageStepDefinitions");
        this.driverWrapper = driverWrapper;

        Then("^The summary line contains \"([^\"]*)\"$", (String text) -> {
            checkIfLabelChanged(text);
        });

        When("^I click the \"([^\"]*)\" button$", (String buttonText) -> {
            driverWrapper.clickButton(buttonText);
        });
    }

    /**
     * Check if summaryLine contains text.
     * @param text Text to find in summaryLine.
     */
    private void checkIfLabelChanged(String text) {
        assertTrue(driverWrapper.getElementText("summaryLine").contains(text));
    }
}
