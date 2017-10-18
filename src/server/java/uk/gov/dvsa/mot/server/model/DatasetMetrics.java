package uk.gov.dvsa.mot.server.model;

import java.util.Optional;

/**
 * Mutable JavaBean encapsulating various metrics about datasets, used in the usage report.
 */
public class DatasetMetrics {

    /** The dataset name. */
    private final String name;

    /** The number of entries (rows) loaded from the database, cached and available for use by the test suite. */
    private Optional<Integer> cacheSize = Optional.empty();

    /** The number of entries (rows) requested from the cache by the test suite. */
    private Optional<Integer> cacheRequested = Optional.empty();

    /** The number of entries (rows) loaded immediately from the database, available for use by the test suite. */
    private Optional<Integer> loadedImmediatelySize = Optional.empty();

    /** The number of entries (rows) requested from queries loaded immediately from the database. */
    private Optional<Integer> loadedImmediatelyRequested = Optional.empty();

    /** The amount of time (in milliseconds) the database query took to execute. */
    private Optional<Long> timingMilliseconds = Optional.empty();

    /**
     * Creates a new instance.
     * @param name  The dataset name
     */
    public DatasetMetrics(String name) {
        this.name = name;
    }

    /**
     * Set the number of entries (rows) loaded from the database, cached and available for use by the test suite.
     * @param cacheSize     The number of entries
     */
    public void setCacheSize(int cacheSize) {
        this.cacheSize = Optional.of(cacheSize);
    }

    /**
     * Get the number of entries (rows) loaded from the database, cached and available for use by the test suite.
     * @return The number of entries
     */
    public Optional<Integer> getCacheSize() {
        return cacheSize;
    }

    /**
     * Increase the number of entries (rows) requested from the cache by the test suite.
     */
    public void increaseCacheRequested() {
        cacheRequested = Optional.of(cacheRequested.map(value -> value + 1).orElse(1));
    }

    /**
     * Get the number of entries (rows) requested from the cache by the test suite.
     * @return The number of entries
     */
    public Optional<Integer> getCacheRequested() {
        return cacheRequested;
    }

    /**
     * Set the number of entries (rows) loaded immediately from the database, not using caching.
     * @param loadedImmediatelySize  The number of entries
     */
    public void setLoadedImmediatelySize(int loadedImmediatelySize) {
        this.loadedImmediatelySize = Optional.of(loadedImmediatelySize);
    }

    /**
     * Get the number of entries (rows) loaded immediately from the database, not using caching.
     * <p><i>(Note: if the dataset is loaded immediately multiple times, this value will be from the last time it
     * was loaded)</i></p>
     * @return The number of entries
     */
    public Optional<Integer> getLoadedImmediatelySize() {
        return loadedImmediatelySize;
    }

    /**
     * Increase the number of entries (rows) requested from queries loaded immediately from the database.
     */
    public void increaseLoadedImmediatelyRequested() {
        loadedImmediatelyRequested = Optional.of(loadedImmediatelyRequested.map(value -> value + 1).orElse(1));
    }

    /**
     * Get the number of entries (rows) requested from queries loaded immediately from the database.
     * @return The number of entries
     */
    public Optional<Integer> getLoadedImmediatelyRequested() {
        return loadedImmediatelyRequested;
    }

    /**
     * Set the amount of time (in milliseconds) the database query took to execute.
     * @param timingMilliseconds    The time
     */
    public void setTimingMilliseconds(long timingMilliseconds) {
        this.timingMilliseconds = Optional.of(timingMilliseconds);
    }

    /**
     * Get the amount of time (in milliseconds) the database query took to execute.
     * <p><i>(Note: if the dataset is loaded immediately multiple times, this value will be from the last time it
     * was loaded)</i></p>
     * @return The time (in milliseconds)
     */
    public Optional<Long> getTimingMilliseconds() {
        return timingMilliseconds;
    }
}
