package uk.gov.dvsa.mot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.framework.WrongPageException;

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
    private static HtmlUnitWebDriverWrapper driverWrapper;

    /**
     * Global Test initialisation.
     * <p>Done once for the whole test class rather than per test, as this is expensive and time consuming.</p>
     */
    @BeforeClass
    public static void setupAll() {
        // the current working directory as a full path
        String projectHome = Paths.get(".").toAbsolutePath().normalize().toString();

        // mocked testsuite.properties values
        Environment env = new MockEnvironment().withProperty("startingUrl",
                "file:/" + projectHome + "/src/test/java/uk/gov/dvsa/mot/test/exampleHtml");

        driverWrapper = new HtmlUnitWebDriverWrapper(env);
    }

    /**
     * Per-test clean up.
     */
    @After
    public void tearDown() {
        driverWrapper.reset();
    }

    /**
     * Global Test clean up.
     */
    @AfterClass
    public static void tearDownAll() {
        driverWrapper.preDestroy();
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
     * Tests <code>getAllDataKeys()</code> sorts the key names alphabetically.
     */
    @Test
    public void getAllDataKeysSorted() {
        driverWrapper.setData("zzkey2", "aaa");
        driverWrapper.setData("xxkey3", "bbb");
        driverWrapper.setData("yykey1", "ccc");

        List<String> expected = new ArrayList<>();
        expected.add("xxkey3");
        expected.add("yykey1");
        expected.add("zzkey2");

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
     * Tests <code>getLinkClass()</code>, when classes have been populated.
     */
    @Test
    public void getLinkClassPopulated() {
        browseTo("/hasLink-1.html", "hasLink - 1");
        assertEquals("Wrong classes returned",
                "class1 class2 class3", driverWrapper.getLinkClass("test 1"));
    }

    /**
     * Tests <code>getLinkClass()</code>, when classes have been populated, matching by partial link text.
     */
    @Test
    public void getLinkClassPopulatedPartialLinkText() {
        browseTo("/hasLink-1.html", "hasLink - 1");
        assertEquals("Wrong classes returned",
                "class1 class2 class3", driverWrapper.getLinkClass("1"));
    }

    /**
     * Tests <code>getLinkClass()</code>, when no classes have been populated.
     */
    @Test
    public void getLinkClassNotPopulated() {
        browseTo("/hasLink-1.html", "hasLink - 1");
        assertNull("Wrong classes returned", driverWrapper.getLinkClass("test 2"));
    }

    /**
     * Tests <code>getLinkClass()</code>, when no link exists.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getLinkClassMissing() {
        browseTo("/hasLink-1.html", "hasLink - 1");
        driverWrapper.getLinkClass("test 4");
    }

    /**
     * Tests <code>getLinkClass()</code>, when multiple links exists.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getLinkClassMultiple() {
        browseTo("/hasLink-2.html", "hasLink - 2");
        driverWrapper.getLinkClass("test 1");
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
     * Tests <code>hasLink()</code> with multiple matching links.
     */
    @Test
    public void hasLinkMultiple() {
        browseTo("/hasLink-2.html", "hasLink - 2");
        assertTrue("Shouldn't find link not present", driverWrapper.hasLink("test 1"));
    }

    /**
     * Tests <code>hasLink(String startTag, String startText, String relativeXPath)</code> with a matching example.
     */
    @Test
    public void hasLinkComplexMatching() {
        browseTo("/hasLinkComplex-1.html", "hasLink complex - 1");
        assertTrue("Should find link present",
                driverWrapper.hasLink("span", "Span Text", "../../../div/table/"));
    }

    /**
     * Tests <code>hasLink(String startTag, String startText, String relativeXPath)</code> with an example that does
     * not match.
     */
    @Test
    public void hasLinkComplexNotMatching() {
        browseTo("/hasLinkComplex-1.html", "hasLink complex - 1");
        assertFalse("Should find link not present",
                driverWrapper.hasLink("span", "Span Text", "../h3/"));
    }

    /**
     * Tests <code>getElementText(String id)</code> with a matching example.
     */
    @Test
    public void getElementTextByIdMatching() {
        browseTo("/getElementText-1.html", "getElementText - 1");
        assertEquals("Should find element",
                "Other section.", driverWrapper.getElementText("other123"));
    }

    /**
     * Tests <code>getElementText(String id)</code> with a non matching example.
     */
    @Test(expected = NoSuchElementException.class)
    public void getElementTextByIdNotMatching() {
        browseTo("/getElementText-1.html", "getElementText - 1");
        driverWrapper.getElementText("wibble321");
    }

    /**
     * Tests <code>getElementText(String startTag, String startText, String relativeXPath)</code> with a
     * matching example.
     */
    @Test
    public void getElementTextComplexMatching() {
        browseTo("/getElementText-1.html", "getElementText - 1");
        assertEquals("Should find text present", "Paragraph Text.",
                driverWrapper.getElementText(
                        "span", "Span Text", "../../../div/table/.//p"));
    }

    /**
     * Tests <code>getElementText(String startTag, String startText, String relativeXPath)</code> with an
     * example that does not match.
     */
    @Test(expected = NoSuchElementException.class)
    public void getElementTextComplexNotMatching() {
        browseTo("/getElementText-1.html", "getElementText - 1");
        driverWrapper.getElementText("span", "Span Text", "../h3/a");
    }

    /**
     * Tests <code>getTextFromTableRow()</code> with a matching example.
     */
    @Test
    public void getTextFromTableRowMatching() {
        browseTo("/getTextFromTableRow-1.html", "getTextFromTableRow - 1");
        assertEquals("Should find matching text",
                "Data Text 2.", driverWrapper.getTextFromTableRow("Header Text 2."));
    }

    /**
     * Tests <code>getTextFromTableRow()</code> with a non matching example.
     */
    @Test
    public void getTextFromTableRowNotMatching() {
        browseTo("/getTextFromTableRow-1.html", "getTextFromTableRow - 1");
        assertEquals("Should not find any text",
                "", driverWrapper.getTextFromTableRow("Header Text 4."));
    }

    /**
     * Tests <code>getTextFromTableRow()</code> with a matching nested example.
     */
    @Test
    public void getTextFromTableRowNestedMatching() {
        browseTo("/getTextFromTableRow-2.html", "getTextFromTableRow - 2");
        assertEquals("Should find matching and nested text", "Data Text 2. \nSpan Text 2.",
                driverWrapper.getTextFromTableRow("Heading Text 2."));
    }

    /**
     * Tests <code>getTextFromTableRowWithLink()</code> with a matching example.
     */
    @Test
    public void getTextFromTableRowWithLinkMatching() {
        browseTo("/getTextFromTableRowWithLink-1.html", "getTextFromTableRowWithLink - 1");
        assertEquals("Should find matching text", "Data Text 2. Other Link 2.",
                driverWrapper.getTextFromTableRowWithLink("Heading Text 2."));
    }

    /**
     * Tests <code>getTextFromTableRowWithLink()</code> with a non-matching example.
     */
    @Test
    public void getTextFromTableRowWithLinkNonMatching() {
        browseTo("/getTextFromTableRowWithLink-1.html", "getTextFromTableRowWithLink - 1");
        assertEquals("Should not find any matching text",
                "", driverWrapper.getTextFromTableRowWithLink("Heading Text 4."));
    }

    /**
     * Tests <code>getTextFromTableColumn()</code> with a matching example.
     */
    @Test
    public void getTextFromTableColumnMatching() {
        browseTo("/getTextFromTableColumn-1.html", "getTextFromTableColumn - 1");
        assertEquals("Should find matching text",
                "Body3", driverWrapper.getTextFromTableColumn("Header3"));
    }

    /**
     * Tests <code>getTextFromTableColumn()</code> with a non matching example.
     */
    @Test(expected = NoSuchElementException.class)
    public void getTextFromTableColumnNotMatching() {
        browseTo("/getTextFromTableColumn-1.html", "getTextFromTableColumn - 1");
        driverWrapper.getTextFromTableColumn("Header5");
    }

    /**
     * Tests <code>getTextFromUnorderedList()</code> with a matching example.
     */
    @Test
    public void getTextFromUnorderedListMatching() {
        browseTo("/getTextFromUnorderedList-1.html", "getTextFromUnorderedList - 1");

        // note that element text gets trimmed
        assertEquals("Should find matching text",
                "Item 1Item 2Item 3", driverWrapper.getTextFromUnorderedList("Heading"));
    }

    /**
     * Tests <code>getTextFromUnorderedList()</code> with a non matching example.
     */
    @Test(expected = NoSuchElementException.class)
    public void getTextFromUnorderedListNotMatching() {
        browseTo("/getTextFromUnorderedList-1.html", "getTextFromUnorderedList - 1");
        driverWrapper.getTextFromUnorderedList("Wibble");
    }

    /**
     * Tests <code>getTextFromDefinitionList()</code> with a matching example.
     */
    @Test
    public void getTextFromDefinitionListMatching() {
        browseTo("/getTextFromDefinitionList-1.html", "getTextFromDefinitionList - 1");
        assertEquals("Should find matching text",
                "Value 2", driverWrapper.getTextFromDefinitionList("Item 2"));
    }

    /**
     * Tests <code>getTextFromDefinitionList()</code> with a non matching example.
     */
    @Test(expected = NoSuchElementException.class)
    public void getTextFromDefinitionListNotMatching() {
        browseTo("/getTextFromDefinitionList-1.html", "getTextFromDefinitionList - 1");
        driverWrapper.getTextFromDefinitionList("Item 4");
    }

    /**
     * Tests <code>getTextFromHeading()</code> with a matching example.
     */
    @Test
    public void getTextFromHeadingMatching() {
        browseTo("/getTextFromHeading-1.html", "getTextFromHeading - 1");
        assertEquals("Should find matching text",
                "Span 1Span 2Span 3", driverWrapper.getTextFromHeading("Heading Text"));
    }

    /**
     * Tests <code>getTextFromHeading()</code> with a non matching example.
     */
    @Test
    public void getTextFromHeadingNotMatching() {
        browseTo("/getTextFromHeading-1.html", "getTextFromHeading - 1");
        assertEquals("Should not find matching text", "",
                driverWrapper.getTextFromHeading("Other Text"));
    }

    /**
     * Tests <code>getRelativeTextFromHeading()</code> with a matching example.
     */
    @Test
    public void getRelativeTextFromHeadingMatching() {
        browseTo("/getRelativeTextFromHeading-1.html", "getRelativeTextFromHeading - 1");
        assertEquals("Should find matching text", "Following text.",
                driverWrapper.getRelativeTextFromHeading("Heading Text"));
    }

    /**
     * Tests <code>getRelativeTextFromHeading()</code> with a non matching example.
     */
    @Test(expected = NoSuchElementException.class)
    public void getRelativeTextFromHeadingNotMatching() {
        browseTo("/getRelativeTextFromHeading-1.html", "getRelativeTextFromHeading - 1");
        driverWrapper.getRelativeTextFromHeading("Wibble");
    }

    /**
     * Tests <code>checkCurrentPageTitle()</code> with a matching example.
     */
    @Test
    public void checkCurrentPageTitleMatching() {
        driverWrapper.browseTo("/getRelativeTextFromHeading-1.html");
        driverWrapper.checkCurrentPageTitle("getRelativeTextFromHeading - 1");
    }

    /**
     * Tests <code>checkCurrentPageTitle()</code> with a non matching example.
     */
    @Test(expected = WrongPageException.class)
    public void checkCurrentPageTitleNotMatching() {
        driverWrapper.browseTo("/getRelativeTextFromHeading-1.html");
        driverWrapper.checkCurrentPageTitle("wibble");
    }

    /**
     * Tests <code>selectRadio()</code> with a matching example.
     */
    @Test
    public void selectRadioMatching() {
        browseTo("/selectRadio-1.html", "selectRadio - 1");
        driverWrapper.selectRadio("Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio"));

    }

    /**
     * Tests <code>selectRadio()</code> with a non matching example.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioNotMatching() {
        browseTo("/selectRadio-1.html", "selectRadio - 1");
        driverWrapper.selectRadio("Test Radio Button 3");
    }

    /**
     * Tests <code>selectRadio()</code> with a matching example (radio nested inside the label).
     */
    @Test
    public void selectRadioNestedMatching() {
        browseTo("/selectRadio-2.html", "selectRadio - 2");
        driverWrapper.selectRadio("Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio"));
    }

    /**
     * Tests <code>selectRadio()</code> with a non matching example.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioNestedNotMatching() {
        browseTo("/selectRadio-2.html", "selectRadio - 2");
        driverWrapper.selectRadio("Test Radio Button 3");
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with a matching example.
     */
    @Test
    public void selectRadioMatchingInFieldset1() {
        browseTo("/selectRadioInFieldset-1.html", "selectRadioInFieldset - 1");
        driverWrapper.selectRadioInFieldset("First Fieldset", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio1"));
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with another matching example.
     */
    @Test
    public void selectRadioMatchingInFieldset2() {
        browseTo("/selectRadioInFieldset-1.html", "selectRadioInFieldset - 1");
        driverWrapper.selectRadioInFieldset("Second Fieldset", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio2"));
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with a non matching example (no fieldset).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInFieldsetNotMatching1() {
        browseTo("/selectRadioInFieldset-1.html", "selectRadioInFieldset - 1");
        driverWrapper.selectRadioInFieldset("Third Fieldset", "Test Radio Button 1");
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with a non matching example (no radio).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInFieldsetNotMatching2() {
        browseTo("/selectRadioInFieldset-1.html", "selectRadioInFieldset - 1");
        driverWrapper.selectRadioInFieldset("Second Fieldset", "Test Radio Button 3");
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with a matching example (radio nested inside the label).
     */
    @Test
    public void selectRadioInFieldsetNestedMatching1() {
        browseTo("/selectRadioInFieldset-2.html", "selectRadioInFieldset - 2");
        driverWrapper.selectRadioInFieldset("First Fieldset", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio1"));
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with a matching example (radio nested inside the label).
     */
    @Test
    public void selectRadioInFieldsetNestedMatching2() {
        browseTo("/selectRadioInFieldset-2.html", "selectRadioInFieldset - 2");
        driverWrapper.selectRadioInFieldset("Second Fieldset", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio2"));
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with a non matching example (no such fieldset for radio nested
     * inside the label).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInFieldsetNestedNotMatching1() {
        browseTo("/selectRadioInFieldset-2.html", "selectRadioInFieldset - 2");
        driverWrapper.selectRadioInFieldset("Third Fieldset", "Test Radio Button 1");
    }

    /**
     * Tests <code>selectRadioInFieldset()</code> with a non matching example (no such radio nested
     * inside the label).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInFieldsetNestedNotMatching2() {
        browseTo("/selectRadioInFieldset-2.html", "selectRadioInFieldset - 2");
        driverWrapper.selectRadioInFieldset("Second Fieldset", "Test Radio Button 3");
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with a matching example.
     */
    @Test
    public void selectRadioMatchingInNestedFieldset1() {
        browseTo("/selectRadioInNestedFieldset-1.html", "selectRadioInNestedFieldset - 1");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 1", "Nested Fieldset A", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio1"));
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with another matching example.
     */
    @Test
    public void selectRadioMatchingInNestedFieldset2() {
        browseTo("/selectRadioInNestedFieldset-1.html", "selectRadioInNestedFieldset - 1");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 2", "Nested Fieldset B", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio7"));
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with a non matching example (no fieldset).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInNestedFieldsetNotMatching1() {
        browseTo("/selectRadioInNestedFieldset-1.html", "selectRadioInNestedFieldset - 1");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 1", "Nested Fieldset C", "Test Radio Button 1");
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with a non matching example (no radio).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInNestedFieldsetNotMatching2() {
        browseTo("/selectRadioInNestedFieldset-1.html", "selectRadioInNestedFieldset - 1");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 1","Nested Fieldset A", "Test Radio Button 3");
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with a matching example (radio nested inside the label).
     */
    @Test
    public void selectRadioInNestedFieldsetNestedMatching1() {
        browseTo("/selectRadioInNestedFieldset-2.html", "selectRadioInNestedFieldset - 2");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 1","Nested Fieldset A", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio1"));
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with a matching example (radio nested inside the label).
     */
    @Test
    public void selectRadioInNestedFieldsetNestedMatching2() {
        browseTo("/selectRadioInNestedFieldset-2.html", "selectRadioInNestedFieldset - 2");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 2","Nested Fieldset B", "Test Radio Button 1");
        assertTrue("Radio button should have been selected", driverWrapper.isElementChecked("my_radio7"));
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with a non matching example (no such fieldset for radio nested
     * inside the label).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInNestedFieldsetNestedNotMatching1() {
        browseTo("/selectRadioInNestedFieldset-2.html", "selectRadioInNestedFieldset - 2");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 3","Nested Fieldset A", "Test Radio Button 1");
    }

    /**
     * Tests <code>selectRadioInNestedFieldset()</code> with a non matching example (no such radio nested
     * inside the label).
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectRadioInNestedFieldsetNestedNotMatching2() {
        browseTo("/selectRadioInNestedFieldset-2.html", "selectRadioInNestedFieldset - 2");
        driverWrapper.selectRadioInNestedFieldset(
                "Fieldset 1","Nested Fieldset A", "Test Radio Button 3");
    }

    /**
     * Tests <code>selectCheckbox()</code> with a matching example.
     */
    @Test
    public void selectCheckboxMatching() {
        browseTo("/selectCheckbox-1.html", "selectCheckbox - 1");
        driverWrapper.selectCheckbox("Test Checkbox Button");
        assertTrue("Checkbox button should have been selected",
                driverWrapper.isElementChecked("my_input"));

    }

    /**
     * Tests <code>selectCheckbox()</code> with a non matching example.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectCheckboxNotMatching() {
        browseTo("/selectCheckbox-1.html", "selectCheckbox - 1");
        driverWrapper.selectCheckbox("Other Checkbox Button");
    }

    /**
     * Tests <code>selectCheckbox()</code> with a matching example (input nested inside the label).
     */
    @Test
    public void selectCheckboxNestedMatching() {
        browseTo("/selectCheckbox-2.html", "selectCheckbox - 2");
        driverWrapper.selectCheckbox("Test Checkbox Button");
        assertTrue("Checkbox button should have been selected",
                driverWrapper.isElementChecked("my_input"));

    }

    /**
     * Tests <code>selectCheckbox()</code> with a non matching example.
     */
    @Test(expected = IllegalArgumentException.class)
    public void selectCheckboxNestedNotMatching() {
        browseTo("/selectCheckbox-2.html", "selectCheckbox - 2");
        driverWrapper.selectCheckbox("Other Checkbox Button");
    }

    /**
     * Tests <code>containsMessage()</code> with a matching example.
     */
    @Test
    public void containsMessageMatching() {
        browseTo("/containsMessage-1.html", "containsMessage - 1");
        assertTrue("Message should be found",
                driverWrapper.containsMessage("This is a really long and unique message that something happened."));
    }

    /**
     * Tests <code>containsMessage()</code> with a matching example using escaping.
     */
    @Test
    public void containsMessageMatchingEscaped() {
        browseTo("/containsMessage-2.html", "containsMessage - 2");
        assertTrue("Message should be found",
                driverWrapper.containsMessage("This is a really 'long' and unique message that 'something' happened."));
    }

    /**
     * Tests <code>containsMessage()</code> with a non matching example.
     */
    @Test
    public void containsMessageNotMatching() {
        browseTo("/selectCheckbox-2.html", "selectCheckbox - 2");
        assertFalse("Message should not be found",
                driverWrapper.containsMessage("This is a really long and unique message that something happened."));
    }

    /**
     * Tests <code>checkTextFromAnyTableRow()</code> with a matching example.
     */
    @Test
    public void checkTextFromAnyTableRowMatching() {
        browseTo("/checkTextFromAnyTableRow-1.html", "checkTextFromAnyTableRow - 1");
        assertTrue("Text should be found",
                driverWrapper.checkTextFromAnyTableRow("AAA", "ccc \nsss"));
    }

    /**
     * Tests <code>checkTextFromAnyTableRow()</code> with a non matching example (no text).
     */
    @Test
    public void checkTextFromAnyTableRowNotMatchingText() {
        browseTo("/checkTextFromAnyTableRow-1.html", "checkTextFromAnyTableRow - 1");
        assertFalse("Text should not be found",
                driverWrapper.checkTextFromAnyTableRow("BBB", "eee \nsss"));
    }

    /**
     * Tests <code>checkTextFromAnyTableRow()</code> with a non matching example (no matching row).
     */
    @Test
    public void checkTextFromAnyTableRowNotMatchingRow() {
        browseTo("/checkTextFromAnyTableRow-1.html", "checkTextFromAnyTableRow - 1");
        assertFalse("Text should not be found",
                driverWrapper.checkTextFromAnyTableRow("CCC", "ccc \nsss"));
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
    private static class HtmlUnitWebDriverWrapper extends WebDriverWrapper {

        /** Used in checks below. */
        private WebDriver webDriver;

        /**
         * Creates a new instance.
         *
         * @param env The mocked testsuite.properties
         */
        HtmlUnitWebDriverWrapper(Environment env) {
            super(env);
        }

        /**
         * Swaps Chrome with HtmlUnit, running in Chrome emulation mode.
         *
         * @return The web driver instance
         */
        @Override
        protected WebDriver createWebDriver() {
            this.webDriver = new HtmlUnitDriver(BrowserVersion.CHROME);
            return webDriver;
        }

        /**
         * Wait for the web page to fully re-load, and any onload javascript events to complete.
         */
        @Override
        protected void waitForPageLoad() {
            // does nothing
        }

        /**
         * Used to check the outcome of tests on radio and checkbox buttons.
         * @return <code>true</code> if button was checked.
         */
        boolean isElementChecked(String id) {
            return webDriver.findElement(By.id(id)).isSelected();
        }
    }
}
