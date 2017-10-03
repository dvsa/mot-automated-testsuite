package uk.gov.dvsa.mot.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles provision of test data, using existing data from a test database.
 */
public class DatabaseDataProvider {

    /**
     * The logger to use.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataProvider.class);

    /**
     * The DataDao to use.
     */
    private final DataDao dataDao;

    /**
     * The cached datasets to use, keyed by name.
     */
    private final Map<String, List<List<String>>> datasets;

    private final List<String> largeDatasets;
    private final List<String> mediumDatasets;

    /**
     * Creates a new instance.
     *
     * @param dataDao The user DAO to use
     */
    public DatabaseDataProvider(DataDao dataDao) {
        logger.debug("Creating DatabaseDataProvider...");
        this.dataDao = dataDao;
        this.datasets = new HashMap<>();

        // would need to be maintained somewhere
        this.largeDatasets = new ArrayList<>();
        largeDatasets.add("AO1_USER"); // 25
        largeDatasets.add("MOT_TESTER_CLASS_4"); // 20
        largeDatasets.add("VEHICLE_CLASS_4"); // 25

        // would also need to be maintained somewhere
        this.mediumDatasets = new ArrayList<>();
        mediumDatasets.add("2FA_CARD_USER"); // 5
        mediumDatasets.add("CSCO_USER"); // 10
        mediumDatasets.add("SITE"); // 8
        mediumDatasets.add("VEHICLE_CLASS_4_BEFORE_2010"); // 5
        mediumDatasets.add("VEHICLE_EXAMINER_USER"); // 9
    }

