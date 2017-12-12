package uk.gov.dvsa.mot.utils.config;

import groovy.lang.MissingPropertyException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * This is a class to store testsuite config. Provides some testsuite specific functionality.
 */
public class TestsuiteConfig extends Properties {
    /**
     * Creates a new empty instance, without default values.
     */
    public TestsuiteConfig() {
        super();
    }

    /**
     * Gets the value from super class. Then returns the value if not null, or throws exception otherwise.
     *
     * @param key to search the super class for.
     * @return value retrieved from super class.
     * @throws PropertyNotFoundException if value is null.
     */
    public String getRequiredProperty(String key) {
        String value = super.getProperty(key);

        if (value == null) {
            throw new PropertyNotFoundException(String.format("Required property, %s, not found.", key));
        }

        return value;
    }

    /**
     * Load and assemble config from files names.
     *
     * @param configFileNames to load the files from.
     * @return loaded and assembled config.
     */
    public static TestsuiteConfig loadConfig(String... configFileNames) {
        if (configFileNames == null) {
            throw new MissingPropertyException(
                    "To run the application locally you must create a testsuite.properties file."
                            + "\nTo use application with BrowserStack you must create a browserstack.properties and "
                            + "PLATFORM_BROWSER_BROWSER-VERSION.properties files (example: os-x_chrome_62.properties)."
                            + "Then you need to run the testsuite with BrowserStack you must pass "
                            + "a target_config property to the gradlew script."
                            + "\nFor example: ./gradlew browserstack-tag '-Ptag=@browserstack' "
                            + "-Ptarget_config=PLATFORM_BROWSER_BROWSER-VERSION'.");
        }

        TestsuiteConfig testsuiteConfig = new TestsuiteConfig();
        FileInputStream configFile = null;

        for (String configFileName : configFileNames) {

            String configPath = "configuration/" + configFileName + ".properties";

            try {
                configFile = new FileInputStream(configPath);

                testsuiteConfig.load(configFile);
            } catch (FileNotFoundException fileNotFound) {
                throw new IllegalArgumentException(
                        String.format("Unable to load a file from specified path, %s. Stacktrace:\n",
                                configPath, fileNotFound.getMessage()));
            } catch (IOException io) {
                throw new IllegalArgumentException(
                        String.format("Unable to read the file, %s. Stacktrace:\n",
                                configPath, io.getMessage()));
            }
        }

        return testsuiteConfig;
    }

    /**
     * Check if used config contains properties for a mobile device.
     *
     * @return whether it is for a mobile device or not.
     */
    public boolean isMobileConfig() {
        if ((getProperty("device") != null
                && getProperty("realMobile") != null)
                || (getProperty("device") != null
                && getProperty("platform") != null
                && getProperty("browserName") != null) ) {
            return true;
        }

        return false;
    }

    /**
     * Method to check if the target_config is defined to run on BrowserStack.
     *
     * @return whether the config is defined to run on BrowserStack or not.
     */
    public boolean isUsingBrowserStack() {
        return System.getProperty("target_config") != null || System.getProperty("configuration") != null;
    }

    /**
     * Loads a config from a string formatted as key=value\n ...
     *
     * @param configuration string containing the configuration to load
     * @return new TestsuiteConfig object.
     */
    public static TestsuiteConfig loadConfigFromString(String configuration) {
        try {
            final TestsuiteConfig config = new TestsuiteConfig();
            config.load(new StringReader(swapColonSignsToEqualSigns(configuration)));
            return config;
        } catch (IOException io) {
            throw new RuntimeException(String.format("Failed to load the TestsuiteConfig from a key=value string. "
                    + "Stacktrace:\n%s", io.getMessage()));
        }
    }

    private static String swapColonSignsToEqualSigns(String configuration) {
        return configuration.substring(1, configuration.length() - 2);
    }
}
