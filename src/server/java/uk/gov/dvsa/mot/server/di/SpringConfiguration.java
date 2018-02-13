package uk.gov.dvsa.mot.server.di;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.dvsa.mot.server.data.DataDao;
import uk.gov.dvsa.mot.server.data.DatabaseDataProvider;
import uk.gov.dvsa.mot.server.data.QueryFileLoader;
import uk.gov.dvsa.mot.server.reporting.DataUsageReportGenerator;
import uk.gov.dvsa.mot.server.utils.config.TestsuiteConfig;

import java.lang.management.ManagementFactory;
import javax.sql.DataSource;

/**
 * Spring configuration class for the data server.
 */
@Configuration
@EnableTransactionManagement
public class SpringConfiguration {

    private static Logger logger = LoggerFactory.getLogger(SpringConfiguration.class);

    static {
        // using a static initialisation block so this is instantiated as early as possible

        // often of the form: <pid>@<hostname>.<domain> (but not guaranteed to be!)
        String jmxName = ManagementFactory.getRuntimeMXBean().getName();

        // set the "pid" MDC variable, used by logback
        MDC.put("pid", jmxName);
    }

    /**
     * Bean to provide TestsuiteConfig.
     *
     * @return  testsuiteconfig to use.
     */
    @Bean
    public TestsuiteConfig env() {
        String configuration = System.getProperty("configuration");

        if (configuration != null) {
            return TestsuiteConfig.loadConfigFromString(configuration);
        } else {
            return TestsuiteConfig.loadConfig("testsuite");
        }
    }

    /**
     * Creates the database data source.
     * @return A connection pool based data source
     */
    @Bean
    public DataSource dataSource(TestsuiteConfig env) {
        // use connection pool so that the connection gets re-used between scenarios in a feature
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(env.getRequiredProperty("jdbc.url")
                // useful mysql JDBC driver properties for debugging and logging
                // if switch to mariadb JDBC driver then change these
                + "?logSlowQueries=true&slowQueryThresholdMillis=500&dumpQueriesOnException=true"
                + "&gatherPerfMetrics=true&useUsageAdvisor=true&explainSlowQueries=true"
                + "&reportMetricsIntervalMillis=60000&logger=Slf4JLogger");

        String uname = env.getRequiredProperty("jdbc.username");
        String pwd = env.getRequiredProperty("jdbc.password");

        logger.debug("Username: " + uname);
        logger.debug("Password: " + pwd);

        dataSource.setUsername(uname);
        dataSource.setPassword(pwd);
        dataSource.setDefaultAutoCommit(false);
        dataSource.setDefaultReadOnly(true);
        dataSource.setInitialSize(0);
        dataSource.setMaxActive(1); // up to 1 active connection in the pool, as scenarios are run in serial
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * Creates a new JdbcTemplate to use for handling the connection to the database.
     *
     * @param dataSource to build the template from.
     * @return new instance of JdbcTemplate
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean
    public DatabaseDataProvider dataProvider(DataDao dataDao, QueryFileLoader queryFileLoader,
                                             DataUsageReportGenerator dataUsageReportGenerator) {
        return new DatabaseDataProvider(dataDao, queryFileLoader, dataUsageReportGenerator);
    }

    @Bean
    public ResourcePatternResolver classpathScanner() {
        return new PathMatchingResourcePatternResolver();
    }

    @Bean
    public QueryFileLoader queryFileLoader(ResourcePatternResolver classpathScanner, TestsuiteConfig env) {
        return new QueryFileLoader(classpathScanner, env);
    }

    @Bean
    public DataDao dataDao(JdbcTemplate jdbcTemplate) {
        return new DataDao(jdbcTemplate);
    }

    @Bean
    public DataUsageReportGenerator dataUsageReportGenerator(TestsuiteConfig env) {
        return new DataUsageReportGenerator(env);
    }
}
