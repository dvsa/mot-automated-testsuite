package uk.gov.dvsa.mot.data;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.csv.CsvDocument;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A CSV file based data provider.
 */
public class CsvDataProvider implements DataProvider {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(ClientDataProvider.class);

    /** The folder where the data files are stored. */
    private final String dataLocation;

    /** Whether we are using data filtering. */
    private final boolean dataFiltering;

    /** Map of all of the loaded data sets. */
    private Map<String, List<List<String>>> dataSets = new HashMap<String, List<List<String>>>();

    /**
     * Public constructor sets data file location.
     * @param dataLocation  the folder containing the data files
     */
    public CsvDataProvider(String dataLocation, boolean dataFiltering) {
        this.dataLocation = dataLocation;
        this.dataFiltering = dataFiltering;
    }

    /**
     * Get a cached entry from the specified test data set (loading and caching if needed), and removes it from the
     * in-memory cache.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    public List<String> getCachedDatasetEntry(String dataSetName) {
        return getUncachedDataset(dataSetName, 1).get(0);
    }

    /**
     * Get the first entry from the specified test data set, by executing the query immediately, ignoring any caches.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    public List<String> getUncachedDatasetEntry(String dataSetName) {
        return getUncachedDataset(dataSetName, 1).get(0);
    }

    /**
     * Get the specified number of entries from the specified test data set, by executing the query immediately,
     * ignoring any caches.
     * @param dataSetName   The data set name
     * @param length        The number of data set entries
     * @return The data entries
     */
    public List<List<String>> getUncachedDataset(String dataSetName, int length) {

        String filename = this.dataLocation + dataSetName + ".csv";

        try {
            List<List<String>> dataSet;

            // Check whether we have already loaded the dataset else load it
            if (dataSets.containsKey(dataSetName)) {
                logger.debug("Retrieving dataset: " + dataSetName);
                dataSet = dataSets.get(dataSetName);
            } else {
                logger.debug("Getting data for file: " + filename);
                File dataFile = new File(filename);
                CsvDocument csvData = new CsvDocument(CSVParser.parse(dataFile, StandardCharsets.UTF_8,
                        CSVFormat.DEFAULT));
                dataSet = csvData.getRows();
                dataSets.put(dataSetName, dataSet);
            }

            // If the amount of data requested is less than the entire dataset then get a subset of the data
            if (dataSet.size() > length) {
                List<List<String>> subSet = new ArrayList<List<String>>();

                Iterator<List<String>> dataSetIt = dataSet.iterator();
                int count = 0;
                while (dataSetIt.hasNext() && count++ < length) {
                    subSet.add(dataSetIt.next());
                    if (dataFiltering) {
                        dataSetIt.remove();
                    }
                }

                return subSet;
            }

            // Return the data
            return dataSet;

        } catch (IOException ex) {
            throw new RuntimeException("Unable to load static datafile: " + filename);
        }
    }

}
