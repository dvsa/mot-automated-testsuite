package uk.gov.dvsa.mot.server.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dvsa.mot.server.model.Dataset;
import uk.gov.dvsa.mot.server.model.DatasetMetrics;
import uk.gov.dvsa.mot.server.reporting.DataUsageReportGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;

/**
 * Handles provision of test data, using existing data from a test database.
 */
public class DatabaseDataProvider {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataProvider.class);

    /** The DataDao to use. */
    private final DataDao dataDao;

    /** The Query file loader to use. */
    private final QueryFileLoader queryFileLoader;

    /** The data usage report generator to use. */
    private final DataUsageReportGenerator dataUsageReportGenerator;

    /** The cached datasets to use, keyed by name. */
    private final Map<String, Dataset> datasets;

    /**
     * Creates a new instance.
     * @param dataDao                       The user DAO to use
     * @param queryFileLoader               The SQL Query file loader
     * @param dataUsageReportGenerator      The data usage report generator to use
     */
    public DatabaseDataProvider(DataDao dataDao, QueryFileLoader queryFileLoader,
                                DataUsageReportGenerator dataUsageReportGenerator) {
        logger.debug("Creating DatabaseDataProvider...");
        this.dataDao = dataDao;
        this.queryFileLoader = queryFileLoader;
        this.dataUsageReportGenerator = dataUsageReportGenerator;
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
        Dataset dataset = getDataset(dataSetName);

        if (!dataset.isCachePopulated()) {
            // dataset not cached yet, so load and cache it
            final long start = System.currentTimeMillis();
            List<List<String>> results = dataDao.loadDataset(dataset, 0);
            final long stop = System.currentTimeMillis();
            dataset.populateCache(results, stop - start);
        }

        List<String> entry = dataset.getCachedResult();
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
        Dataset dataset = getDataset(dataSetName);
        DatasetMetrics metrics = dataset.getMetrics();

        long start = System.currentTimeMillis();
        List<List<String>> results = dataDao.loadDataset(dataset, 1);
        long stop = System.currentTimeMillis();

        // record dataset size, request, and query timing
        metrics.setLoadedImmediatelySize(results.size());
        metrics.increaseLoadedImmediatelyRequested();
        metrics.setTimingMilliseconds(stop - start);

        if (results.size() < 1) {
            String message = "No more data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        List<String> entry = results.get(0);
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
        Dataset dataset = getDataset(dataSetName);
        DatasetMetrics metrics = dataset.getMetrics();

        long start = System.currentTimeMillis();
        List<List<String>> results = dataDao.loadDataset(dataset, length);
        long stop = System.currentTimeMillis();

        // record dataset size, request, and query timing
        // note: could actually have used length entries, if passwords are not valid
        metrics.setLoadedImmediatelySize(results.size());
        metrics.increaseLoadedImmediatelyRequested();
        metrics.setTimingMilliseconds(stop - start);

        if (results.size() < 1) {
            String message = "No more data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        logger.debug("Using {} entries from dataset {}", results.size(), dataSetName);
        return results;
    }

    /**
     * Outputs a data usage report, called by Spring just before the data server is shutdown.
     */
    @PreDestroy
    public void outputUsageReport() {
        logger.info("In outputUsageReport...");
        dataUsageReportGenerator.generateReport(
                // extract metrics, keyed by dataset name
                datasets.values().stream().collect(Collectors.toMap(
                        dataset -> dataset.getName(), dataset -> dataset.getMetrics()))
        );
    }

    /**
     * Get the dataset details, handling loading and caching on demand when needed.
     * @param datasetName   The dataset name
     * @return The dataset details
     */
    private Dataset getDataset(String datasetName) {
        if (datasets.containsKey(datasetName)) {
            return datasets.get(datasetName);

        } else {
            Dataset dataset = queryFileLoader.loadFromFile(datasetName);
            datasets.put(datasetName, dataset);
            return dataset;
        }
    }
}
