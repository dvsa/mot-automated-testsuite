package uk.gov.dvsa.mot.data;

/**
 * Handles provision of test data.
 */
public interface DataProvider {

    /**
     * Loads a user of the specified user type.
     * @param userType      The user type
     * @return The username
     */
    String loadUser(String userType);
}
