package uk.gov.dvsa.mot.server.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Handles MariaDB database queries to load test data.
 */
public class DataDao {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataDao.class);

    /** The JDBC template to use. */
    private final JdbcTemplate jdbcTemplate;

    /** Used to scan for query files on the classpath. */
    private final ResourcePatternResolver classpathScanner;

    /**
     * Creates a new instance.
     * @param jdbcTemplate      The JDBC template to use
     * @param classpathScanner  The classpath scanner to use
     */
    @Inject
    public DataDao(JdbcTemplate jdbcTemplate, ResourcePatternResolver classpathScanner) {
        logger.debug("Creating DataDao...");
        this.jdbcTemplate = jdbcTemplate;
        this.classpathScanner = classpathScanner;
    }

    /**
     * Loads all entries in the specified dataset.
     * @param datasetName   The name of the dataset
     * @return The dataset
     */
    public List<List<String>> loadDataset(String datasetName) {
        logger.debug("loading all entries in dataset {}", datasetName);
        return loadFromDataset(datasetName, 0);
    }

    /**
     * Loads the specified number of entries for the dataset.
     * @param datasetName   The name of the dataset
     * @param length        The number of entries to load
     * @return The dataset
     */
    public List<List<String>> loadDataset(String datasetName, int length) {
        logger.debug("loading {} entries in dataset {}", length, datasetName);
        return loadFromDataset(datasetName, length);
    }

    /**
     * Loads the specified number of entries for the dataset.
     * @param datasetName   The name of the dataset
     * @param length        The number of entries to load
     * @return The dataset
     */
    private List<List<String>> loadFromDataset(String datasetName, int length) {
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
            return executeQuery(query, datasetName, length);
        }
    }

    /**
     * Executes a data set query.
     * @param query         The SQL query
     * @param dataSetName   The name of the data set
     * @param length        The number of entries to load
     * @return The dataset
     */
    private List<List<String>> executeQuery(String query, String dataSetName, int length) {
        if (length > 0) {
            jdbcTemplate.setMaxRows(length);
        } else {
            jdbcTemplate.setMaxRows(-1);
        }

        List<List<String>> dataSet = jdbcTemplate.query(query, (ResultSet rs, int rowNum) -> {
            List<String> row = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();

            // JDBC column indices start at 1, not 0...
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            return row;
        });

        // check dataset is not empty
        if (dataSet.size() == 0) {
            String message = "No test data found matching data set name: '" + dataSetName + "'";
            logger.warn(message);
        }

        return dataSet;
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
                builder.append(line + "\n");
            }
            return builder.toString();

        } catch (IOException ex) {
            String message = "Error reading query in " + resource.getFilename();
            logger.error(message, ex);
            throw new IllegalArgumentException(message, ex);
        }
    }
}
