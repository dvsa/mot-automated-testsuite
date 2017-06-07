package uk.gov.dvsa.mot.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

/**
 * Spring configuration class for the test suite.
 *
 * Note: The combination of the CourgetteRunner being set to a <code>runLevel</code> of <code>FEATURE</code>
 * with use of the Cucumber-JVM Spring module means that a new Spring application is created for every Cucumber feature,
 * shared between every scenario within each feature.
 *
 * Note 2: New instances of hooks and step definitions are created by Spring for every scenario within each feature,
 * with Spring dependencies injected from the current Spring application.
 */
@Configuration
@PropertySource("file:configuration/testsuite.properties")
public class SpringConfiguration {

    @Autowired
    Environment env;

    @Bean
    public WebDriverWrapper webDriverWrapper() {
        return new WebDriverWrapper(env);
    }
}
