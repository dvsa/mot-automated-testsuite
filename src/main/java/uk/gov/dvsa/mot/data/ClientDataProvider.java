package uk.gov.dvsa.mot.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Handles provision of test data, by operating as a HTTP client calling a data server that has a REST API.
 */
public class ClientDataProvider implements DataProvider {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(ClientDataProvider.class);

    /** The rest client to use. */
    private final RestTemplate restTemplate;

    /** The base URL of the data server. */
    private final String dataserverUrl;

    /**
     * Creates a new instance.
     */
    public ClientDataProvider(Environment env, RestTemplate restTemplate) {
        logger.debug("Creating ClientDataProvider...");
        this.dataserverUrl = env.getRequiredProperty("dataserverUrl");
        this.restTemplate = restTemplate;
    }

    /**
     * Get a cached entry from the specified test data set.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    @Override
    public List<String> getCachedDatasetEntry(String dataSetName) {
        logger.info("Querying Data Server for cached dataset entry, for dataset {}", dataSetName);
        restTemplate.setErrorHandler(new ErrorHandler(dataSetName));

        @SuppressWarnings("unchecked")
        List<String> entry = (List<String>) (restTemplate.getForObject(
                dataserverUrl + "/entry/cached/{dataSetName}", List.class, dataSetName));

        return entry;
    }

    /**
     * Get the first entry from the specified test data set, by executing the query immediately, ignoring any caches.
     * @param dataSetName   The data set name
     * @return The data entry
     */
    @Override
    public List<String> getUncachedDatasetEntry(String dataSetName) {
        logger.info("Querying Data Server for uncached dataset entry, for dataset {}", dataSetName);
        restTemplate.setErrorHandler(new ErrorHandler(dataSetName));

        @SuppressWarnings("unchecked")
        List<String> entry = (List<String>) (restTemplate.getForObject(
                dataserverUrl + "/entry/uncached/{dataSetName}", List.class, dataSetName));

        return entry;
    }

    /**
     * Get the specified number of entries from the specified test data set, by executing the query immediately,
     * ignoring any caches.
     * @param dataSetName   The data set name
     * @param length        The number of data set entries
     * @return The data entries
     */
    @Override
    public List<List<String>> getUncachedDataset(String dataSetName, int length) {
        logger.info("Querying Data Server for uncached dataset {}, length {}", dataSetName, length);
        restTemplate.setErrorHandler(new ErrorHandler(dataSetName));

        @SuppressWarnings("unchecked")
        List<List<String>> entry = (List<List<String>>) (restTemplate.getForObject(
                dataserverUrl + "/dataset/uncached/{dataSetName}/{length}", List.class, dataSetName, length));

        return entry;
    }

    /**
     * Handles a response error.
     */
    private static class ErrorHandler implements ResponseErrorHandler {

        /** The dataset name. */
        private final String datasetName;

        /**
         * Creates a new instance.
         * @param datasetName   The dataset name
         */
        ErrorHandler(String datasetName) {
            this.datasetName = datasetName;
        }

        /**
         * Decide whether we have an error or not.
         * @param response  The response to check
         * @return <code>true</code> if response is an error
         * @throws IOException  Error reading status code
         */
        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode() != HttpStatus.OK;
        }

        /**
         * Handles an error.
         * @param response                  The response
         * @throws IOException              Error reading the response code
         * @throws IllegalStateException    The error
         */
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                String message = "No more data for dataset: " + datasetName;
                logger.error(message);
                throw new IllegalStateException(message);
            } else {
                // if 500 error then response contains error JSON object rather than List of data
                String errorMessage = readResponseBodyAsString(response.getBody());
                String message = "Error querying Data Server for cached dataset entry for dataset:" + datasetName
                        + ", " + errorMessage;
                logger.error(message);
                throw new IllegalStateException(message);
            }
        }

        /**
         * Reads response body as a string.
         * @param bodyInputStream   The input stream to read from
         * @return The string
         */
        private String readResponseBodyAsString(InputStream bodyInputStream) {
            try {
                ByteArrayOutputStream resultBytes = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = bodyInputStream.read(buffer)) != -1) {
                    resultBytes.write(buffer, 0, length);
                }

                return resultBytes.toString("UTF-8");

            } catch (IOException ex) {
                String message = "Error reading from error response body: " + ex.getMessage();
                logger.error(message, ex);
                throw new IllegalStateException(message, ex);
            }
        }
    }
}
