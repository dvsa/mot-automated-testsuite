package uk.gov.dvsa.mot.framework;

/**
 * Implementation of the TestDataProvider interface. This implementation returns hard coded responses as a direct
 * replacement for the test data being directly coded in feature files.
 */
public class StaticTestDataProvider implements TestDataProvider {

    @Override
    public String getValidUserOfType(String userType) {
        return "DERE3460";
    }
}
