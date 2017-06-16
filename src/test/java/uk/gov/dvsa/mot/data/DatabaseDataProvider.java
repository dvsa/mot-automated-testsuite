package uk.gov.dvsa.mot.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Handles provision of test data, using existing data from a test database.
 */
public class DatabaseDataProvider {

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
     * Loads an entry from the specified test data set.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    @Transactional(readOnly = true)
    public List<String> loadData(String dataSetName) {
        /*
         * TODO: If using a new runner with each test running in a separate thread, then this class can be updated to
          * load each dataset once then pass out one item (e.g. username) to each test as needed, so each test gets
           * unique data
           *
           * if can't get new runner to work, then will need global work around to achieve same thing across multiple
           * processes (shared file with locks?) - slower and more brittle
         */
        logger.debug("Loading an entry from data set {}", dataSetName);
        List<String> data = dataDao.loadData(dataSetName);
        logger.debug("Found data {}", data);
        return data;
    }
}
