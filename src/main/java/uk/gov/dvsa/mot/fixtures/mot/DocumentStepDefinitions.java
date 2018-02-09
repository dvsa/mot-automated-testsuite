package uk.gov.dvsa.mot.fixtures.mot;

import static junit.framework.TestCase.assertTrue;

import cucumber.api.DataTable;
import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.framework.document.csv.CsvDocument;
import uk.gov.dvsa.mot.framework.document.pdf.PdfDocument;
import uk.gov.dvsa.mot.framework.document.pdf.PdfException;
import uk.gov.dvsa.mot.framework.document.xml.XmlDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                (String link, DataTable table) -> {
                    try {
                        PdfDocument pdfDocument = driverWrapper.createPdfDocument(link);

                        assertTrue(pdfDocument.contains(getList(table)));

                        driverWrapper.writeDocument(pdfDocument, Optional.of(false));
                    } catch (Exception exception) {
                        logger.error("Unable to load PDF document: %s", exception);
                    }
            });

        And("^I click \"([^\"]+)\" and check the CSV contains:$",
                (String link, DataTable table) -> {
                    try {
                        CsvDocument csvDocument = driverWrapper.createCsvDocument(link);

                        assertTrue(csvDocument.contains(getList(table)));

                        driverWrapper.writeDocument(csvDocument, Optional.of(false));
                    } catch (Exception exception) {
                        logger.error("Unable to load CSV document: %s", exception);
                    }
                });

        And("^I click \"([^\"]+)\" and check the XML contains:$",
                (String link, DataTable table) -> {
                    try {
                        XmlDocument csvDocument = driverWrapper.createXmlDocument(link);

                        assertTrue(csvDocument.contains(getList(table)));

                        driverWrapper.writeDocument(csvDocument, Optional.of(false));
                    } catch (Exception exception) {
                        logger.error("Unable to load XML document: %s", exception);
                    }
                });
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

    private List<String> getList(DataTable dataTable) {
        List<String> processedDataRows = new ArrayList<>();

        for (String dataItem : dataTable.asList(String.class)) {
            processedDataRows.add(getStringValue(dataItem));
        }

        return processedDataRows;
    }
}
