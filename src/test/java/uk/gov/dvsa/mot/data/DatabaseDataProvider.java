package uk.gov.dvsa.mot.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles provision of test data, using existing data from a test database.
 */
public class DatabaseDataProvider implements DataProvider {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataProvider.class);

    /** The DataDao to use. */
    private final DataDao dataDao;

    /**
     * Creates a new instance.
     * @param dataDao   The user DAO to use
     */
    public DatabaseDataProvider(DataDao dataDao) {
        logger.debug("Creating DatabaseDataProvider...");
        this.dataDao = dataDao;
    }

    /**
     * Loads a user of the specified user type.
     * @param userType The user type
     * @return The username
     */
    @Transactional(readOnly = true)
    @Override
    public String loadUser(String userType) {
        /*
         * TODO: If using a new runner with each test running in a separate thread, then this class can be updated to
          * load each dataset once then pass out one item (e.g. username) to each test as needed, so each test gets
           * unique data
           *
           * if can't get new runner to work, then will need global work around to achieve same thing across multiple
           * processes (shared file with locks?) - slower and more brittle
         */
        logger.debug("Loading user type {}", userType);
        String username = dataDao.loadUser(userType);
        logger.debug("Found username {}", username);
        return username;
    }
}
