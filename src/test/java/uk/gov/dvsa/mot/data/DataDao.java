package uk.gov.dvsa.mot.data;

import java.util.List;

/**
 * Handles database queries to load data.
 */
public interface DataDao {

    /**
     * Loads an entry from the specified test data set.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    List<String> loadData(String dataSetName);
}
