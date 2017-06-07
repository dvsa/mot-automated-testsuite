package uk.gov.dvsa.mot.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

/**
 * Spring configuration class for the test suite.
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
