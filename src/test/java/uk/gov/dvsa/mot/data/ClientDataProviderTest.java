package uk.gov.dvsa.mot.data;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.RestTemplate;
import uk.gov.dvsa.mot.utils.config.TestsuiteConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the <code>ClientDataProvider</code> class.
 */
public class ClientDataProviderTest {

    /** Simulates the data server URL. */
    private static final String URL = "http://dataserver:1234";

    /** Simulates the data set name. */
    private static final String DATA_SET = "TEST_DATA_SET";

    /** The mock REST server to use. */
    private MockRestServiceServer server = null;

    /** The class under test. */
    private DataProvider dataProvider = null;

    /**
     * Shared test initialisation.
     */
    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        TestsuiteConfig env = new TestsuiteConfig();
        env.setProperty("dataserverUrl", URL);
        dataProvider = new ClientDataProvider(env, restTemplate);
    }

    /**
     * Shared test clean up.
     */
    @After
    public void tearDown() {
        server.verify();

        server = null;
        dataProvider = null;
    }

    /**
     * Tests the <code>getCachedDatasetEntry</code> with a valid populated response.
     */
    @Test
    public void getCachedDatasetEntry_HappyPath() {
        List<String> expected = new ArrayList<>();
        expected.add("54321");
        expected.add("XYZ");
        expected.add("01-12-2017");

        mockServerRequest(URL + "/entry/cached/" + DATA_SET,
                withSuccess("[ \"54321\", \"XYZ\", \"01-12-2017\" ]", MediaType.APPLICATION_JSON_UTF8));

        List<String> actual = dataProvider.getCachedDatasetEntry(DATA_SET);
        assertEquals("Wrong result", expected, actual);
    }

    /**
     * Tests the <code>getCachedDatasetEntry</code> with a 404 response.
     */
    @Test
    public void getCachedDatasetEntry_NoData() {
        mockServerRequest(URL + "/entry/cached/" + DATA_SET, withStatus(HttpStatus.NOT_FOUND));

        try {
            dataProvider.getCachedDatasetEntry(DATA_SET);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertTrue("Wrong error message", ex.getMessage().contains("No more data"));
            assertTrue("Wrong error message", ex.getMessage().contains(DATA_SET));
        }
    }

    /**
     * Tests the <code>getCachedDatasetEntry</code> with a 500 response.
     */
    @Test
    public void getCachedDatasetEntry_Error() {
        mockServerRequest(URL + "/entry/cached/" + DATA_SET, withServerError().body("{ \"error\": \"Oops\" }"));

        try {
            dataProvider.getCachedDatasetEntry(DATA_SET);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertTrue("Wrong error message", ex.getMessage().contains("Oops"));
        }
    }

    /**
     * Tests the <code>getUncachedDatasetEntry</code> with a valid populated response.
     */
    @Test
    public void getUncachedDatasetEntry_HappyPath() {
        List<String> expected = new ArrayList<>();
        expected.add("54321");
        expected.add("XYZ");
        expected.add("01-12-2017");

        mockServerRequest(URL + "/entry/uncached/" + DATA_SET,
                withSuccess("[ \"54321\", \"XYZ\", \"01-12-2017\" ]", MediaType.APPLICATION_JSON_UTF8));

        List<String> actual = dataProvider.getUncachedDatasetEntry(DATA_SET);
        assertEquals("Wrong result", expected, actual);
    }

    /**
     * Tests the <code>getUncachedDatasetEntry</code> with a 404 response.
     */
    @Test
    public void getUncachedDatasetEntry_NoData() {
        mockServerRequest(URL + "/entry/uncached/" + DATA_SET, withStatus(HttpStatus.NOT_FOUND));

        try {
            dataProvider.getUncachedDatasetEntry(DATA_SET);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertTrue("Wrong error message", ex.getMessage().contains("No more data"));
            assertTrue("Wrong error message", ex.getMessage().contains(DATA_SET));
        }
    }

    /**
     * Tests the <code>getUncachedDatasetEntry</code> with a 500 response.
     */
    @Test
    public void getUncachedDatasetEntry_Error() {
        mockServerRequest(URL + "/entry/uncached/" + DATA_SET, withServerError().body("{ \"error\": \"Oops\" }"));

        try {
            dataProvider.getUncachedDatasetEntry(DATA_SET);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertTrue("Wrong error message", ex.getMessage().contains("Oops"));
        }
    }

    /**
     * Tests the <code>getUncachedDataset</code> with a valid populated response.
     */
    @Test
    public void getUncachedDataset_HappyPath() {
        List<String> entry1 = new ArrayList<>();
        entry1.add("54321");
        entry1.add("XYZ");
        entry1.add("01-12-2017");

        List<String> entry2 = new ArrayList<>();
        entry2.add("67890");
        entry2.add("abc");
        entry2.add(null);

        List<List<String>> expected = new ArrayList<>();
        expected.add(entry1);
        expected.add(entry2);

        mockServerRequest(URL + "/dataset/uncached/" + DATA_SET + "/10",
                withSuccess("[ [ \"54321\", \"XYZ\", \"01-12-2017\" ],[ \"67890\", \"abc\", null ] ]",
                        MediaType.APPLICATION_JSON_UTF8));

        List<List<String>> actual = dataProvider.getUncachedDataset(DATA_SET, 10);
        assertEquals("Wrong result", expected, actual);
    }

    /**
     * Tests the <code>getUncachedDataset</code> with a 404 response.
     */
    @Test
    public void getUncachedDataset_NoData() {
        mockServerRequest(URL + "/dataset/uncached/" + DATA_SET + "/10", withStatus(HttpStatus.NOT_FOUND));

        try {
            dataProvider.getUncachedDataset(DATA_SET, 10);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertTrue("Wrong error message", ex.getMessage().contains("No more data"));
            assertTrue("Wrong error message", ex.getMessage().contains(DATA_SET));
        }
    }

    /**
     * Tests the <code>getUncachedDataset</code> with a 500 response.
     */
    @Test
    public void getUncachedDataset_Error() {
        mockServerRequest(URL + "/dataset/uncached/" + DATA_SET + "/10",
                withServerError().body("{ \"error\": \"Oops\" }"));

        try {
            dataProvider.getUncachedDataset(DATA_SET, 10);
            fail("Expected exception to be thrown");

        } catch (IllegalStateException ex) {
            assertTrue("Wrong error message", ex.getMessage().contains("Oops"));
        }
    }

    /**
     * Set the mock REST server to expect a GET request with the specified URL, and to return the specified response.
     * @param url               The URL to expect
     * @param responseCreator   Used to create the response to return
     */
    private void mockServerRequest(String url, ResponseCreator responseCreator) {
        server.expect(requestTo(url)).andExpect(method(HttpMethod.GET)).andRespond(responseCreator);
    }
}