    /**
     * test.
     */
    @Transactional
    public void testData() {

        // the raw data
        final List<Dataset> datasets = new ArrayList<>();

        // the various filters
        final Filter userFilter = new Filter("user");
        final Filter aeFilter = new Filter("ae");
        final Filter vehicleFilter = new Filter("vehicle");
        final Filter siteFilter = new Filter("site");

        // load all data
        load(datasets, "2FA_CARD_USER", userFilter, 5); // need 5 - first column is username
        // "2FA_CARD_UNUSED"
        load(datasets, "AEDM_AND_AMBER_SITE", userFilter, 1); // need 1
        load(datasets, "AEDM_AND_GREEN_SITE", userFilter, 1); // need 1
        load(datasets, "AEDM_AND_NON_AED_USER", userFilter, 1); // need 1
        load(datasets, "AEDM_AND_RED_SITE", userFilter, 1); // need 1
        load(datasets, "AEDM_AND_TESTER_AT_SITE", userFilter, 1); // need 2
        load(datasets, "AEDM_USER", userFilter, 2); // need 2
        load(datasets, "AED_AND_GROUP_A_SITE", userFilter, 1); // need 1
        load(datasets, "AED_AND_TESTER", userFilter, 1); // need 1
        load(datasets, "AE_NOT_REJECTED", aeFilter, 4); // need 4 - first column is ae ref
        load(datasets, "AE_USER", aeFilter, 1); // need 1
        load(datasets, "AE_WITH_AEDM", aeFilter, 1); // need 1
        load(datasets, "AE_WITH_ASSIGNED_SITE", aeFilter, 1); // need 1
        load(datasets, "AE_WITH_NO_AEDM", aeFilter, 2); // need 2
        load(datasets, "AE_WITH_UNASSIGNED_SITE", aeFilter, 1); // need 1
        load(datasets, "AO1_USER", userFilter, 25); // need 25
        load(datasets, "AUTHORISED_EXAMINER", aeFilter, 2); // need 2
        //load(datasets, "CONTINGENCY_CODE");
        load(datasets, "CSCO_USER", userFilter, 10); // need 10
        load(datasets, "DVLA_MANAGER_USER", userFilter, 3); // need 3
        load(datasets, "DVLA_OPERATIVE_USER", userFilter, 1); // need 1
        load(datasets, "DVLA_VEHICLE", vehicleFilter, 1); // need 1 - first column is reg
        load(datasets, "FINANCE_USER", userFilter, 1); // need 1
        //load(datasets, "LAST_FAILED_MOT_CLASS_4");
        //load(datasets, "LAST_USED_TEST_EMAIL");
        //load(datasets, "LATEST_TEST_USER");
        load(datasets, "MOT_TESTER_CLASS_1", userFilter, 2); // need 2
        load(datasets, "MOT_TESTER_CLASS_2", userFilter, 2); // need 2
        load(datasets, "MOT_TESTER_CLASS_3", userFilter, 2); // need 2
        load(datasets, "MOT_TESTER_CLASS_4", userFilter, 20); // need 20
        load(datasets, "MOT_TESTER_CLASS_5", userFilter, 2); // need 2
        load(datasets, "MOT_TESTER_CLASS_7", userFilter, 2); // need 2
        load(datasets, "SCHEME_MGR", userFilter, 4); // need 4
        load(datasets, "SCHEME_USER", userFilter, 2); // need 2
        load(datasets, "SITE", siteFilter, 8); // need 8 - first column is site id
        load(datasets, "SITE_ADMIN", userFilter, 1); // need 1
        load(datasets, "SITE_LOCATION_INFORMATION", siteFilter, 2); // need 2 - first column is site id
        load(datasets, "SITE_MGR_AND_OTHER_TESTER", userFilter, 3); // need 3
        load(datasets, "SITE_MGR_AND_TESTER_CLASS_4", userFilter, 3); // need 3
        load(datasets, "TESTER_GROUP_B_AND_NOT_A", userFilter, 2); // need 2
        load(datasets, "TESTER_WITH_2_MONTH_HISTORY", userFilter, 2); // need 2
        load(datasets, "TESTER_WITH_LICENCE", userFilter, 3); // need 3
        load(datasets, "VEHICLE_CLASS_1", vehicleFilter, 1); // need 1
        load(datasets, "VEHICLE_CLASS_2_RED", vehicleFilter, 1); // need 1
        load(datasets, "VEHICLE_CLASS_3", vehicleFilter, 3); // need 3
        load(datasets, "VEHICLE_CLASS_4", vehicleFilter, 25); // need 25
        load(datasets, "VEHICLE_CLASS_4_AFTER_2010", vehicleFilter, 2); // need 2
        load(datasets, "VEHICLE_CLASS_4_BEFORE_2010", vehicleFilter, 5); // need 5
        load(datasets, "VEHICLE_CLASS_4_HISTORIC_10_DAYS", vehicleFilter, 3); // need 3
        load(datasets, "VEHICLE_CLASS_4_NOT_UPDATED_TODAY", vehicleFilter, 1); // need 1
        load(datasets, "VEHICLE_CLASS_4_WITH_MOT", vehicleFilter, 1); // need 1
        load(datasets, "VEHICLE_CLASS_5_DIESEL", vehicleFilter,1); // need 1
        load(datasets, "VEHICLE_CLASS_7", vehicleFilter, 1); // need 1
        load(datasets, "VEHICLE_EXAMINER_USER", userFilter, 9); // need 9
        load(datasets, "VE_AND_NOT_AO1_USER", userFilter, 1); // need 1

        datasets.forEach(dataset ->
                logger.info("Dataset {} has {} entries before filtering", dataset.getName(), dataset.getData().size()));

        /*
        datasets.stream()
                // apply filtering in reverse order of data set size
                .sorted(Comparator.comparing(entry -> entry.getData().size()))
                .forEach(dataset -> {
                    logger.info("Filtering {} which has {} entries", dataset.getName(), dataset.getData().size());
                    if (largeDatasets.contains(dataset.getName())) {
                        dataset.filter(25);
                    } else if (mediumDatasets.contains(dataset.getName())) {
                        dataset.filter(10);
                    } else {
                        dataset.filter(4);
                    }
                    logger.info("Filtered {} down to {} entries", dataset.getName(), dataset.getData().size());
                });
        */

        datasets.stream()
                .filter(dataset -> dataset.getSize() == DatasetSize.Small)
                .forEach(dataset -> {
                    logger.info("Filtering {} which has {} entries", dataset.getName(), dataset.getData().size());
                    dataset.filter(4);
                    logger.info("Filtered {} down to {} entries", dataset.getName(), dataset.getData().size());
                });

        datasets.stream()
                .filter(dataset -> dataset.getSize() == DatasetSize.Medium)
                .forEach(dataset -> {
                    logger.info("Filtering {} which has {} entries", dataset.getName(), dataset.getData().size());
                    dataset.filter(10);
                    logger.info("Filtered {} down to {} entries", dataset.getName(), dataset.getData().size());
                });

        datasets.stream()
                .filter(dataset -> dataset.getSize() == DatasetSize.Large)
                .forEach(dataset -> {
                    logger.info("Filtering {} which has {} entries", dataset.getName(), dataset.getData().size());
                    dataset.filter(25);
                    logger.info("Filtered {} down to {} entries", dataset.getName(), dataset.getData().size());
                });

        datasets.forEach(dataset -> {
            if (dataset.getData().size() > dataset.getAmountNeeded() + 3) {
                //logger.info("Dataset {} has sufficient entries after filtering", dataset.getName());

            } else if (dataset.getData().size() >= dataset.getAmountNeeded()) {
                //logger.info("Dataset {} has sufficient entries after filtering (but only just!)", dataset.getName());

            } else {
                logger.info("Dataset {} doesn't have sufficient entries after filtering (got {} but needed {})",
                        dataset.getName(), dataset.getData().size(), dataset.getAmountNeeded());
            }
        });
    }

