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

    /** The cache of dataset results (if any). */
    private List<List<String>> cache;

    /**
     * Creates a new instance.
     * @param name  The dataset name
     * @param sql   The SQL query
     */
    public Dataset(String name, String sql) {
        this.name = name;
        this.sql = sql;
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
     * Populates the cache with the query results.
     * @param results   The results
     */
    public void populateCache(List<List<String>> results) {
        logger.debug("Caching {} results for dataset {}", results.size(), name);
        this.cache = results;
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
        if (cache.size() > 0) {
            List<String> result = cache.get(0);
            cache.remove(result);
            logger.debug("{} using result: {}", name, result);
            return result;

        } else {
            String message = "No more data available for dataset: " + name;
            logger.error(message);
            throw new IllegalStateException(message);
        }
    }
}
