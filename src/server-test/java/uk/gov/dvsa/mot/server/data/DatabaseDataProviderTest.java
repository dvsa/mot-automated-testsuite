package uk.gov.dvsa.mot.server.data;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import uk.gov.dvsa.mot.server.model.Dataset;
import uk.gov.dvsa.mot.server.model.DatasetMetrics;
import uk.gov.dvsa.mot.server.reporting.DataUsageReportGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Tests the <code>DatabaseDataProvider</code> class.
 */
public class DatabaseDataProviderTest {

    /** The first data set name. */
    private static final String DATA_SET1 = "TEST_DATA_SET1";

    /** The mock DAO. */
    private DataDao mockDao;

    /** The mock report generator. */
    private DataUsageReportGenerator mockGenerator;

    /** Used to capture the dataset metrics passed to the generator. */
    private ArgumentCaptor<Map<String, DatasetMetrics>> captor;

    /** The class under test. */
    private DatabaseDataProvider provider;

    /**
     * Shared test initialisation.
     */
    @Before
    public void setUp() {
        mockDao = mock(DataDao.class);
        QueryFileLoader mockLoader = mock(QueryFileLoader.class);
        mockGenerator = mock(DataUsageReportGenerator.class);
        provider = new DatabaseDataProvider(mockDao, mockLoader, mockGenerator);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, DatasetMetrics>> arg = ArgumentCaptor.forClass((Class) Map.class);
        captor = arg;

        given(mockLoader.loadFromFile(DATA_SET1)).willReturn(new Dataset(DATA_SET1, "sql"));
    }

    /**
     * Shared test cleanup.
     */
    @After
    public void tearDown() {
        mockDao = null;
        mockGenerator = null;
        provider = null;
        captor = null;
    }

