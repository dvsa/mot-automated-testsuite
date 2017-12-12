package uk.gov.dvsa.mot.framework.pdf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class PdfDocumentTest {

    /**
     * Tests the PdfDocument contains method can read all values off a VT20.
     */
    @Test
    public void testPdfContainsVt20() {
        // The PDF file
        String fileName = new File("").getAbsolutePath() + "/src/test/resources/examplePdf/vt20.pdf";

        // The test values that are in the PDF
        ArrayList<String> values = new ArrayList<String>();
        values.add("VT20");
        values.add("195890049269");
        values.add("AI74IVW");
        values.add("WVIIAAAAAAA146584");
        values.add("PEUGEOT");
        values.add("BOXER");
        values.add("Grey");
        values.add("C. M. Weatherbee");
        values.add("Great Britain");
        values.add("11 December 2018 (EIGHTEEN)");
        values.add("12 Dec 2017");
        values.add("VTS076410");
        values.add("123456 mi");

        PdfDocument pdfDocument = loadLocalPdfDocument(fileName);
        try {
            assertTrue("PDF does not contain expected values", pdfDocument.contains(values));
        } catch (PdfException ex) {
            throw new RuntimeException("Error checking PDF", ex);
        }
    }

    /**
     * Tests the PdfDocument contains method can read all values off a VT30.
     */
    @Test
    public void testPdfContainsVt30() {
        // The PDF file
        String fileName = new File("").getAbsolutePath() + "/src/test/resources/examplePdf/vt30.pdf";

        // The test values that are in the PDF
        ArrayList<String> values = new ArrayList<String>();
        values.add("VT30");
        values.add("709674994171");
        values.add("AG74PSK");
        values.add("KSPGAAAAAAA116074");
        values.add("BMW");
        values.add("325");
        values.add("Blue");
        values.add("C. M. Weatherbee");
        values.add("Great Britain");
        values.add("1 March 2002");
        values.add("Unreadable");
        values.add("11 Dec 2017");
        values.add("VTS076410");

        PdfDocument pdfDocument = loadLocalPdfDocument(fileName);
        try {
            assertTrue("PDF does not contain expected values", pdfDocument.contains(values));
        } catch (PdfException ex) {
            throw new RuntimeException("Error checking PDF", ex);
        }
    }

    /**
     * Tests the PdfDocument contains returns false when value does not exist.
     */
    @Test
    public void testPdfNotContainsVt20() {
        // The PDF file
        String fileName = new File("").getAbsolutePath() + "/src/test/resources/examplePdf/vt20.pdf";

        // The test values that are in the PDF
        ArrayList<String> values = new ArrayList<String>();
        values.add("VT30");

        PdfDocument pdfDocument = loadLocalPdfDocument(fileName);
        try {
            assertFalse("PDF should not contain expected values", pdfDocument.contains(values));
        } catch (PdfException ex) {
            throw new RuntimeException("Error checking PDF", ex);
        }
    }

    /**
     * Tests the PdfDocument containsValueNextToLabel.
     */
    @Test
    public void testPdfContainsValueNextToLabel() {
        // The PDF file
        String fileName = new File("").getAbsolutePath() + "/src/test/resources/examplePdf/vt20.pdf";

        PdfDocument pdfDocument = loadLocalPdfDocument(fileName);
        try {
            assertTrue("PDF should contain value next to label",
                    pdfDocument.containsValueNextToLabel("PEUGEOT", "BOXER"));
        } catch (PdfException ex) {
            throw new RuntimeException("Error checking PDF", ex);
        }
    }

    /**
     * Tests the PdfDocument containsValueBelowTheLabel.
     */
    @Test
    public void testPdfContainsValueBelowTheLabel() {
        // The PDF file
        String fileName = new File("").getAbsolutePath() + "/src/test/resources/examplePdf/vt20.pdf";

        PdfDocument pdfDocument = loadLocalPdfDocument(fileName);
        try {
            assertTrue("PDF should contain value below label",
                    pdfDocument.containsValueBelowTheLabel("Make", "PEUGEOT"));
        } catch (PdfException ex) {
            throw new RuntimeException("Error checking PDF", ex);
        }
    }

    /**
     * Loads a local test file by filename.
     * @param fileName      The PDF file
     * @return              The PDFDocument for the file
     */
    private PdfDocument loadLocalPdfDocument(String fileName) {
        try {
            FileInputStream fs = new FileInputStream(fileName);

            return new PdfDocument(PDDocument.load(fs));
        } catch (Exception ex) {
            throw new RuntimeException("Error loading PDF", ex);
        }
    }
}
