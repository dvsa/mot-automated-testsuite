package uk.gov.dvsa.mot.data.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import uk.gov.dvsa.mot.data.DataDao;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles MariaDB database queries to load test data.
 */
public class DataDaoImpl implements DataDao {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataDaoImpl.class);

    /** The JDBC template to use. */
    private final JdbcTemplate jdbcTemplate;

    // TODO: extract these out into config
    private static final String MOT_TESTER = //"select p.username as username, o.name as ae_name, s.name as site_name" +
            "select p.username as username" +
                    " from person p, auth_for_testing_mot aftm, organisation o," +
                    "  organisation_site_map osm, site s, auth_for_testing_mot_at_site afts," +
                    "  site_business_role_map sbrm, auth_for_ae afa, security_card sc," +
                    "  person_security_card_map pscm" +
                    " where aftm.person_id = p.id" +
                    " and aftm.vehicle_class_id = 4" + // only cars
                    " and aftm.status_id = 9" + // only qualified testing authorisations
                    " and p.is_account_claim_required = 0" +
                    " and p.is_password_change_required = 0" +
                    " and o.id = osm.organisation_id" +
                    " and s.id = osm.site_id" +
                    " and s.id = afts.site_id" +
                    " and afts.vehicle_class_id = 4" + // only cars
                    " and afts.status_id = 2" + // only valid site authorisations
                    " and sbrm.site_id = s.id" +
                    " and sbrm.person_id = p.id" +
                    " and sbrm.site_business_role_id = 1" + // only testers
                    " and afa.organisation_id = o.id" +
                    " and afa.status_id = 2" + // only valid ae authorisations
                    " and o.slots_balance > 0" + // ae's with slots available
                    " and p.id = pscm.person_id" +
                    " and sc.id = pscm.security_card_id" +
                    " and sc.security_card_status_lookup_id = 1" + // only assigned cards
                    " and p.username not in (" +
                    " select username from special_notice" +
                    " where is_acknowledged != 1" +
                    " and is_deleted != 1" +
                    ")";

    private static final String VEHICLE_CAR =
            "select veh.registration, veh.vin, mtc.odometer_value " +
            " from vehicle veh, model_detail md, model, make, mot_test_current mtc " +
            " where veh.model_detail_id = md.id" +
            " and md.model_id = model.id" +
            " and model.name is not null" +
            " and model.make_id = make.id" +
            " and model.name is not null" +
            " and md.vehicle_class_id = 4" + // cars only
            " and mtc.vehicle_id = veh.id" +  // with current MOT
            " and mtc.odometer_result_type = 'OK'"; // with successful previous odometer reading

    /**
     * Creates a new instance.
     * @param jdbcTemplate      The JDBC template to use
     */
    @Inject
    public DataDaoImpl(JdbcTemplate jdbcTemplate) {
        logger.debug("Creating DataDaoImpl...");
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Loads an entry from the specified test data set.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    @Override
    public List<String> loadData(String dataSetName) {
        logger.debug("loadData - data set is {}", dataSetName);
        String query = null;
        switch (dataSetName) {
            case "MOT_TESTER_A":
                query = MOT_TESTER + " and p.username like 'A%'"; // short term work-around
                break;

            case "MOT_TESTER_C":
                query = MOT_TESTER + " and p.username like 'C%'"; // short term work-around
                break;

            case "VEHICLE_CAR_H":
                query = VEHICLE_CAR + " and veh.registration like 'H%'"; // short term work-around
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
}
