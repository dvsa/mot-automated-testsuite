package uk.gov.dvsa.mot.di;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.dvsa.mot.data.DataDao;
import uk.gov.dvsa.mot.data.DataProvider;
import uk.gov.dvsa.mot.data.DatabaseDataProvider;
import uk.gov.dvsa.mot.data.impl.DataDaoImpl;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.sql.DataSource;
import java.lang.management.ManagementFactory;

/**
 * Spring configuration class for the test suite.
 *
 * Note: The combination of the CourgetteRunner being set to a <code>runLevel</code> of <code>FEATURE</code>
 * with use of the Cucumber-JVM Spring module means that a separate process, each with a separate Spring application,
 * is created for every Cucumber feature, shared between every scenario within that feature.
 *
 * Note 2: New instances of hooks and step definitions are created by Spring for every scenario within each feature,
 * with Spring dependencies injected from the current Spring application.
 */
@Configuration
@EnableTransactionManagement
@PropertySource("file:configuration/testsuite.properties")
public class SpringConfiguration {

    static {
        // using a static initialisation block so this is instantiated as early as possible

        // often of the form: <pid>@<hostname>.<domain> (but not guaranteed to be!)
        String jmxName = ManagementFactory.getRuntimeMXBean().getName();

        // set the "pid" MDC variable, used by logback
        MDC.put("pid", jmxName);
    }

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        // use connection pool so that the connection gets re-used between scenarios in a feature
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(env.getRequiredProperty("jdbc.url") +
            // useful mysql JDBC driver properties for debugging and logging
            // if switch to mariadb JDBC driver then change these
            "?logSlowQueries=true&slowQueryThresholdMillis=500&dumpQueriesOnException=true&gatherPerfMetrics=true" +
            "&useUsageAdvisor=true&explainSlowQueries=true&reportMetricsIntervalMillis=60000&logger=Slf4JLogger");
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
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

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }

    @Bean
    public DataProvider dataProvider(DataDao dataDao) {
        return new DatabaseDataProvider(dataDao);
    }

    @Bean
    public DataDao userDao(JdbcTemplate jdbcTemplate) {
        return new DataDaoImpl(jdbcTemplate);
    }

    @Bean
    public WebDriverWrapper webDriverWrapper() {
        return new WebDriverWrapper(env);
    }
}
