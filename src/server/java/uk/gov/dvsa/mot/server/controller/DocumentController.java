package uk.gov.dvsa.mot.server.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dvsa.mot.server.reporting.DocumentListReportGenerator;

@RestController()
public class DocumentController {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

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
