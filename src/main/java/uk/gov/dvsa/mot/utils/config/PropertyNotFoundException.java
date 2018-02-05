package uk.gov.dvsa.mot.utils.config;

public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
