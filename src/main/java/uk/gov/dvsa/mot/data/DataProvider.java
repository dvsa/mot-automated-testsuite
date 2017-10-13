package uk.gov.dvsa.mot.data;

import java.util.List;

/**
 * Handles provision of test data.
 */
public interface DataProvider {

    /**
     * Get a cached entry from the specified test data set (loading and caching if needed), and removes it from the
     * in-memory cache.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    List<String> getCachedDatasetEntry(String dataSetName);

    /**
     * Get the first entry from the specified test data set, by executing the query immediately, ignoring any caches.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    List<String> getUncachedDatasetEntry(String dataSetName);

    /**
     * Get the specified number of entries from the specified test data set, by executing the query immediately,
     * ignoring any caches.
     * @param dataSetName   The data set name
     * @param length        The number of data set entries
     * @return The data entries
     */
    List<List<String>> getUncachedDataset(String dataSetName, int length);
}
