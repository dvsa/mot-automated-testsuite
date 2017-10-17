package uk.gov.dvsa.mot.server.model;

import java.util.Optional;

/**
 * Mutable JavaBean encapsulating various metrics about datasets, used in the usage report.
 */
public class DatasetMetrics {

    /** The dataset name. */
    private final String name;

    /** The number of entries (rows) loaded from the database, cached and available for use by the test suite. */
    private Optional<Integer> entriesCached = Optional.empty();

    /** The number of entries (rows) requested from the cache by the test suite. */
    private Optional<Integer> entriesRequested = Optional.empty();

    /** The number of entries (rows) loaded immediately from the database, not using caching. */
    private Optional<Integer> entriesLoadedImmediately = Optional.empty();

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
     * @param entriesCached     The number of entries
     */
    public void setEntriesCached(int entriesCached) {
        this.entriesCached = Optional.of(entriesCached);
    }

    /**
     * Get the number of entries (rows) loaded from the database, cached and available for use by the test suite.
     * @return The number of entries
     */
    public Optional<Integer> getEntriesCached() {
        return entriesCached;
    }

    /**
     * Increase the number of entries (rows) requested from the cache by the test suite.
     */
    public void increaseEntriesRequested() {
        entriesRequested = Optional.of(entriesRequested.map(value -> value + 1).orElse(1));
    }

    /**
     * Get the number of entries (rows) requested from the cache by the test suite.
     * @return The number of entries
     */
    public Optional<Integer> getEntriesRequested() {
        return entriesRequested;
    }

    /**
     * Set the number of entries (rows) loaded immediately from the database, not using caching.
     * @param entriesLoadedImmediately  The number of entries
     */
    public void setEntriesLoadedImmediately(int entriesLoadedImmediately) {
        this.entriesLoadedImmediately = Optional.of(entriesLoadedImmediately);
    }

    /**
     * Get the number of entries (rows) loaded immediately from the database, not using caching.
     * <p><i>(Note: if the dataset is loaded immediately multiple times, this value will be from the last time it
     * was loaded)</i></p>
     * @return The number of entries
     */
    public Optional<Integer> getEntriesLoadedImmediately() {
        return entriesLoadedImmediately;
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
