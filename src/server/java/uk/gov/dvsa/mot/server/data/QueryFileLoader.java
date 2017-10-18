package uk.gov.dvsa.mot.server.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import uk.gov.dvsa.mot.server.model.Dataset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Handles loading dataset queries from files in the classpath.
 */
public class QueryFileLoader {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(QueryFileLoader.class);

    /** Used to scan for query files on the classpath. */
    private final ResourcePatternResolver classpathScanner;

    /**
     * Creates a new instance.
     * @param classpathScanner      The scanner to use
     */
    public QueryFileLoader(ResourcePatternResolver classpathScanner) {
        this.classpathScanner = classpathScanner;
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
            resources = classpathScanner.getResources("classpath*:queries/" + datasetName + ".sql");
        } catch (IOException ex) {
            String message = "Error scanning the classpath for query file: " + datasetName;
            logger.error(message, ex);
            throw new IllegalStateException(message, ex);
        }

        if (resources == null || resources.length == 0) {
            String message = "No query file found for dataset: " + datasetName;
            logger.error(message);
            throw new IllegalStateException(message);

        } else {
            Resource resource = resources[0];

            // load the SQL query contained in the file
            String query = loadQuery(resource);

            // execute the SQL query to load the dataset
            return new Dataset(datasetName, query);
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
