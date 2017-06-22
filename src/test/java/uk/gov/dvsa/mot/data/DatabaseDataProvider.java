package uk.gov.dvsa.mot.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Handles provision of test data, using existing data from a test database.
 */
public class DatabaseDataProvider {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataProvider.class);

    /** The DataDao to use. */
    private final DataDao dataDao;

    /** The cached datasets to use, keyed by name. */
    private Map<String, List<List<String>>> datasets = null;

    /**
     * Creates a new instance.
     * @param dataDao   The user DAO to use
     */
    public DatabaseDataProvider(DataDao dataDao) {
        logger.debug("Creating DatabaseDataProvider...");
        this.dataDao = dataDao;
    }

    /**
     * Loads all test data sets, populating the in-memory cache.
     * If called multiple times, does nothing once the cache has been populated.
     */
    @Transactional(readOnly = true)
    public void loadAllDatasets() {
        if (datasets == null) {
            datasets = dataDao.loadAllDatasets();
            logger.debug("{} datasets loaded", datasets.size());
        } else {
            logger.debug("datasets already loaded");
        }
    }

    /**
     * Get an entry from the specified test data set, and removes it from the in-memory cache.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    public List<String> getDatasetEntry(String dataSetName) {
        if (datasets == null) {
            String message = "No datasets loaded, please call \"loadAllDatasets\" first";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        List<List<String>> dataset;
        if (!datasets.containsKey(dataSetName)) {
            String message = "Unknown dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);

        } else {
            dataset = datasets.get(dataSetName);
        }

        if (dataset.size() < 1) {
            String message = "No more data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        List<String> entry = dataset.get(0);
        dataset.remove(entry);

        logger.debug("Using {} from dataset {}", entry, dataSetName);
        return entry;
    }
}
