package uk.gov.dvsa.mot.framework.excel;

/**
 * Exception class for loading Excel files.
 */
public class ExcelException extends Exception {

    /**
     * Exception loading Excel file.
     * @param errorMessage  The error message
     * @param exception     The throwable exception
     */
    public ExcelException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
