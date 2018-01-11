package uk.gov.dvsa.mot.server.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dvsa.mot.server.data.DatabaseDataProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

@RestController()
public class DataController {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    /** The data provider to delegate to. */
    @Inject
    private DatabaseDataProvider dataProvider;

    Set<String> clients = new HashSet<>();

    /**
     * Add a new client to the server application.
     *
     * @return confirmation that the action has been completed.
     */
    @ResponseBody
    @RequestMapping(value = "/testsuite/start", method = POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> startTestsuite(@RequestBody String clientLocalId) {
        clients.add(clientLocalId);

        return ResponseEntity.ok(String.format("Added %s.", clientLocalId));
    }

    /**
     * Remove a client to the server application.
     *
     * @return confirmation that the action has been completed.
     */
    @ResponseBody
    @RequestMapping(value = "/testsuite/stop", method = POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> stopTestsuite(@RequestBody String clientLocalId) {
        clients.remove(clientLocalId);

        return ResponseEntity.ok(String.format("Removed %s.", clientLocalId));
    }

    /**
     * Get the client count running on the application server.
     *
     * @return the count of clients running on thse server.
     */
    @RequestMapping(value = "/testsuite/count", method = GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Integer> testsuiteCount() {
        return ResponseEntity.ok(clients.size());
    }

    /**
     * Get an uncached dataset.
     * @return The dataset
     */
    @RequestMapping(value = "/dataset/uncached/{datasetName}/{length}", method = GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<List<String>>> getUncachedDataset(
            @PathVariable String datasetName, @PathVariable int length) {
        logger.info("getUncachedDataset: name {} length {}", datasetName, length);
        return ResponseEntity.ok(dataProvider.getUncachedDataset(datasetName, length));
    }

    /**
     * Get an entry from an uncached dataset.
     * @return The entry
     */
    @RequestMapping(value = "/entry/uncached/{datasetName}", method = GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<String>> getUncachedDatasetEntry(@PathVariable String datasetName) {
        logger.info("getUncachedDatasetEntry: name {}", datasetName);
        return ResponseEntity.ok(dataProvider.getUncachedDatasetEntry(datasetName));
    }

    /**
     * Get an entry from a cached dataset.
     * @return The entry
     */
    @RequestMapping(value = "/entry/cached/{datasetName}", method = GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<String>> getCachedDatasetEntry(@PathVariable String datasetName) {
        logger.info("getCachedDatasetEntry: name {}", datasetName);
        return ResponseEntity.ok(dataProvider.getCachedDatasetEntry(datasetName));
    }
}
