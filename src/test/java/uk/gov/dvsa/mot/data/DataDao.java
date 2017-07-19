package uk.gov.dvsa.mot.data;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * Loads all datasets, by scanning the <i>queries</i> directory on the classpath.
     * @return A map of datasets, keyed by dataset name
     */
    public Map<String, List<List<String>>> loadAllDatasets() {
        logger.debug("loading all datasets");

        Map<String, List<List<String>>> datasets = new HashMap<>();
        Resource[] resources;
        try {
            // scan the classpath for query files
            resources = classpathScanner.getResources("classpath*:queries/**.sql");
        } catch (IOException ex) {
            String message = "Error scanning the classpath for query files";
            logger.error(message, ex);
            throw new IllegalStateException(message, ex);
        }

        for (Resource resource : resources) {
            String filename = resource.getFilename();

            // dataset name is filename minus the ".sql" suffix
            String datasetName = filename.substring(0, filename.length() - 4);

            logger.debug("found query file {} for dataset {}", filename, datasetName);

            // load the SQL query contained in the file
            String query = loadQuery(resource);

            // execute the SQL query to load the dataset
            long start = System.currentTimeMillis();
            List<List<String>> dataset = loadDataset(query);
            long end = System.currentTimeMillis();

            // check dataset is not empty
            if (dataset.size() == 0) {
                String message = "No test data found matching data set name: '" + datasetName + "'";
                logger.error(message);
                throw new IllegalStateException(message);
            }
            double timingInSeconds = (end - start) / 1000.0D;
            String formattedTiming = String.format("%.3f", timingInSeconds);
            logger.debug("loaded {} entries for dataset {} in {} secs", dataset.size(), datasetName, formattedTiming);

            if (timingInSeconds > 10.0D) {
                logger.warn("slow dataset: {} took {} seconds, please check the SQL query performance!",
                        datasetName, formattedTiming);
            }

            // add the dataset to the map
            datasets.put(datasetName, dataset);
        }

        return datasets;
    }

    /**
     * Loads a data set.
     * @param query   The SQL query
     * @return The dataset
     */
    private List<List<String>> loadDataset(String query) {
        List<List<String>> dataSet = jdbcTemplate.query(query, (ResultSet rs, int rowNum) -> {
            List<String> row = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();

            // JDBC column indices start at 1, not 0...
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            return row;
        });
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
