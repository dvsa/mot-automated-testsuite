package uk.gov.dvsa.mot.data.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.gov.dvsa.mot.data.DataDao;

import javax.inject.Inject;
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
     * Loads a user of the specified user type.
     * @param userType      The user type
     * @return The username
     */
    @Override
    public String loadUser(String userType) {
        logger.debug("loadUser - userType is {}", userType);
        String query = null;
        switch (userType) {
            case "MOT_TESTER_A":
                query = MOT_TESTER + " and p.username like 'A%'"; // short term work-around
                break;

            case "MOT_TESTER_C":
                query = MOT_TESTER + " and p.username like 'C%'"; // short term work-around
                break;

            default:
                String message = "Unknown data set name: '" + userType + "'";
                logger.error(message);
                throw new IllegalArgumentException(message);
        }
        // TODO: only query for first item, not a list
        List<String> username = jdbcTemplate.queryForList(query, String.class);

        if (username.size() == 0) {
            String message = "No test data found matching data set name: '" + userType + "'";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        logger.debug("found username {}", username.get(0));
        return username.get(0);
    }
}
