package uk.gov.dvsa.mot.server.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dvsa.mot.server.model.DatasetMetrics;
import uk.gov.dvsa.mot.server.reporting.DataUsageReportGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PreDestroy;

/**
 * Handles provision of test data, using existing data from a test database.
 */
public class DatabaseDataProvider {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataProvider.class);

    /** The DataDao to use. */
    private final DataDao dataDao;

    /** The data usage report generator to use. */
    private final DataUsageReportGenerator dataUsageReportGenerator;

    /** The cached datasets to use, keyed by name. */
    private final Map<String, List<List<String>>> datasets;

    /** The metrics recorded for each dataset. */
    private final Map<String, DatasetMetrics> datasetMetrics;

    /**
     * Creates a new instance.
     * @param dataDao                       The user DAO to use
     * @param dataUsageReportGenerator      The data usage report generator to use
     */
    public DatabaseDataProvider(DataDao dataDao, DataUsageReportGenerator dataUsageReportGenerator) {
        logger.debug("Creating DatabaseDataProvider...");
        this.dataDao = dataDao;
        this.dataUsageReportGenerator = dataUsageReportGenerator;
        this.datasets = new HashMap<>();
        this.datasetMetrics = new HashMap<>();
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
            final long start = System.currentTimeMillis();
            dataset = dataDao.loadDataset(dataSetName);
            final long stop = System.currentTimeMillis();
            datasets.put(dataSetName, dataset);

            // record size of the cached dataset, and query timing
            getDatasetMetrics(dataSetName).setEntriesCached(dataset.size());
            getDatasetMetrics(dataSetName).setTimingMilliseconds(stop - start);

        } else {
            dataset = datasets.get(dataSetName);
        }

        // record request for data from the cached dataset
        getDatasetMetrics(dataSetName).increaseEntriesRequested();

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
        long start = System.currentTimeMillis();
        List<List<String>> dataset = dataDao.loadDataset(dataSetName);
        long stop = System.currentTimeMillis();

        // record dataset size, and query timing
        getDatasetMetrics(dataSetName).setEntriesLoadedImmediately(dataset.size());
        getDatasetMetrics(dataSetName).setTimingMilliseconds(stop - start);

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
        long start = System.currentTimeMillis();
        List<List<String>> dataset = dataDao.loadDataset(dataSetName, length);
        long stop = System.currentTimeMillis();

        // record dataset size, and query timing
        getDatasetMetrics(dataSetName).setEntriesLoadedImmediately(dataset.size());
        getDatasetMetrics(dataSetName).setTimingMilliseconds(stop - start);

        if (dataset.size() < 1) {
            String message = "No data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        return dataset;
    }

    /**
     * Outputs a data usage report, called by Spring just before the data server is shutdown.
     */
    @PreDestroy
    public void outputUsageReport() {
        logger.info("In outputUsageReport...");
        dataUsageReportGenerator.generateReport(datasetMetrics);
    }

    /**
     * Get the metrics for the specified dataset, handling creation and caching on demand when needed.
     * @param datasetName   The dataset name
     * @return The metrics
     */
    private DatasetMetrics getDatasetMetrics(String datasetName) {
        if (datasetMetrics.containsKey(datasetName)) {
            return datasetMetrics.get(datasetName);

        } else {
            DatasetMetrics metrics = new DatasetMetrics(datasetName);
            datasetMetrics.put(datasetName, metrics);
            return metrics;
        }
    }
}