    private void load(List<Dataset> datasets, String datasetName, Filter filter, int amountNeeded) {
        List<List<String>> dataset = dataDao.loadDataset(datasetName);

        final DatasetSize size;
        if (largeDatasets.contains(datasetName)) {
            size = DatasetSize.Large;
        } else if (mediumDatasets.contains(datasetName)) {
            size = DatasetSize.Medium;
        } else {
            size = DatasetSize.Small;
        }

        datasets.add(new Dataset(datasetName, dataset, filter, amountNeeded, size));
    }


    /**
     * Get a cached entry from the specified test data set (loading and caching if needed), and removes it from the
     * in-memory cache.
     *
     * @param dataSetName The data set name
     * @return The data entry
     */
    @Transactional
    public List<String> getCachedDatasetEntry(String dataSetName) {
        List<List<String>> dataset;
        if (!datasets.containsKey(dataSetName)) {
            // dataset not cached yet, so load and cache it
            dataset = dataDao.loadDataset(dataSetName);
            datasets.put(dataSetName, dataset);

        } else {
            dataset = datasets.get(dataSetName);
        }

        if (dataset.size() < 1) {
            String message = "No more data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        List<String> entry = dataset.get(0);
        dataset.remove(entry);

        logger.debug("Using {} from dataset {}", entry, dataSetName);
        return entry;
    }

    /**
     * Get the first entry from the specified test data set, by executing the query immediately, ignoring any caches.
     *
     * @param dataSetName The data set name
     * @return The data entry
     */
    @Transactional
    public List<String> getUncachedDatasetEntry(String dataSetName) {
        List<List<String>> dataset = dataDao.loadDataset(dataSetName);

        if (dataset.size() < 1) {
            String message = "No more data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        List<String> entry = dataset.get(0);

        logger.debug("Using {} from dataset {}", entry, dataSetName);
        return entry;
    }

    /**
     * Get the specified number of entries from the specified test data set, by executing the query immediately,
     * ignoring any caches.
     *
     * @param dataSetName The data set name
     * @param length      The number of data set entries
     * @return The data entries
     */
    @Transactional
    public List<List<String>> getUncachedDataset(String dataSetName, int length) {
        List<List<String>> dataset = dataDao.loadDataset(dataSetName, length);

        if (dataset.size() < 1) {
            String message = "No data available for dataset: " + dataSetName;
            logger.error(message);
            throw new IllegalStateException(message);
        }

        return dataset;
    }

    enum DatasetSize {
        Small, Medium, Large
    }

    static class Dataset {
        private static final Logger logger = LoggerFactory.getLogger(Dataset.class);

        private final String name;
        private List<List<String>> data;
        private final Filter filter;
        private final int amountNeeded;
        private final DatasetSize size;

        public Dataset(String name, List<List<String>> data, Filter filter, int amountNeeded, DatasetSize size) {
            this.name = name;
            this.data = data;
            this.filter = filter;
            this.amountNeeded = amountNeeded;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public List<List<String>> getData() {
            return data;
        }

        public int getAmountNeeded() {
            return amountNeeded;
        }

        public DatasetSize getSize() {
            return size;
        }

        public void filter(int max) {
            if (filter != null) {

                logger.info("Attempting to find {} unique values out of {}", max, data.size());
                logger.info("Filter {} has {} entries so far", filter.getName(), filter.data.size());

                List<List<String>> uniqueData = new ArrayList<>();
                List<String> uniqueValues = new ArrayList<>();
                for (List<String> row : data) {
                    String value = row.get(0);

                    // looking for unique within the data loaded (some queries aren't distinct)
                    if (!uniqueValues.contains(value)) {
                        uniqueData.add(row);
                        uniqueValues.add(value);
                    }
                }
                logger.info("Out of {} entries, {} are unique", data.size(), uniqueData.size());

                List<List<String>> filteredData = new ArrayList<>();
                List<String> filteredValues = new ArrayList<>();
                for (List<String> row : uniqueData) {
                    String value = row.get(0);

                    // looking for globally unique values (using the filter)
                    if (filter.isUnique(value)) {
                        filteredData.add(row);
                        filteredValues.add(value);
                    }

                    if (filteredData.size() >= max) {
                        break;
                    }
                }

                /*
                List<List<String>> filteredData = data.stream()
                        .filter(value -> filter.isUnique(value.get(0)))
                        //.limit(max)
                        .collect(Collectors.toList());

                List<String> filteredValues = filteredData.stream()
                        .map(row -> row.get(0))
                        .collect(Collectors.toList());
                */
                data = filteredData;
                filter.addValues(filteredValues);

                logger.info("Found {} unique entries", filteredData.size());
            }
        }
    }

    class Filter {
        private final String name;
        private final List<String> data = new ArrayList<>();

        public Filter(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean isUnique(String value) {
            return !data.contains(value);
        }

        public void addValues(List<String> values) {
            data.addAll(values);
        }
    }
}
