package uk.gov.dvsa.mot.framework;

/**
 * Interface for a data provider. This allows us to switch out implementations as needed.
 */
public interface TestDataProvider {
    String getValidUserOfType(String userType);
}
