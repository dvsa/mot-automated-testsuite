package uk.gov.dvsa.mot.framework.xml;

/**
 * Exception class for loading XML files.
 */
public class XmlException extends Exception {

    /**
     * Exception loading XML file.
     * @param errorMessage  The error message
     * @param exception     The throwable exception
     */
    public XmlException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
