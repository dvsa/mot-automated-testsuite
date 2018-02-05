package uk.gov.dvsa.mot.server.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.dvsa.mot.server.data.DatabaseDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests the <code>DataController</code> class.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DataController.class)
public class DataControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DatabaseDataProvider dataProvider;

    /**
     * Tests the GET /data/dataset/uncached endpoint, with a successful scenario.
     * @throws Exception    Test failed
     */
    @Test
    public void getUncachedDataset_HappyPath() throws Exception {

        List<String> entry1 = new ArrayList<>();
        entry1.add("1");
        entry1.add("aaa");
        entry1.add("");

        List<String> entry2 = new ArrayList<>();
        entry2.add("2");
        entry2.add("bbb");
        entry2.add(null);

        List<List<String>> dataset = new ArrayList<>();
        dataset.add(entry1);
        dataset.add(entry2);

        given(dataProvider.getUncachedDataset("TEST_DATASET", 5)).willReturn(dataset);

        // should return 200 response with JSON response
        mvc.perform(get("/data/dataset/uncached/TEST_DATASET/5").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("[[\"1\",\"aaa\",\"\"],[\"2\",\"bbb\",null]]"));
    }

    /**
     * Tests the GET /data/dataset/uncached endpoint, for an unknown dataset.
     * @throws Exception    Test failed
     */
    @Test
    public void getUncachedDataset_UnknownDataset() throws Exception {

        given(dataProvider.getUncachedDataset("NO_SUCH_DATASET", 10))
                .willThrow(new IllegalStateException("No such dataset!"));

        // should return 404 response with no response body
        mvc.perform(get("/data/dataset/uncached/NO_SUCH_DATASET/10").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the GET /data/dataset/uncached endpoint, for an other error.
     * @throws Exception    Test failed
     */
    @Test
    public void getUncachedDataset_OtherError() throws Exception {

        given(dataProvider.getUncachedDataset("ERROR_DATASET", 2))
                .willThrow(new DataSourceLookupFailureException("Fatal error!")); // example Spring runtime exception

        // should return 500 response with JSON respponse
        mvc.perform(get("/data/dataset/uncached/ERROR_DATASET/2").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"message\":\"Fatal error!\"}"));
    }

    /**
     * Tests the GET /data/entry/uncached endpoint, with a successful scenario.
     * @throws Exception    Test failed
     */
    @Test
    public void getUncachedDatasetEntry_HappyPath() throws Exception {

        List<String> entry = new ArrayList<>();
        entry.add("2");
        entry.add("bbb");
        entry.add(null);

        given(dataProvider.getUncachedDatasetEntry("TEST_DATASET")).willReturn(entry);

        // should return 200 response with JSON response
        mvc.perform(get("/data/entry/uncached/TEST_DATASET").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("[\"2\",\"bbb\",null]"));
    }

    /**
     * Tests the GET /data/entry/uncached endpoint, for an unknown dataset.
     * @throws Exception    Test failed
     */
    @Test
    public void getUncachedDatasetEntry_UnknownDataset() throws Exception {

        given(dataProvider.getUncachedDatasetEntry("NO_SUCH_DATASET"))
                .willThrow(new IllegalStateException("No such dataset!"));

        // should return 404 response with no response body
        mvc.perform(get("/data/entry/uncached/NO_SUCH_DATASET").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the GET /data/entry/uncached endpoint, for an other error.
     * @throws Exception    Test failed
     */
    @Test
    public void getUncachedDatasetEntry_OtherError() throws Exception {

        given(dataProvider.getUncachedDatasetEntry("ERROR_DATASET"))
                .willThrow(new DataSourceLookupFailureException("Fatal error!")); // example Spring runtime exception

        // should return 500 response with JSON respponse
        mvc.perform(get("/data/entry/uncached/ERROR_DATASET").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"message\":\"Fatal error!\"}"));
    }

    /**
     * Tests the GET /data/entry/cached endpoint, with a successful scenario.
     * @throws Exception    Test failed
     */
    @Test
    public void getCachedDatasetEntry_HappyPath() throws Exception {

        List<String> entry = new ArrayList<>();
        entry.add("2");
        entry.add("bbb");
        entry.add(null);

        given(dataProvider.getCachedDatasetEntry("TEST_DATASET")).willReturn(entry);

        // should return 200 response with JSON response
        mvc.perform(get("/data/entry/cached/TEST_DATASET").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("[\"2\",\"bbb\",null]"));
    }

    /**
     * Tests the GET /data/entry/cached endpoint, for an unknown dataset.
     * @throws Exception    Test failed
     */
    @Test
    public void getCachedDatasetEntry_UnknownDataset() throws Exception {

        given(dataProvider.getCachedDatasetEntry("NO_SUCH_DATASET"))
                .willThrow(new IllegalStateException("No such dataset!"));

        // should return 404 response with no response body
        mvc.perform(get("/data/entry/cached/NO_SUCH_DATASET").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the GET /data/entry/cached endpoint, for an other error.
     * @throws Exception    Test failed
     */
    @Test
    public void getCachedDatasetEntry_OtherError() throws Exception {

        given(dataProvider.getCachedDatasetEntry("ERROR_DATASET"))
                .willThrow(new DataSourceLookupFailureException("Fatal error!")); // example Spring runtime exception

        // should return 500 response with JSON respponse
        mvc.perform(get("/data/entry/cached/ERROR_DATASET").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("{\"message\":\"Fatal error!\"}"));
    }
}
