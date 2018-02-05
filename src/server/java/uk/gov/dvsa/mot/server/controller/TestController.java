package uk.gov.dvsa.mot.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController()
public class TestController {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    private Map<String, String> testResults = new HashMap<>();

    /**
     * Add a test result.
     * @param result to add.
     * @return confirmation
     */
    @ResponseBody
    @RequestMapping(value = "/moth/results", method = POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addTestResults(@RequestBody String result) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(result));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                testResults.put(line.split("=")[0], line.split("=")[1]);
            }
        } catch (IOException io) {
            logger.error(String.format("Failed to add test results: %s", result));
        }

        return ResponseEntity.ok(String.format("Added document result: %s.", result));
    }

    /**
     * Get all mot test results.
     * @return mot results.
     */
    @ResponseBody
    @RequestMapping(value = "/moth/results", method = GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTestResults() {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : testResults.keySet()) {
            stringBuilder.append(key).append("=").append(testResults.get(key)).append("\n");
        }

        return ResponseEntity.ok(stringBuilder.toString());
    }
}

