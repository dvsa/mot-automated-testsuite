package uk.gov.dvsa.mot.framework.csv;

/**
 * Exception class for loading CSV files.
 */
public class CsvException extends Exception {

    /**
     * Exception loading CSV file.
     * @param errorMessage  The error message
     * @param exception     The throwable exception
     */
    public CsvException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
