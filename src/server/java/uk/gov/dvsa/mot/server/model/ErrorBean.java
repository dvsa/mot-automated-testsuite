package uk.gov.dvsa.mot.server.model;

/**
 * Immutable javabean that encapsulates an error response.
 */
public class ErrorBean {

    private String message;

    public ErrorBean(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
