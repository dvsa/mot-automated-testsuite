package uk.gov.dvsa.mot.fixtures.mot;

import cucumber.api.java8.En;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.WebDriverWrapper;
import uk.gov.dvsa.mot.framework.elasticsearch.ElasticSearchResult;
import uk.gov.dvsa.mot.framework.excel.ExcelDocument;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

/**
 * Step definitions specific to the <i>Elastic search</i> feature.
 */
public class ElasticSearchStepDefinitions implements En {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchStepDefinitions.class);

    /** The web driver to use. */
    private final WebDriverWrapper driverWrapper;

    /** The map of results for each test to be compared. */
    private Map<String, ArrayList<ElasticSearchResult>> searchResultsData;

    /**
     * Creates a new instance.
     * @param driverWrapper the driver wrapper to use
     */
    @Inject
    public ElasticSearchStepDefinitions(WebDriverWrapper driverWrapper) {
        logger.debug("Creating ElasticSearchStepDefinitions...");

        this.driverWrapper = driverWrapper;
        this.searchResultsData = new HashMap<>();

        When("^I query elastic search with \"([^\"]+)\" as \"([^\"]+)\"$", (String csvName, String dataKey) ->
                searchForRfrsWithCsvData(csvName, dataKey)
        );

        And("^I compare the search results for \"([^\"]+)\" and \"([^\"]+)\" with data \"([^\"]+)\"$",
                (String result1, String result2, String csvFile) ->
                new ExcelDocument().compareTestResults(getSearchResultData(result1), getSearchResultData(result2),
                        csvFile)
        );
    }

    /**
     * Iterates through a csv file and records the total results and top 10 search results.
     * @param csvFile the file name for the csv file
     */
    private void searchForRfrsWithCsvData(String csvFile, String dataKey) {

        ArrayList<ElasticSearchResult> results = new ArrayList<>();

        for (CSVRecord record: processCsvDataForDefects(csvFile)) {
            int totalCount = 0;
            ArrayList<String> top10 = new ArrayList<String>();

            // And I enter <text> into the <search-main> field (has no label)
            driverWrapper.enterIntoFieldWithId(record.get(0), "search-main");
            // And I press the "Search" button
            driverWrapper.pressButton("Search");

            //And I get the total count data
            totalCount = Integer.parseInt(
                    driverWrapper.getElementText("h2", "Your search for", "strong[2]"));

            if (totalCount != 0) {
                top10 = driverWrapper.getTextFromElementsByClass(
                        "list-unstyled eu-defect__list eu-defect__list--interim", "li");
            } else {
                top10.add("No results found for " + record.get(0));
            }

            //Add the latest result to the array
            results.add(new ElasticSearchResult(record.get(0), totalCount, top10));
        }
        searchResultsData.put(dataKey, results);
    }

    /**
     * Processes a csv file to provide the search terms needed for a particular csv file.
     * @param csvFile the csv file to be processed
     * @return returns an array list of csv records
     */
    private ArrayList<CSVRecord> processCsvDataForDefects(String csvFile) {
        ArrayList<CSVRecord> records = new ArrayList<>();

        File defectsData = new File("src/main/java/uk/gov/dvsa/mot/framework/elasticsearch/" + csvFile + ".csv");

        try {
            CSVParser parser = CSVParser.parse(defectsData, Charset.defaultCharset(), CSVFormat.EXCEL);
            records.addAll(parser.getRecords());
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }

        return records;
    }

    /**
     * Sets the data in the map to be compared later.
     * @param dataKey the name of the key to store the data under
     */
    private void setSearchResultData(ArrayList<ElasticSearchResult> searchResults, String dataKey) {
        this.searchResultsData.put(dataKey, searchResults);
    }

    /**
     * Gets the mapped data from the search results.
     * @param key the key the data is mapped under
     * @return returns an array list of the Elastic Search Reuslts
     */
    private ArrayList<ElasticSearchResult> getSearchResultData(String key) {
        return searchResultsData.get(key);
    }
}
