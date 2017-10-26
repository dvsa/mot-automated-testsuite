package uk.gov.dvsa.mot.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Encapsulates a dataset, that can be cached, filtered and/or loaded immediately.
 */
public class Dataset {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(Dataset.class);

    /** The dataset name. */
    private final String name;

    /** The dataset SQL query. */
    private final String sql;

    /** The recorded metrics. */
    private final DatasetMetrics metrics;

    /** The filter to use (if any). */
    private final Filter filter;

    /** The cache of dataset results (if any). */
    private List<List<String>> cache;

    /**
     * Creates a new instance, without a filter.
     * @param name  The dataset name
     * @param sql   The SQL query
     */
    public Dataset(String name, String sql) {
        this.name = name;
        this.sql = sql;
        this.metrics = new DatasetMetrics(name);
        this.filter = null;
    }

    /**
     * Creates a new instance, with a filter.
     * @param name      The dataset name
     * @param sql       The SQL query
     * @param filter    The filter to use
     */
    public Dataset(String name, String sql, Filter filter) {
        this.name = name;
        this.sql = sql;
        this.metrics = new DatasetMetrics(name, filter.getName());
        this.filter = filter;
    }

    /**
     * Get the dataset name.
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the SQL Query.
     * @return The Sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * Get the dataset metrics.
     * @return The Metrics
     */
    public DatasetMetrics getMetrics() {
        return metrics;
    }

    /**
     * Get the filter (if any).
     * @return The filter (can be <code>null</code>)
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Populates the cache with the query results.
     * @param results   The results
     * @param timing    The time (in milliseconds) taken to load the results
     */
    public void populateCache(List<List<String>> results, long timing) {
        logger.debug("Caching {} results for dataset {}", results.size(), name);
        this.cache = results;
        metrics.setCacheSize(results.size());
        metrics.setTimingMilliseconds(timing);
    }

    /**
     * Determines whether the cache has been populated.
     * @return <code>true</code> if populated
     */
    public boolean isCachePopulated() {
        return cache != null;
    }

    /**
     * Get a result from the cache.
     * @return The result
     * @throws IllegalStateException If no more data available in the cache
     */
    public List<String> getCachedResult() throws IllegalStateException {
        metrics.increaseCacheRequested();

        if (cache.size() < 1) {
            String message = "No more data available for dataset: " + name;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        if (filter == null) {
            List<String> result = cache.get(0);
            cache.remove(result);
            logger.debug("{} using result (no filtering): {}", name, result);
            return result;

        } else {
            List<String> result = cache.get(0);
            while (!filter.isDataUnique(result.get(0))) {
                logger.debug("Filtering out {}", result);
                cache.remove(result);
                metrics.increaseFilteredOut();

                if (cache.size() < 1) {
                    String message = "No more data available for dataset: " + name;
                    logger.error(message);
                    throw new IllegalStateException(message);

                } else {
                    result = cache.get(0);
                }
            }

            filter.addToFilter(result.get(0));
            cache.remove(result);

            logger.debug("{} using result (with filtering): {}", name, result);
            return result;
        }
    }
}
