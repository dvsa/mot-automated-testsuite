package uk.gov.dvsa.mot.di;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import uk.gov.dvsa.mot.data.ClientDataProvider;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.utils.config.TestsuiteConfig;

import java.lang.management.ManagementFactory;

/**
 * Spring configuration class for the test suite.
 * <p>Note: Using the Cucumber cli runner with the Cucumber-JVM Spring module results in a single Spring application
 * created for the testsuite, shared between every Cucumber feature being run.</p>
 * <p>Note 2: New instances of hooks and step definitions are created by Spring for every scenario within each feature,
 * with Spring dependencies injected from the current Spring application.</p>
 */
@Configuration
public class SpringConfiguration {

    public static TestsuiteConfig testsuiteConfig;

    static {
        String targetConfig = System.getProperty("target_config");
        String configuration = System.getProperty("configuration");

        if (targetConfig != null) {
            testsuiteConfig = TestsuiteConfig.loadCurrentConfig("testsuite",
                    "browserstack",
                    targetConfig);
        } else if (configuration != null) {
            testsuiteConfig = TestsuiteConfig.loadCurrentConfigFromString(configuration);
        } else {
            testsuiteConfig = TestsuiteConfig.loadCurrentConfig("testsuite");
        }

        // using a static initialisation block so this is instantiated as early as possible

        // often of the form: <pid>@<hostname>.<domain> (but not guaranteed to be!)
        String jmxName = ManagementFactory.getRuntimeMXBean().getName();

        // set the "pid" MDC variable, used by logback
        MDC.put("pid", jmxName);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ClientDataProvider dataProvider() {
        return new ClientDataProvider(testsuiteConfig(), restTemplate());
    }

    @Bean
    public WebDriverWrapper webDriverWrapper() {
        return new WebDriverWrapper(testsuiteConfig());
    }

    @Bean
    public TestsuiteConfig testsuiteConfig() {
        return testsuiteConfig;
    }
}
