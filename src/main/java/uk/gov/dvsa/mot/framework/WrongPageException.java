package uk.gov.dvsa.mot.framework;

/**
 * Encapsulates the web browser not being on the expected web page.
 */
public class WrongPageException extends RuntimeException {

    /**
     * Creates a new instance.
     * @param expected  The expected page title
     * @param actual    The actual page title
     */
    public WrongPageException(String expected, String actual) {
        super("Expected page was: '" + expected + "', but the actual page was: '" + actual + "'");
    }
}
