package uk.gov.dvsa.mot.server.model;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests data filtering by the <code>Dataset</code> and <code>Filter</code> classes, and metrics recording.
 */
public class DatasetTest {

    /**
     * Tests that when datasets have no filters, results can be repeated.
     */
    @Test
    public void noFilteringAllowsRepeatedResults() {
        final Dataset dataset = new Dataset("TEST1", "sql");

        List<String> entry1 = new ArrayList<>();
        entry1.add("abcd");
        entry1.add("123");

        List<String> entry2 = new ArrayList<>();
        entry2.add("wxyz");
        entry2.add("789");

        List<String> entry3 = new ArrayList<>();
        entry3.add("wxyz"); // same key as entry2
        entry3.add("456");

        List<String> entry4 = new ArrayList<>();
        entry4.add("efgh");
        entry4.add("001");

        List<List<String>> queryResults = new ArrayList<>();
        queryResults.add(entry1);
        queryResults.add(entry2);
        queryResults.add(entry3);
        queryResults.add(entry4);

        // given
        dataset.populateCache(queryResults, 100L);

        // when
        final List<String> result1 = dataset.getCachedResult();
        final List<String> result2 = dataset.getCachedResult();
        final List<String> result3 = dataset.getCachedResult();
        final List<String> result4 = dataset.getCachedResult();

        // then
        assertEquals("Wrong result 1", entry1, result1);
        assertEquals("Wrong result 2", entry2, result2);
        assertEquals("Wrong result 3", entry3, result3);
        assertEquals("Wrong result 4", entry4, result4);

        // and
        try {
            dataset.getCachedResult();
            fail("Expected to run out of data");

        } catch (IllegalStateException ex) {
            assertEquals("Wrong error", "No more data available for dataset: TEST1", ex.getMessage());
        }

        // and
        assertEquals("Wrong cache size", Optional.of(4), dataset.getMetrics().getCacheSize());
        assertEquals("Wrong timing", Optional.of(100L), dataset.getMetrics().getTimingMilliseconds());
        assertEquals("Wrong cache requested", Optional.of(5), dataset.getMetrics().getCacheRequested());
        assertEquals("Wrong filtered out", Optional.empty(), dataset.getMetrics().getFilteredOut());
    }

    /**
     * Tests that when a dataset has a filter, results are unique.
     */
    @Test
    public void singleFilterEnsuresUniqueResults() {
        final Dataset dataset = new Dataset("TEST1", "sql", new Filter("test-filter"));

        List<String> entry1 = new ArrayList<>();
        entry1.add("abcd");
        entry1.add("123");

        List<String> entry2 = new ArrayList<>();
        entry2.add("wxyz");
        entry2.add("789");

        List<String> entry3 = new ArrayList<>();
        entry3.add("wxyz"); // same key as entry2
        entry3.add("456");

        List<String> entry4 = new ArrayList<>();
        entry4.add("efgh");
        entry4.add("001");

        List<List<String>> queryResults = new ArrayList<>();
        queryResults.add(entry1);
        queryResults.add(entry2);
        queryResults.add(entry3);
        queryResults.add(entry4);

        // given
        dataset.populateCache(queryResults, 100L);

        // when
        List<String> result1 = dataset.getCachedResult();
        List<String> result2 = dataset.getCachedResult();
        List<String> result3 = dataset.getCachedResult();

        // then
        assertEquals("Wrong result 1", entry1, result1);
        assertEquals("Wrong result 2", entry2, result2);
        assertEquals("Wrong result 3", entry4, result3); // ignores entry 3, returns entry 4

        // and
        try {
            dataset.getCachedResult();
            fail("Expected to run out of data");

        } catch (IllegalStateException ex) {
            assertEquals("Wrong error", "No more data available for dataset: TEST1", ex.getMessage());
        }

        // and
        assertEquals("Wrong cache size", Optional.of(4), dataset.getMetrics().getCacheSize());
        assertEquals("Wrong timing", Optional.of(100L), dataset.getMetrics().getTimingMilliseconds());
        assertEquals("Wrong cache requested", Optional.of(4), dataset.getMetrics().getCacheRequested());
        assertEquals("Wrong filtered out", Optional.of(1), dataset.getMetrics().getFilteredOut());
    }

