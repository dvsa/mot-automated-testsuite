package uk.gov.dvsa.mot.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    private final Map<String, List<List<String>>> datasets;

    /**
     * Creates a new instance.
     * @param dataDao   The user DAO to use
     */
    public DatabaseDataProvider(DataDao dataDao) {
        logger.debug("Creating DatabaseDataProvider...");
        this.dataDao = dataDao;
        this.datasets = new HashMap<>();
    }

    /**
     * Get a cached entry from the specified test data set (loading and caching if needed), and removes it from the
     * in-memory cache.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    @Transactional
    public List<String> getCachedDatasetEntry(String dataSetName) {
        List<List<String>> dataset;
        if (!datasets.containsKey(dataSetName)) {
            // dataset not cached yet, so load and cache it
            dataset = dataDao.loadDataset(dataSetName);
            datasets.put(dataSetName, dataset);

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

    /**
     * Get the first entry from the specified test data set, by executing the query immediately, ignoring any caches.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    @Transactional
    public List<String> getUncachedDatasetEntry(String dataSetName) {
        List<List<String>> dataset = dataDao.loadDataset(dataSetName);

        if (dataset.size() < 1) {
            String message = "No more data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        List<String> entry = dataset.get(0);

        logger.debug("Using {} from dataset {}", entry, dataSetName);
        return entry;
    }

    /**
     * Get the specified number of entries from the specified test data set, by executing the query immediately,
     * ignoring any caches.
     * @param dataSetName   The data set name
     * @param length        The number of data set entries
     * @return The data entries
     */
    @Transactional
    public List<List<String>> getUncachedDataset(String dataSetName, int length) {
        List<List<String>> dataset = dataDao.loadDataset(dataSetName, length);

        if (dataset.size() < 1) {
            String message = "No data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        return dataset;
    }
}
