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
import uk.gov.dvsa.mot.server.reporting.DocumentListReportGenerator;

import java.util.List;
import javax.inject.Inject;

@RestController()
public class DataController {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    /** The data provider to delegate to. */
    @Inject
    private DatabaseDataProvider dataProvider;

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

    /**
     * Get the timestamp used by DocumentListReportGenerator.
     * @return timestamp
     */
    @RequestMapping(value = "/timestamp", method = GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTimestamp() {
        logger.info("getTimestamp");
        return ResponseEntity.ok(DocumentListReportGenerator.getTimestamp());
    }

    /**
     * Add a document result.
     * @param result to add.
     * @return confirmation
     */
    @ResponseBody
    @RequestMapping(value = "/documents/results", method = POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addDocumentResults(@RequestBody String result) {
        DocumentListReportGenerator.addDocumentResult(result);

        return ResponseEntity.ok(String.format("Added document result: %s.", result));
    }

    /**
     * Get all document results.
     * @return document results.
     */
    @ResponseBody
    @RequestMapping(value = "/documents/results", method = GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getDocumentResults() {
        return ResponseEntity.ok(DocumentListReportGenerator.getDocumentResults());
    }
}
