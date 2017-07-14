package uk.gov.dvsa.mot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.nio.file.Paths;

/**
 * Unit tests for any thorny logic in the <code>WebDriverWrapper</code> class.
 */
public class WebDriverWrapperTest {

    /**
     * The class under test.
     */
    private WebDriverWrapper driverWrapper;

    /**
     * Test initialisation.
     */
    @Before
    public void setup() {
        // the current working directory as a full path
        String projectHome = Paths.get(".").toAbsolutePath().normalize().toString();

        // mocked testsuite.properties values
        Environment env = new MockEnvironment().withProperty("startingUrl",
                "file:/" + projectHome + "/src/test/java/uk/gov/dvsa/mot/test/exampleHtml");

        driverWrapper = new HtmlUnitWebDriverWrapper(env);
    }

    /**
     * Test clean up.
     */
    @After
    public void tearDown() {
        driverWrapper.preDestroy();
        driverWrapper = null; // trigger GC
    }

    /**
     * Tests <code>hasLink()</code> matches by full link text.
     */
    @Test
    public void hasLinkFullText() {
        browseTo("/hasLink-1.html", "hasLink - 1");
        assertTrue("Couldn't find link by full name", driverWrapper.hasLink("test 1"));
    }

    /**
     * Tests <code>hasLink()</code> matches by partial link text.
     */
    @Test
    public void hasLinkPartialText() {
        browseTo("/hasLink-1.html", "hasLink - 1");
        assertTrue("Couldn't find link by partial name", driverWrapper.hasLink("2"));
    }

    /**
     * Tests <code>hasLink()</code> doesn't match links that aren't present.
     */
    @Test
    public void hasLinkNotPresent() {
        browseTo("/hasLink-1.html", "hasLink - 1");
        assertFalse("Shouldn't find link not present", driverWrapper.hasLink("test 4"));
    }

    /**
     * Browses to the specified test page, and check the page title is correct.
     *
     * @param testPage      The test page, must start with "/"
     * @param expectedTitle The expected page title
     */
    private void browseTo(String testPage, String expectedTitle) {
        driverWrapper.browseTo(testPage);
        assertEquals("Wrong page loaded", expectedTitle, driverWrapper.getCurrentPageTitle());
    }

    /**
     * Test version of the <code>WebDriverWrapper</code> class, that uses HtmlUnit in Chrome emulation mode.
     */
    private class HtmlUnitWebDriverWrapper extends WebDriverWrapper {

        /**
         * Creates a new instance.
         *
         * @param env The mocked testsuite.properties
         */
        public HtmlUnitWebDriverWrapper(Environment env) {
            super(env);
        }

        /**
         * Swaps Chrome with HtmlUnit, running in Chrome emulation mode.
         *
         * @return The web driver instance
         */
        @Override
        protected WebDriver createWebDriver() {
            return new HtmlUnitDriver(BrowserVersion.CHROME);
        }

        /**
         * Wait for the web page to fully re-load, and any onload javascript events to complete.
         */
        @Override
        protected void waitForPageLoad() {
            // does nothing
        }
    }
}
