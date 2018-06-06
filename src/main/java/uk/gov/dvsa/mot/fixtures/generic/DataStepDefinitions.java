package uk.gov.dvsa.mot.fixtures.generic;

import static org.junit.Assert.assertEquals;

import cucumber.api.java8.En;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.data.DataProvider;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.framework.excel.ExcelDocument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.inject.Inject;

/**
 * Step definitions for test data loading steps.
 */
public class DataStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataStepDefinitions.class);

    /** The driver wrapper to use. */
    private final WebDriverWrapper driverWrapper;

    /** The data provider to use. */
    private final DataProvider dataProvider;

    /** Indicates whether parallel mode and data filtering is enabled. */
    private final boolean isParallelMode;

    /**
     * Creates a new instance.
     * @param driverWrapper     The driver wrapper to use
     * @param dataProvider      The data provider to use
     */
    @Inject
    public DataStepDefinitions(WebDriverWrapper driverWrapper, DataProvider dataProvider, Environment env) {
        logger.debug("Creating DataStepDefinitions...");
        this.driverWrapper = driverWrapper;
        this.dataProvider = dataProvider;
        this.isParallelMode = Boolean.parseBoolean(env.getProperty("dataFiltering", "false"));

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}$",
                (String dataSetName, String key1) ->
                    loadFromCachedData(dataSetName, new String[] {key1}));

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2) ->
                    loadFromCachedData(dataSetName, new String[] {key1, key2}));

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3) ->
                    loadFromCachedData(dataSetName, new String[] {key1, key2, key3}));

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4) ->
                    loadFromCachedData(dataSetName, new String[] {key1, key2, key3, key4}));

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4, String key5) ->
                    loadFromCachedData(dataSetName, new String[] {key1, key2, key3, key4, key5}));

        When("^I load \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4, String key5, String key6) ->
                    loadFromCachedData(dataSetName, new String[] {key1, key2, key3, key4, key5, key6}));

        When("^I load immediately \"([^\"]+)\" as \\{([^\\}]+)\\}$",
                (String dataSetName, String key1) ->
                        loadFromUncachedData(dataSetName, new String[] {key1}));

        When("^I load immediately \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2) ->
                        loadFromUncachedData(dataSetName, new String[] {key1, key2}));

        When("^I load immediately \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3) ->
                        loadFromUncachedData(dataSetName, new String[] {key1, key2, key3}));

        When("^I load immediately \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4) ->
                        loadFromUncachedData(dataSetName, new String[] {key1, key2, key3, key4}));

        When("^I load immediately \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4, String key5) ->
                        loadFromUncachedData(dataSetName, new String[] {key1, key2, key3, key4, key5}));

        When("^I load immediately \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4, String key5, String key6) ->
                        loadFromUncachedData(dataSetName, new String[] {key1, key2, key3, key4, key5, key6}));

        When("^I load uniquely \"([^\"]+)\" as \\{([^\\}]+)\\}$",
                (String dataSetName, String key1) ->
                        loadUniqueData(dataSetName, new String[] {key1}));

        When("^I load uniquely \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2) ->
                        loadUniqueData(dataSetName, new String[] {key1, key2}));

        When("^I load uniquely \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3) ->
                        loadUniqueData(dataSetName, new String[] {key1, key2, key3}));

        When("^I load uniquely \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4) ->
                        loadUniqueData(dataSetName, new String[] {key1, key2, key3, key4}));

        When("^I load uniquely \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4, String key5) ->
                        loadUniqueData(dataSetName, new String[] {key1, key2, key3, key4, key5}));

        When("^I load uniquely \"([^\"]+)\" as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, "
                        + "\\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dataSetName, String key1, String key2, String key3, String key4, String key5, String key6) ->
                        loadUniqueData(dataSetName, new String[] {key1, key2, key3, key4, key5, key6}));

        When("^I set today as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (String dayKeyName, String monthKeyName, String yearKeyName) ->
                        setToday(dayKeyName, monthKeyName, yearKeyName));

        When("^I set today formatted using \"([^\"]+)\" as \\{([^\\}]+)\\}$",
                (String dateTimeFormat, String dateTimeKeyName) ->
                        setTodayWithFormat(dateTimeFormat, dateTimeKeyName));

        When("^I get the date (\\d+) days ago as \\{([^\\}]+)\\}, \\{([^\\}]+)\\}, \\{([^\\}]+)\\}$",
                (Integer days, String dayKeyName, String monthKeyName, String yearKeyName) ->
                        setDate(days, dayKeyName, monthKeyName, yearKeyName));
    }

    /**
     * Loads from a cached data set, and populates the scenario test data, for the specified keys.
     * @param dataSetName       The name of the data set
     * @param keys              The keys to populate
     */
    private void loadFromCachedData(String dataSetName, String[] keys) {
        List<String> dataSet = dataProvider.getCachedDatasetEntry(dataSetName);

        // check the number of items in the data set matches the number of keys in the test step
        assertEquals("Expected data set " + dataSetName + " to contain " + keys.length + " data items, "
                        + "but it contained " + dataSet.size() + " data items. Please check your scenario",
                keys.length, dataSet.size());

        for (int i = 0; i < keys.length; i++) {
            driverWrapper.setData(keys[i], dataSet.get(i));
        }
    }

    /**
     * Loads from a uncached data set (i.e. executes the database query immediately), and populates the scenario
     * test data, for the specified keys.
     * @param dataSetName       The name of the data set
     * @param keys              The keys to populate
     */
    private void loadFromUncachedData(String dataSetName, String[] keys) {
        List<String> dataSet = dataProvider.getUncachedDatasetEntry(dataSetName);

        // check the number of items in the data set matches the number of keys in the test step
        assertEquals("Expected data set " + dataSetName + " to contain " + keys.length + " data items, "
                        + "but it contained " + dataSet.size() + " data items. Please check your scenario",
                keys.length, dataSet.size());

        for (int i = 0; i < keys.length; i++) {
            driverWrapper.setData(keys[i], dataSet.get(i));
        }
    }

    /**
     * Loads unique data (either using caching and data filtering if parallel enabled, otherwise load immediately).
     * @param dataSetName       The name of the data set
     * @param keys              The keys to populate
     */
    private void loadUniqueData(String dataSetName, String[] keys) {
        if (isParallelMode) {
            /*
             * If running in parallel mode then each test gets unique data thanks to the data filtering.
             */
            loadFromCachedData(dataSetName, keys);
        } else {
            /*
             * If running in serial mode then loading uncached data immediately will help work around data changes
             * due to preceding tests, although in other situations it results in the same data being loaded every time
             */
            loadFromUncachedData(dataSetName, keys);
        }
    }

    /**
     * Sets the specified keys with the current date.
     * @param dayKeyName        The key to set with the current day of the month (1..31)
     * @param monthKeyName      The key to set with the current month of the year (1..12)
     * @param yearKeyName       The key to set with the currwent year (4 digits)
     */
    private void setToday(String dayKeyName, String monthKeyName, String yearKeyName) {
        setDate(0, dayKeyName, monthKeyName, yearKeyName);
    }

    /**
     * Sets the specified keys with the date X days ago.
     * i.e. 7 days would be last week
     * @param days              The number of days ago to get the date for
     * @param dayKeyName        The key to set with the current day of the month (1..31)
     * @param monthKeyName      The key to set with the current month of the year (1..12)
     * @param yearKeyName       The key to set with the currwent year (4 digits)
     */
    private void setDate(Integer days, String dayKeyName, String monthKeyName, String yearKeyName) {
        LocalDate theDate = LocalDate.now();
        theDate = theDate.minusDays(days.longValue());
        driverWrapper.setData(dayKeyName, String.valueOf(theDate.getDayOfMonth()));
        driverWrapper.setData(monthKeyName, String.valueOf(theDate.getMonthValue()));
        driverWrapper.setData(yearKeyName, String.valueOf(theDate.getYear()));
    }

    /**
     * Sets the specified key with the current date/time, formatted using a format pattern.
     * @param dateTimeFormat    The pattern to use
     * @param dateTimeKeyName   The key to set
     */
    private void setTodayWithFormat(String dateTimeFormat, String dateTimeKeyName) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
            driverWrapper.setData(dateTimeKeyName, formatter.format(LocalDate.now()));

        } catch (IllegalArgumentException ex) {
            String message = "Invalid DateTimeFormatter pattern: " + dateTimeFormat;
            logger.error(message, ex);
            throw new IllegalArgumentException(message, ex);
        }
    }
}
