package uk.gov.dvsa.mot.server.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import uk.gov.dvsa.mot.server.model.Dataset;
import uk.gov.dvsa.mot.server.model.Filter;
import uk.gov.dvsa.mot.utils.config.TestsuiteConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles loading dataset queries from files in the classpath.
 */
public class QueryFileLoader {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(QueryFileLoader.class);

    /** Used to scan for query files on the classpath. */
    private final ResourcePatternResolver classpathScanner;

    /** The filters to share between datasets. */
    private final Map<String, Filter> filters;

    /** Indicates whether filtering is enabled. */
    private final boolean isFilteringEnabled;

    /**
     * Creates a new instance.
     * @param classpathScanner      The scanner to use
     */
    public QueryFileLoader(ResourcePatternResolver classpathScanner, TestsuiteConfig env) {
        this.classpathScanner = classpathScanner;
        this.filters = new HashMap<>();
        this.isFilteringEnabled = Boolean.parseBoolean(env.getProperty("dataFiltering", "false"));
        logger.info("Filtering enabled: {}", isFilteringEnabled);
    }

    /**
     * Loads the dataset SQL query from a file on the classpath.
     * @param datasetName   The name of the dataset
     * @return The dataset
     * @throws IllegalStateException Error reading the file
     */
    public Dataset loadFromFile(String datasetName) throws IllegalStateException {
        Resource[] resources;
        try {
            // scan the classpath for the query file
            resources = classpathScanner.getResources("classpath*:queries/**/" + datasetName + ".sql");
        } catch (IOException ex) {
            String message = "Error scanning the classpath for query file: " + datasetName;
            logger.error(message, ex);
            throw new IllegalStateException(message, ex);
        }

        if (resources == null || resources.length == 0) {
            String message = "No query file found for dataset: " + datasetName;
            logger.error(message);
            throw new IllegalStateException(message);

        }

        try {
            Resource resource = resources[0];

            // load the SQL query contained in the file
            String query = loadQuery(resource);

            if (!isFilteringEnabled) {
                return new Dataset(datasetName, query);

            } else {
                // find the name of the parent directory, used as the filter name
                String parentDirName = resource.getFile().getParentFile().getName();

                // if a sub-directory
                Filter filter = null;
                if (!"queries".equals(parentDirName)) {
                    // get shared filter
                    filter = filters.get(parentDirName);

                    if (filter == null) {
                        // create new filter
                        filter = new Filter(parentDirName);
                        filters.put(parentDirName, filter);
                    }
                }

                if (filter != null) {
                    return new Dataset(datasetName, query, filter);

                } else {
                    return new Dataset(datasetName, query);
                }
            }

        } catch (IOException ex) {
            String message = "Error querying the parent directory name: " + datasetName;
            logger.error(message, ex);
            throw new IllegalStateException(message, ex);
        }
    }

    /**
     * Load a SQL query from a file on the classpath.
     * @param resource   The query file resource
     * @return The query
     */
    private String loadQuery(Resource resource) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();

        } catch (IOException ex) {
            String message = "Error reading query in " + resource.getFilename();
            logger.error(message, ex);
            throw new IllegalArgumentException(message, ex);
        }
    }
}
