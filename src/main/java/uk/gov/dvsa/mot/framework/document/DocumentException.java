package uk.gov.dvsa.mot.framework.document;

/**
 * Exception class for loading XML files.
 */
public class DocumentException extends Exception {

    /**
     * Exception loading document.
     * @param errorMessage  The error message
     * @param exception     The throwable exception
     */
    public DocumentException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
