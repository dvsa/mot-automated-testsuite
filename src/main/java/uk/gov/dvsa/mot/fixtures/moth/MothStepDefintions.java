package uk.gov.dvsa.mot.fixtures.moth;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

public class MothStepDefintions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(MothStepDefintions.class);

    /** The driverwrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Create a new instance.
     * @param driverWrapper The web driver wrapper to be used
     */
    @Inject
    public MothStepDefintions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        When("^I enter \"([^\"]+)\" in the registration field$", (String text) ->
                driverWrapper.enterIntoFieldWithLabel(" Registration number (number plate) ", text));

        When("^I enter \\{([^\\}]+)\\} in the registration field$", (String dataKey) ->
                driverWrapper.enterIntoFieldWithLabel(" Registration number (number plate) ",
                        driverWrapper.getData(dataKey)));

        When("^I click the last \"([^\"]+)\" text$", (String linkText) ->
                driverWrapper.clickLastText(linkText));

        When("^I go to the next tab$", () -> driverWrapper.goNextTab());

        When("^I close extra tabs$", () -> driverWrapper.closeTabs());

        And("^I check whether MOT history for vehicle with \"([^\"]+)\" VRM, " +
                        "\"([^\"]+)\" VIN and \"([^\"]+)\" test number contains:$",
                (String Vrm, String Vin, DataTable table) -> {
                });
    }

    private void mothNormalTestContains(String Vrm, String Vin, String testNumber) {
        try {
            File inputFile = new File("input.plain");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getOwnerDocument().normalize();

            NodeList nList = doc.getElementsByTagName("student");


            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mothContingencyTestContains(String Vrm, String Vin, String testNumber) {
        try {
            File inputFile = new File("input.plain");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getOwnerDocument().normalize();

            NodeList nList = doc.getElementsByTagName("student");


            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the data provider value or the value of the text.
     * @param text  The name of the value
     * @return      The data provider value of the original text
     */
    private String getStringValue(String text) {
        if (text.startsWith("{") && text.endsWith("}")) {
            return driverWrapper.getData(text.substring(1, text.length() - 1));
        }
        return text;
    }
}
