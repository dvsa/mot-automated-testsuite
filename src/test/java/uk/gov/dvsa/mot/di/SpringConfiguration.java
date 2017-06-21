package uk.gov.dvsa.mot.di;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.gov.dvsa.mot.data.DatabaseDataProvider;
import uk.gov.dvsa.mot.data.DataDao;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import javax.sql.DataSource;

/**
 * Spring configuration class for the test suite.
 *
 * Note: Using the Cucumber cli runner with the Cucumber-JVM Spring module results in a single Spring application
 * created for the testsuite, shared between every Cucumber feature being run.
 *
 * Note 2: New instances of hooks and step definitions are created by Spring for every scenario within each feature,
 * with Spring dependencies injected from the current Spring application.
 */
@Configuration
@EnableTransactionManagement
@PropertySource("file:configuration/testsuite.properties")
public class SpringConfiguration {

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
        return jdbcTemplate;
    }

    @Bean
    public DatabaseDataProvider dataProvider(DataDao dataDao) {
        return new DatabaseDataProvider(dataDao);
    }

    @Bean
    public DataDao dataDao(JdbcTemplate jdbcTemplate) {
        return new DataDao(jdbcTemplate);
    }

    @Bean
    public WebDriverWrapper webDriverWrapper() {
        return new WebDriverWrapper(env);
    }
}
