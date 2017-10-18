package uk.gov.dvsa.mot.server.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.gov.dvsa.mot.server.model.Dataset;

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
     * Loads the specified number of entries for the dataset.
     * @param dataset   The dataset
     * @param length    The number of entries to load (0 means all)
     * @return The results
     */
    public List<List<String>> loadDataset(Dataset dataset, int length) {
        if (length > 0) {
            jdbcTemplate.setMaxRows(length);
        } else {
            jdbcTemplate.setMaxRows(-1);
        }

        return jdbcTemplate.query(dataset.getSql(), (ResultSet rs, int rowNum) -> {
            List<String> row = new ArrayList<>();
            ResultSetMetaData metaData = rs.getMetaData();

            // JDBC column indices start at 1, not 0...
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                row.add(rs.getString(i));
            }
            return row;
        });
    }
}
