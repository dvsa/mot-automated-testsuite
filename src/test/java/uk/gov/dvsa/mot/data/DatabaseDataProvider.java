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
    private final Map<String, List<List<String>>> datasets = new HashMap<>();

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
        List<List<String>> dataset;
        if (!datasets.containsKey(dataSetName)) {
            // need to load the dataset
            dataset = dataDao.loadDataset(dataSetName);

            // add to in-memory cache
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
}
