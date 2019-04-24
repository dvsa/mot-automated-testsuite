package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Step definitions specific to the verifying PDFs and CSV Documents.
 */
public class DocumentStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DocumentStepDefinitions.class);

    /** The web driver to use. */
    private final WebDriverWrapper driverWrapper;

    /**
     * Creates a new instance.
     * @param driverWrapper The driver wrapper to use
     */
    @Inject
    public DocumentStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating DocumentStepDefinitions...");
        this.driverWrapper = driverWrapper;

        And("^I click \"([^\"]+)\" and check the PDF contains:$",
                (String link, DataTable table) -> assertTrue(pdfContainsData(link, table)));

        And("^I click the certificate link and check the MOT certificate PDF contains:$",
                (DataTable table) -> assertTrue(motCertificatePdfContainsData(table)));

        And("^I click \"([^\"]+)\" and check the CSV contains:$",
                (String link, DataTable table) -> assertTrue(csvContainsData(link, table)));
    }

    /**
     * Converts the raw data array and verifies all values are contained within the PDF.
     * @param rawData   The raw list of data items to check for
     * @return          Whether all data items were present in the PDF
     */
    private boolean motCertificatePdfContainsData(DataTable rawData) {
        List<String> rawDataRows = rawData.asList(String.class);
        List<String> processedDataRows = new ArrayList<>();
        for (String dataItem : rawDataRows) {
            processedDataRows.add(getStringValue(dataItem));
        }

        return driverWrapper.motCertPdfContains(processedDataRows);
    }

    /**
     * Converts the raw data array and verifies all values are contained within the PDF.
     * @param link      The link to the PDF
     * @param rawData   The raw list of data items to check for
     * @return          Whether all data items were present in the PDF
     */
    private boolean pdfContainsData(String link, DataTable rawData) {
        List<String> rawDataRows = rawData.asList(String.class);
        List<String> processedDataRows = new ArrayList<String>();
        for (String dataItem : rawDataRows) {
            processedDataRows.add(getStringValue(dataItem));
        }

        return driverWrapper.pdfContains(link, processedDataRows);
    }

    /**
     * Converts the raw data array and verifies all values are contained within the CSV.
     * @param link      The link to the CSV
     * @param rawData   The raw list of data items to check for
     * @return          Whether all data items were present in the CSV
     */
    private boolean csvContainsData(String link, DataTable rawData) {
        List<String> rawDataRows = rawData.asList(String.class);
        List<String> processedDataRows = new ArrayList<String>();
        for (String dataItem : rawDataRows) {
            processedDataRows.add(getStringValue(dataItem));
        }

        return driverWrapper.csvContains(link, processedDataRows);
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
