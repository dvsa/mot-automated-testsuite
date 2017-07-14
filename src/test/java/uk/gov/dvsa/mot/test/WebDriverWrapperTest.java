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
import java.util.ArrayList;
import java.util.List;

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
     * Tests <code>getData()</code> and <code>setData()</code> in combination.
     */
    @Test
    public void setAndGetData() {
        driverWrapper.setData("key1", "aaa");
        driverWrapper.setData("key2", "bbb");
        driverWrapper.setData("key3", "ccc");

        assertEquals("Wrong value returned", "aaa", driverWrapper.getData("key1"));
        assertEquals("Wrong value returned", "bbb", driverWrapper.getData("key2"));
        assertEquals("Wrong value returned", "ccc", driverWrapper.getData("key3"));
    }

    /**
     * Tests that calling <code>setData()</code> with the same key replaces the previous value.
     */
    @Test
    public void setDataReplaces() {
        driverWrapper.setData("key1", "aaa");
        driverWrapper.setData("key1", "bbb");

        assertEquals("Wrong value returned", "bbb", driverWrapper.getData("key1"));
    }

    /**
     * Tests that calling <code>getData()</code> with a key that hasn't been populated throws an exception.
     */
    @Test(expected = IllegalStateException.class)
    public void getDataNotSet() {
        driverWrapper.getData("key1");
    }

    /**
     * Tests <code>getAllDataKeys()</code> when data has been populated.
     */
    @Test
    public void getAllDataKeysPopulated() {
        driverWrapper.setData("key1", "aaa");
        driverWrapper.setData("key2", "bbb");

        List<String> expected = new ArrayList<>();
        expected.add("key1");
        expected.add("key2");

        assertEquals("Wrong value returned", expected, driverWrapper.getAllDataKeys());
    }

    /**
     * Tests <code>getAllDataKeys()</code> when data has not been populated.
     */
    @Test
    public void getAllDataKeysEmpty() {
        List<String> expected = new ArrayList<>();

        assertEquals("Wrong value returned", expected, driverWrapper.getAllDataKeys());
    }

    /**
     * Tests that <code>reset()</code> clears all data.
     */
    @Test
    public void resetClearsData() {
        driverWrapper.setData("key1", "aaa");
        driverWrapper.setData("key2", "bbb");

        driverWrapper.reset();

        assertEquals("All data should have been cleared", 0, driverWrapper.getAllDataKeys().size());
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when the input is disabled.
     */
    @Test
    public void isButtonDisabledInputYes() {
        browseTo("/isButtonDisabled-1.html", "isButtonDisabled - 1");
        assertTrue("Button is disabled", driverWrapper.isButtonDisabled("Button 2"));
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when the input is not disabled.
     */
    @Test
    public void isButtonDisabledInputNo() {
        browseTo("/isButtonDisabled-1.html", "isButtonDisabled - 1");
        assertFalse("Button is not disabled", driverWrapper.isButtonDisabled("Button 3"));
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when the input is missing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void isButtonDisabledInputMissing() {
        browseTo("/isButtonDisabled-1.html", "isButtonDisabled - 1");
        driverWrapper.isButtonDisabled("Button 4");
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when there are several inputs matching the button text.
     */
    @Test(expected = IllegalArgumentException.class)
    public void isButtonDisabledInputSeveral() {
        browseTo("/isButtonDisabled-2.html", "isButtonDisabled - 2");
        driverWrapper.isButtonDisabled("Button 1");
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when the button is disabled.
     */
    @Test
    public void isButtonDisabledButtonYes() {
        browseTo("/isButtonDisabled-3.html", "isButtonDisabled - 3");
        assertTrue("Button is disabled", driverWrapper.isButtonDisabled("Button 2"));
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when the button is not disabled.
     */
    @Test
    public void isButtonDisabledButtonNo() {
        browseTo("/isButtonDisabled-3.html", "isButtonDisabled - 3");
        assertFalse("Button is not disabled", driverWrapper.isButtonDisabled("Button 3"));
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when the button is missing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void isButtonDisabledButtonMissing() {
        browseTo("/isButtonDisabled-3.html", "isButtonDisabled - 3");
        driverWrapper.isButtonDisabled("Button 4");
    }

    /**
     * Tests <code>isButtonDisabled()</code>, when there are several buttons matching the button text.
     */
    @Test(expected = IllegalArgumentException.class)
    public void isButtonDisabledButtonSeveral() {
        browseTo("/isButtonDisabled-4.html", "isButtonDisabled - 4");
        driverWrapper.isButtonDisabled("Button 1");
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
