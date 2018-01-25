package uk.gov.dvsa.mot.framework.pdf;

/**
 * Exception class for loading PDF files.
 */
public class PdfException extends Exception {

    /**
     * Exception loading PDF file.
     * @param errorMessage  The error message
     * @param exception     The throwable exception
     */
    public PdfException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
