package uk.gov.dvsa.mot.data;

/**
 * Handles database queries to load data.
 */
public interface DataDao {

    /**
     * Loads a user of the specified user type.
     * @param userType      The user type
     * @return The username
     */
    String loadUser(String userType);
}
