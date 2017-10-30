package uk.gov.dvsa.mot.server.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.mock.env.MockEnvironment;
import uk.gov.dvsa.mot.server.model.Dataset;
import uk.gov.dvsa.mot.server.model.Filter;

/**
 * Tests the <code>QueryFileLoader</code> class.
 */
public class QueryFileLoaderTest {

    /**
     * Tests the <code>loadFromFile</code> method with a valid file in a top-level directory, with filtering off.
     */
    @Test
    public void loadFromFile_HappyPathTopLevelFilteringOff() {
        QueryFileLoader loader = createLoader(false);
        Dataset dataset = loader.loadFromFile("TEST_DB_QUERY1");

        assertEquals("Wrong name", "TEST_DB_QUERY1", dataset.getName());
        assertTrue("Wrong SQL", dataset.getSql().startsWith("select 1 as test_column"));
        assertTrue("Wrong SQL", dataset.getSql().endsWith("where nst.id = 1001 -- dummy sql comment\n"));
        assertNull("Should be no filter", dataset.getFilter());
    }

    /**
     * Tests the <code>loadFromFile</code> method with a valid file in a sub-directory, with filtering off.
     */
    @Test
    public void loadFromFile_HappyPathSubDirFilteringOff() {
        QueryFileLoader loader = createLoader(false);
        Dataset dataset = loader.loadFromFile("TEST_DB_QUERY2");

        assertEquals("Wrong name", "TEST_DB_QUERY2", dataset.getName());
        assertTrue("Wrong SQL", dataset.getSql().startsWith("select 2 as other_test_column"));
        assertTrue("Wrong SQL", dataset.getSql().endsWith("where onst.id = 2 -- another dummy sql comment\n"));
        assertNull("Should be no filter", dataset.getFilter());
    }

    /**
     * Tests the <code>loadFromFile</code> method with a valid file in a top-level directory, with filtering on.
     */
    @Test
    public void loadFromFile_HappyPathTopLevelFilteringOn() {
        QueryFileLoader loader = createLoader(true);
        Dataset dataset = loader.loadFromFile("TEST_DB_QUERY1");

        assertEquals("Wrong name", "TEST_DB_QUERY1", dataset.getName());
        assertTrue("Wrong SQL", dataset.getSql().startsWith("select 1 as test_column"));
        assertTrue("Wrong SQL", dataset.getSql().endsWith("where nst.id = 1001 -- dummy sql comment\n"));
        assertNull("Should be no filter", dataset.getFilter()); // no filter as in top-level directory
    }

    /**
     * Tests the <code>loadFromFile</code> method with a valid file in a sub-directory, with filtering on.
     */
    @Test
    public void loadFromFile_HappyPathSubDirFilteringOn() {
        QueryFileLoader loader = createLoader(true);
        Dataset dataset = loader.loadFromFile("TEST_DB_QUERY2");

        assertEquals("Wrong name", "TEST_DB_QUERY2", dataset.getName());
        assertTrue("Wrong SQL", dataset.getSql().startsWith("select 2 as other_test_column"));
        assertTrue("Wrong SQL", dataset.getSql().endsWith("where onst.id = 2 -- another dummy sql comment\n"));
        assertNotNull("Should be a filter", dataset.getFilter());
        assertEquals("Wrong filter name", "filter1", dataset.getFilter().getName());
    }

    /**
     * Tests the <code>loadFromFile</code> method with a valid files in the same sub-directory, with filtering on,
     * share the same filter.
     */
    @Test
    public void loadFromFile_HappyPathSharedFilters() {
        QueryFileLoader loader = createLoader(true);
        final Dataset dataset1 = loader.loadFromFile("TEST_DB_QUERY2");
        final Dataset dataset2 = loader.loadFromFile("TEST_DB_QUERY3");

        assertEquals("Wrong name", "TEST_DB_QUERY2", dataset1.getName());
        assertTrue("Wrong SQL", dataset1.getSql().startsWith("select 2 as other_test_column"));
        assertTrue("Wrong SQL", dataset1.getSql().endsWith("where onst.id = 2 -- another dummy sql comment\n"));

        assertEquals("Wrong name", "TEST_DB_QUERY3", dataset2.getName());
        assertTrue("Wrong SQL", dataset2.getSql().startsWith("select 3 as yet_another_test_column"));
        assertTrue("Wrong SQL", dataset2.getSql().endsWith("where yanst.id = 3 -- yet another dummy sql comment\n"));

        Filter filter1 = dataset1.getFilter();
        Filter filter2 = dataset2.getFilter();

        assertNotNull("Should be a filter", filter1);
        assertNotNull("Should be a filter", filter2);
        assertEquals("Wrong filter name", "filter1", filter1.getName());
        assertEquals("Wrong filter name", "filter1", filter2.getName());
        assertSame("Should be same filter", filter1, filter2);
    }

    /**
     * Tests the <code>loadFromFile</code> method with a valid files in different sub-directories, with filtering on,
     * have a different filter.
     */
    @Test
    public void loadFromFile_HappyPathDifferentFilters() {
        QueryFileLoader loader = createLoader(true);
        final Dataset dataset1 = loader.loadFromFile("TEST_DB_QUERY2");
        final Dataset dataset2 = loader.loadFromFile("TEST_DB_QUERY4");

        assertEquals("Wrong name", "TEST_DB_QUERY2", dataset1.getName());
        assertTrue("Wrong SQL", dataset1.getSql().startsWith("select 2 as other_test_column"));
        assertTrue("Wrong SQL", dataset1.getSql().endsWith("where onst.id = 2 -- another dummy sql comment\n"));

        assertEquals("Wrong name", "TEST_DB_QUERY4", dataset2.getName());
        assertTrue("Wrong SQL", dataset2.getSql().startsWith("select wibble"));
        assertTrue("Wrong SQL", dataset2.getSql().endsWith("from nowhere\n"));

        Filter filter1 = dataset1.getFilter();
        Filter filter2 = dataset2.getFilter();

        assertNotNull("Should be a filter", filter1);
        assertNotNull("Should be a filter", filter2);
        assertEquals("Wrong filter name", "filter1", filter1.getName());
        assertEquals("Wrong filter name", "filter2", filter2.getName());
        assertNotSame("Should be different filter", filter1, filter2);
    }

    /**
     * Tests the <code>loadFromFile</code> method with a file that doesn't exist.
     */
    @Test(expected = IllegalStateException.class)
    public void loadFromFile_NoSuchFile() {
        QueryFileLoader loader = createLoader(true);
        loader.loadFromFile("NO_SUCH_FILE");
    }

    /**
     * Creates the loader, using the specified filtering configuration.
     * @param filter            Whether filtering is on
     * @return The class under test
     */
    private QueryFileLoader createLoader(boolean filter) {
        Environment env = new MockEnvironment().withProperty("dataFiltering", String.valueOf(filter));
        return new QueryFileLoader(new PathMatchingResourcePatternResolver(), env);
    }
}
