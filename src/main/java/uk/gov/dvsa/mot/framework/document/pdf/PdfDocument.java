package uk.gov.dvsa.mot.framework.document.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.document.Document.IDocument;
import uk.gov.dvsa.mot.framework.document.Document.Type;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Class for PDF Documents.
 */
public class PdfDocument implements IDocument {
    private final static Type fileType = Type.PDF;

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(PdfDocument.class);

    /** The underlying PDF document. */
    private PDDocument pdDocument;

    /**
     * public constructor.
     * @param rawPdf    raw PDF document string
     */
    public PdfDocument(String rawPdf) throws PdfException {
        InputStream pdfStream = new ByteArrayInputStream(rawPdf.getBytes());

        try {
            pdDocument = PDDocument.load(pdfStream);
        } catch (Exception ex) {
            throw new PdfException("Failed to load a PDF document.", ex);
        }
    }

    /**
     * Whether this PDF document contains all the string values.
     * @param values            list of string values to check for
     * @return                  whether all strings are contained within the PDF
     * @throws PdfException     error retrieving PDF contents
     */
    @Override
    public boolean contains(List<String> values) throws Exception {
        try {

            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setSortByPosition(true);

            String pdfContents = textStripper.getText(this.pdDocument);
            if (logger.isDebugEnabled()) {
                logger.debug("PDF Contents: " + pdfContents);
            }

            for (String text : values) {
                if (!pdfContents.contains(text)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("PDF does not contain: " + text);
                    }
                    return false;
                }
            }

            // All values must exist
            return true;
        } catch (IOException io) {
        throw new PdfException(String.format("Failed to find %s values in the PDF document.",
                String.join(", ", values)), io);
    }
    }

    @Override
    public boolean contains(String value) {
        return false;
    }

    @Override
    public String parse() {
        return null;
    }

    @Override
    public void write(String filePath, Optional<Boolean> overwrite) {

    }

    @Override
    public Type getFileType() {
        return fileType;
    }

    @Override
    public String getExtension() {
        return null;
    }

    /**
     * Find the label in the PDF document, and check if it contains the text pattern.
     * @param label             label used to locate the text.
     * @param text              to look for in the document.
     * @return                  whether the value is next to the label
     * @throws PdfException     error parsing PDF document
     */
    public boolean containsValueNextToLabel(String label, String text) throws PdfException {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setSortByPosition(true);

            String document = textStripper.getText(this.pdDocument);
            String[] lines = document.split(System.getProperty("line.separator"));

            for (int i = 0; i < lines.length; ++i) {
                if (lines[i].contains(label) && lines[i].contains(text)) {
                    return true;
                }
            }

            // Value not found
            return false;
        } catch (IOException io) {
            throw new PdfException(String.format("Failed to find '%s' pattern for the %s label.", text, label), io);
        }
    }

    /**
     * Find the label in the PDF document, and check if it contains the text pattern.
     * @param label label used to locate the text.
     * @param text to look for in the document.
     * @return whether the value is below the label
     * @throws PdfException     error parsing PDF document
     */
    public boolean containsValueBelowTheLabel(String label, String text) throws PdfException {
        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setSortByPosition(true);

            String document = textStripper.getText(this.pdDocument);
            String[] lines = document.split(System.getProperty("line.separator"));

            for (int i = 0; i < lines.length; ++i) {
                if (lines[i].contains(label)) {
                    if (lines.length >= i + 1 && lines[i + 1].contains(text)) {
                        return true;
                    }
                }
            }

            // Value not found
            return false;
        } catch (IOException io) {
            throw new PdfException(String.format("Failed to find '%s' pattern for the %s label.", text, label), io);
        }
    }
}