    /**
     * Tests the <code>getUncachedDatasetEntry</code> with a successful query.
     */
    @Test
    public void getUncachedDatasetEntry_HappyPath() {
        List<String> entry1 = new ArrayList<>();
        entry1.add("abcd");
        entry1.add("123");

        List<String> entry2 = new ArrayList<>();
        entry2.add("wxyz");
        entry2.add("789");

        List<List<String>> queryResults = new ArrayList<>();
        queryResults.add(entry1);
        queryResults.add(entry2);

        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(1))).willReturn(queryResults);

        // when
        List<String> actual1 = provider.getUncachedDatasetEntry(DATA_SET1);

        // then
        assertEquals("Wrong results returned", entry1, actual1);

        // then to test the dataset is uncached, get another entry and check the cumulative metrics...

        List<String> entry3 = new ArrayList<>();
        entry3.add("efgh");
        entry3.add("456");

        List<List<String>> secondResults = new ArrayList<>();
        secondResults.add(entry3);

        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(1))).willReturn(secondResults);

        // when
        List<String> actual2 = provider.getUncachedDatasetEntry(DATA_SET1);

        // then
        assertEquals("Wrong results returned", entry3, actual2);

        // loaded immediately size is of most recent query, requested should be 2...
        checkDatasetMetrics(DATA_SET1, empty(), empty(), of(1), of(2));
    }

    /**
     * Tests the <code>getUncachedDatasetEntry</code> with an empty query.
     */
    @Test
    public void getUncachedDatasetEntry_NoData() {
        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(1))).willReturn(new ArrayList<>());

        // when
        try {
            provider.getUncachedDatasetEntry(DATA_SET1);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertEquals("Wrong error message",
                    "No more data available for dataset: TEST_DATA_SET1", ex.getMessage());
        }

        // then
        checkDatasetMetrics(DATA_SET1, empty(), empty(), of(0), of(1));
    }

    /**
     * Tests the <code>getUncachedDataset</code> with a successful query.
     */
    @Test
    public void getUncachedDataset_HappyPath() {
        List<String> entry1 = new ArrayList<>();
        entry1.add("abcd");
        entry1.add("123");

        List<String> entry2 = new ArrayList<>();
        entry2.add("wxyz");
        entry2.add("789");

        List<List<String>> queryResults = new ArrayList<>();
        queryResults.add(entry1);
        queryResults.add(entry2);

        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(10))).willReturn(queryResults);

        // when
        List<List<String>> actual1 = provider.getUncachedDataset(DATA_SET1, 10);

        // then
        assertEquals("Wrong results returned", queryResults, actual1);

        // then to test the dataset is uncached, get another entry and check the cumulative metrics...

        List<String> entry3 = new ArrayList<>();
        entry3.add("efgh");
        entry3.add("456");

        List<String> entry4 = new ArrayList<>();
        entry4.add("ijkl");
        entry4.add("001");

        List<String> entry5 = new ArrayList<>();
        entry5.add("mno");
        entry5.add("002");

        List<List<String>> secondResults = new ArrayList<>();
        secondResults.add(entry3);
        secondResults.add(entry4);
        secondResults.add(entry5);

        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(10))).willReturn(secondResults);

        // when
        List<List<String>> actual2 = provider.getUncachedDataset(DATA_SET1, 10);

        // then
        assertEquals("Wrong results returned", secondResults, actual2);

        // loaded immediately size is of most recent query, requested should be 2...
        checkDatasetMetrics(DATA_SET1, empty(), empty(), of(3), of(2));
    }

    /**
     * Tests the <code>getUncachedDataset</code> with an empty query.
     */
    @Test
    public void getUncachedDataset_NoData() {
        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(10))).willReturn(new ArrayList<>());

        // when
        try {
            provider.getUncachedDataset(DATA_SET1, 10);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertEquals("Wrong error message",
                    "No more data available for dataset: TEST_DATA_SET1", ex.getMessage());
        }

        // then
        checkDatasetMetrics(DATA_SET1, empty(), empty(), of(0), of(1));
    }

    /**
     * Tests the <code>getCachedDatasetEntry</code> with a successful query.
     */
    @Test
    public void getCachedDatasetEntry_HappyPath() {
        List<String> entry1 = new ArrayList<>();
        entry1.add("abcd");
        entry1.add("123");

        List<String> entry2 = new ArrayList<>();
        entry2.add("wxyz");
        entry2.add("789");

        List<List<String>> queryResults = new ArrayList<>();
        queryResults.add(entry1);
        queryResults.add(entry2);

        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(0))).willReturn(queryResults);

        // when
        List<String> actual1 = provider.getCachedDatasetEntry(DATA_SET1);

        // then
        assertEquals("Wrong results returned", entry1, actual1);

        // then to test the dataset is cached, get another entry...

        // when
        List<String> actual2 = provider.getCachedDatasetEntry(DATA_SET1);

        // then
        assertEquals("Wrong results returned", entry2, actual2);

        // then get another entry (trigger no more data) and check the cumulative metrics...

        // when
        try {
            // when
            provider.getCachedDatasetEntry(DATA_SET1);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            // then
            assertEquals("Wrong error message",
                    "No more data available for dataset: TEST_DATA_SET1", ex.getMessage());
        }

        // check the DAO was only called once...
        verify(mockDao, times(1)).loadDataset(any(Dataset.class), eq(0));

        // cache size should be 2, requested should be 3...
        checkDatasetMetrics(DATA_SET1, of(2), of(3), empty(), empty());
    }

    /**
     * Tests the <code>getCachedDataset</code> with an empty query.
     */
    @Test
    public void getCachedDataset_NoData() {
        // given
        given(mockDao.loadDataset(any(Dataset.class), eq(0))).willReturn(new ArrayList<>());

        // when
        try {
            provider.getCachedDatasetEntry(DATA_SET1);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertEquals("Wrong error message",
                    "No more data available for dataset: TEST_DATA_SET1", ex.getMessage());
        }

        // then
        checkDatasetMetrics(DATA_SET1, of(0), of(1), empty(), empty());
    }

    /**
     * Check the dataset metrics for the specified dataset, captured when passed to the report generator.
     * @param datasetName                           The dataset name
     * @param expectedCacheSize                     The expected cache size
     * @param expectedCacheRequested                The expected cache requested amount
     * @param expectedLoadedImmediatelySize         The expected loaded immediately size
     * @param expectedLoadedImmediatelyRequested    The expected loaded immediately requested amount
     */
    private void checkDatasetMetrics(String datasetName, Optional<Integer> expectedCacheSize,
                Optional<Integer> expectedCacheRequested, Optional<Integer> expectedLoadedImmediatelySize,
                    Optional<Integer> expectedLoadedImmediatelyRequested) {
        provider.outputUsageReport();
        verify(mockGenerator).generateReport(captor.capture());
        DatasetMetrics metrics = captor.getValue().get(datasetName);
        assertNotNull("Dataset metrics for " + datasetName + " not found", metrics);

        assertEquals("Wrong expected cache size",
                expectedCacheSize, metrics.getCacheSize());

        assertEquals("Wrong expected cache requested",
                expectedCacheRequested, metrics.getCacheRequested());

        assertEquals("Wrong loaded immediately size",
                expectedLoadedImmediatelySize, metrics.getLoadedImmediatelySize());

        assertEquals("Wrong loaded immediately requested",
                expectedLoadedImmediatelyRequested, metrics.getLoadedImmediatelyRequested());
    }
}
