package uk.gov.dvsa.mot.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Guice Module that defines all objects shared between Step Definitions/Hooks.
 */
public class GuiceModule extends AbstractModule {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(GuiceModule.class);

    /**
     * Configures the Guice module.
     */
    public void configure() {
        bind(WebDriverWrapper.class);
        bind(Configuration.class).toProvider(ConfigurationProvider.class);
    }
/*
    @Provides
    public Configuration provideConfiguration() {
        try {
            Configurations configs = new Configurations();
            return configs.properties(new File("configuration/testsuite.properties"));

        } catch (ConfigurationException ex) {
            String message = "Error loading testsuite.properties: " + ex.getMessage();
            logger.error(message, ex);
            return null;
        }
    }
*/

    public static class ConfigurationProvider implements Provider<Configuration> {
        public Configuration get() {
            try {
                Configurations configs = new Configurations();
                return configs.properties(new File("configuration/testsuite.properties"));

            } catch (ConfigurationException ex) {
                String message = "Error loading testsuite.properties: " + ex.getMessage();
                logger.error(message, ex);
                throw new RuntimeException(message, ex);
                //return null;
            }
        }
    }
}