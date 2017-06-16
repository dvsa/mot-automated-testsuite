package uk.gov.dvsa.mot.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles MariaDB database queries to load test data.
 */
public class DataDao {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataDao.class);

    /** The JDBC template to use. */
    private final JdbcTemplate jdbcTemplate;

    /**
     * Creates a new instance.
     * @param jdbcTemplate      The JDBC template to use
     */
    @Inject
    public DataDao(JdbcTemplate jdbcTemplate) {
        logger.debug("Creating DataDao...");
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Loads an entry from the specified test data set.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    public List<String> loadData(String dataSetName) {
        logger.debug("loadData - data set is {}", dataSetName);
        String query = null;
        switch (dataSetName) {
            case "MOT_TESTER_A":
                query = loadQuery("MOT_TESTER") + " and p.username like 'A%'"; // short term work-around
                break;

            case "MOT_TESTER_C":
                query = loadQuery("MOT_TESTER") + " and p.username like 'C%'"; // short term work-around
                break;

            case "VEHICLE_CAR_H":
                query = loadQuery("VEHICLE_CAR") + " and veh.registration like 'H%'"; // short term work-around
                break;

            default:
                String message = "Unknown data set name: '" + dataSetName + "'";
                logger.error(message);
                throw new IllegalArgumentException(message);
        }

        List<List<String>> dataSet = jdbcTemplate.query(query, new RowMapper<List<String>> () {
            public List<String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                List<String> row = new ArrayList<>();
                ResultSetMetaData metaData = rs.getMetaData();

                // JDBC column indices start at 1, not 0...
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                return row;
            }
        });

        if (dataSet.size() == 0) {
            String message = "No test data found matching data set name: '" + dataSetName + "'";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        logger.debug("found entry {}", dataSet.get(0));
        return dataSet.get(0);
    }

    /**
     * Load a SQL query from the queries directory on the classpath.
     * @param dataSetName   The data set name (also query file name)
     * @return The query
     */
    private String loadQuery(String dataSetName) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        this.getClass().getResourceAsStream("/queries/" + dataSetName + ".sql")))) {

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            return builder.toString();

        } catch (IOException ex) {
            String message = "Error reading SQL query for dataset " + dataSetName;
            logger.error(message, ex);
            throw new IllegalArgumentException(message, ex);
        }
    }
}
