package uk.gov.dvsa.mot.fixtures.generic;

import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.data.DatabaseDataProvider;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;

import java.util.List;
import javax.inject.Inject;

/**
 * Step definitions for test data loading steps.
 */
public class DataStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataStepDefinitions.class);

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param dataProvider      The data provider to use
     */
    @Inject
    public DataStepDefinitions(WebDriverWrapper driverWrapper, DatabaseDataProvider dataProvider) {
        logger.debug("Creating DataStepDefinitions...");

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}$",
                (String dataSetName, String key1) -> {
                    loadData(driverWrapper, dataProvider, dataSetName, new String[] {key1});
            });

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2) -> {
                    loadData(driverWrapper, dataProvider, dataSetName, new String[] {key1, key2});
            });

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3) -> {
                    loadData(driverWrapper, dataProvider, dataSetName, new String[] {key1, key2, key3});
            });

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4) -> {
                    loadData(driverWrapper, dataProvider, dataSetName, new String[] {key1, key2, key3, key4});
            });
    }

    /**
     * Loads a data set, and populates the scenario test data, for the specified keys.
     * @param driverWrapper     The driver wrapper to use
     * @param dataProvider      The data provider to use
     * @param dataSetName       The name of the data set
     * @param keys              The keys to populate
     */
    private void loadData(WebDriverWrapper driverWrapper, DatabaseDataProvider dataProvider, String dataSetName,
                          String[] keys) {
        List<String> dataSet = dataProvider.getDatasetEntry(dataSetName);

        // check the number of items in the data set matches the number of keys in the test step
        assertEquals("Expected data set " + dataSetName + " to contain " + keys.length + " data items, "
                        + "but it contained " + dataSet.size() + " data items. Please check your scenario",
                keys.length, dataSet.size());

        for (int i = 0; i < keys.length; i++) {
            driverWrapper.setData(keys[i], dataSet.get(i));
        }
    }
}
