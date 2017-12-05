package uk.gov.dvsa.mot.fixtures.generic;


import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import cucumber.api.java8.En;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;


import javax.inject.Inject;

public class PdfStepDefinitions  implements En {
    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataStepDefinitions.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    private PDDocument pdfDocument;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     */
    @Inject
    public PdfStepDefinitions(WebDriverWrapper driverWrapper) {
        this.driverWrapper = driverWrapper;

        Then("^I download PDF file from \"([^\"]+)\" url$", (String url) -> {
            pdfDocument = driverWrapper.getPdfFromUrl(url);
            assertFalse(pdfDocument == null);
        });

        And("^I check if the downloaded PDF contains \"([^\"]+)\"$", (String text) ->
                assertTrue(driverWrapper.contains(pdfDocument, text)));

        And("^PDF field value below \"([^\"]+)\" label, equals \"([^\"]+)\".$", (String label, String text) ->
        {});

        And("^I get PDF field value next to \"([^\"]+)\" label$", (String buttonText) ->
                assertTrue(driverWrapper.isButtonDisabled(buttonText)));
    }
}