    /**
     * Tests that when datasets have shared filters, results are unique.
     */
    @Test
    public void sharedFilterEnsuresUniqueResults() {
        Filter filter = new Filter("test-filter");
        final Dataset dataset1 = new Dataset("TEST1", "sql", filter);
        final Dataset dataset2 = new Dataset("TEST2", "sql2", filter); // shared same filter

        List<String> entry1 = new ArrayList<>();
        entry1.add("001");
        entry1.add("aaa");

        List<String> entry2 = new ArrayList<>();
        entry2.add("002");
        entry2.add("bbb");

        List<String> entry3 = new ArrayList<>();
        entry3.add("003");
        entry3.add("ccc");

        List<String> entry4 = new ArrayList<>();
        entry4.add("004");
        entry4.add("ddd");

        List<List<String>> queryResults1 = new ArrayList<>();
        queryResults1.add(entry1);
        queryResults1.add(entry2);
        queryResults1.add(entry3);
        queryResults1.add(entry4);

        List<String> entry5 = new ArrayList<>();
        entry5.add("005"); // different
        entry5.add("eee");

        List<String> entry6 = new ArrayList<>();
        entry6.add("002"); // same
        entry6.add("bbb");

        List<String> entry7 = new ArrayList<>();
        entry7.add("007"); // different
        entry7.add("fff");

        List<String> entry8 = new ArrayList<>();
        entry8.add("004"); // same
        entry8.add("ddd");

        List<List<String>> queryResults2 = new ArrayList<>();
        queryResults2.add(entry5);
        queryResults2.add(entry6);
        queryResults2.add(entry7);
        queryResults2.add(entry8);

        // given
        dataset1.populateCache(queryResults1, 200L);
        dataset2.populateCache(queryResults2, 300L);

        // when
        final List<String> result1 = dataset1.getCachedResult();
        final List<String> result2 = dataset1.getCachedResult();
        final List<String> result3 = dataset1.getCachedResult();
        final List<String> result4 = dataset1.getCachedResult();
        final List<String> result5 = dataset2.getCachedResult();
        final List<String> result6 = dataset2.getCachedResult();

        // then
        assertEquals("Wrong result 1", entry1, result1); // first datset is as-is
        assertEquals("Wrong result 2", entry2, result2); // first datset is as-is
        assertEquals("Wrong result 3", entry3, result3); // first datset is as-is
        assertEquals("Wrong result 4", entry4, result4); // first datset is as-is
        assertEquals("Wrong result 3", entry5, result5); // second datset, not filtered out
        assertEquals("Wrong result 3", entry7, result6); // second dataset, entry6 is filtered out

        // and
        try {
            dataset1.getCachedResult();
            fail("Expected to run out of data");

        } catch (IllegalStateException ex) {
            assertEquals("Wrong error", "No more data available for dataset: TEST1", ex.getMessage());
        }

        try {
            // entry7 should be filtered out, resulting in no more data
            dataset2.getCachedResult();
            fail("Expected to run out of data");

        } catch (IllegalStateException ex) {
            assertEquals("Wrong error", "No more data available for dataset: TEST2", ex.getMessage());
        }

        // and
        assertEquals("Wrong cache size", Optional.of(4), dataset1.getMetrics().getCacheSize());
        assertEquals("Wrong timing", Optional.of(200L), dataset1.getMetrics().getTimingMilliseconds());
        assertEquals("Wrong cache requested", Optional.of(5), dataset1.getMetrics().getCacheRequested());
        assertEquals("Wrong filtered out", Optional.empty(), dataset1.getMetrics().getFilteredOut());

        // and
        assertEquals("Wrong cache size", Optional.of(4), dataset2.getMetrics().getCacheSize());
        assertEquals("Wrong timing", Optional.of(300L), dataset2.getMetrics().getTimingMilliseconds());
        assertEquals("Wrong cache requested", Optional.of(3), dataset2.getMetrics().getCacheRequested());
        assertEquals("Wrong filtered out", Optional.of(2), dataset2.getMetrics().getFilteredOut());
    }
}
