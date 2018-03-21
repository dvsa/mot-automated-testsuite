package uk.gov.dvsa.mot.framework;


import static com.jayway.restassured.RestAssured.with;
import static com.jayway.restassured.config.HttpClientConfig.httpClientConfig;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.joda.time.DateTime;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.csv.CsvDocument;
import uk.gov.dvsa.mot.framework.csv.CsvException;
import uk.gov.dvsa.mot.utils.config.TestsuiteConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RequestHandler {

    /** These are the default cookie names for authentication. **/
    private static final String DEFAULT_TOKEN_COOKIE_NAME = "iPlanetDirectoryPro";
    private static final String DEFAULT_SESSION_COOKIE_NAME = "PHPSESSID";

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    /** The web driver to use. */
    private WebDriver webDriver;

    /** The configuration settings to use. */
    private TestsuiteConfig env;

    private boolean saveDocuments;

    private String timestamp;

    /**
     * Creates new RequestHandler instance.
     * @param env config to use
     */
    public RequestHandler(TestsuiteConfig env) {
        this.env = env;

        RestAssured.useRelaxedHTTPSValidation();
        int timeout = 60000;

        RestAssured.config()
                .httpClient(httpClientConfig()
                        .setParam(ClientPNames.CONN_MANAGER_TIMEOUT, timeout)
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
    }

    /**
     * Creates new RequestHandler instance.
     * @param webDriver Web driver to use for retrieving the cookies.
     */
    public RequestHandler(WebDriver webDriver, TestsuiteConfig env) {
        this(env);
        this.webDriver = webDriver;
        this.env = env;
        saveDocuments = Boolean.parseBoolean(env.getProperty("saveDocuments", "false"));

        timestamp = getTimestamp();
    }

    /**
     * Get cookie from local storage.
     * @param name of the cookie to get.
     * @return the cookie object.
     */
    private Cookie getCookie(String name) {
        return webDriver.manage().getCookieNamed(name);
    }

    /**
     * Get PDF document.
     * @param url of the file to load.
     * @return loaded PDF document.
     */
    public PDDocument getPdfDocument(String url) throws IOException {
        Cookie session = getCookie(DEFAULT_SESSION_COOKIE_NAME);
        Cookie token = getCookie(DEFAULT_TOKEN_COOKIE_NAME);

        Response serverResponse = with()
                .cookie(session.getName(), session.getValue())
                .cookie(token.getName(), token.getValue())
                .get(url);

        PDDocument pdDocument = PDDocument.load(serverResponse.asInputStream());
        serverResponse.asInputStream().close();

        return pdDocument;
    }

    /**
     * Get CSV document.
     * @param url of the file to load.
     * @return csv document as a string.
     */
    public CsvDocument getCsvDocument(String url) throws CsvException {
        Cookie session = getCookie(DEFAULT_SESSION_COOKIE_NAME);
        Cookie token = getCookie(DEFAULT_TOKEN_COOKIE_NAME);

        Response serverResponse = with()
                .cookie(session.getName(), session.getValue())
                .cookie(token.getName(), token.getValue())
                .get(url);

        String document = new String(serverResponse.asByteArray());
        try {

            return new CsvDocument(CSVParser.parse(document, CSVFormat.DEFAULT));
        } catch (IOException ex) {
            throw new CsvException("Error parsing CSV file", ex);
        }
    }

    /**
     * Used to save a copy of the document for auditing and verification purposes.
     * @param fileUrl       The URL of the file to save
     */
    public String writeFile(String filename, String fileUrl) {
        // Check whether we should save documents
        if (this.saveDocuments) {

            Cookie session = getCookie(DEFAULT_SESSION_COOKIE_NAME);
            Cookie token = getCookie(DEFAULT_TOKEN_COOKIE_NAME);
            try {
                File dir = new File("target/documents/" + timestamp);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File file = new File(dir + File.separator + filename);
                file.createNewFile();

                URL url = new URL(fileUrl);
                Response serverResponse = with()
                        .cookie(session.getName(), session.getValue())
                        .cookie(token.getName(), token.getValue())
                        .get(url);

                Files.copy(serverResponse.asInputStream(), Paths.get(file.getPath()),
                        StandardCopyOption.REPLACE_EXISTING);
                serverResponse.asInputStream().close();
            } catch (Exception ex) {
                logger.error("Error saving document", ex);
            }

            return filename;
        }

        return null;
    }

    /**
     * Get the timestamp from the server.
     * @return the timestamp as a string
     */
    public String getTimestamp() {
        if (timestamp == null || timestamp == "") {
            try {
                URL url = new URL(env.getProperty("dataserverUrl"));
                Response serverResponse = with().get(url + "/timestamp");

                timestamp = serverResponse.asString();

                serverResponse.asInputStream().close();
            } catch (Exception ex) {
                timestamp = DateTime.now().toString("dd-MM-yyyy_HH-mm-ss");
                logger.error("Error getting timestamp ", ex);
            }
        }

        return timestamp;
    }

    /**
     * Get document results.
     * @return document results for the current session
     */
    public String getDocumentResults() {
        String documentResults = "";

        try {
            URL url = new URL(env.getProperty("dataserverUrl"));
            Response serverResponse = with().get(url + "/documents/results");

            documentResults = serverResponse.asString();

            serverResponse.asInputStream().close();
        } catch (Exception ex) {
            logger.error("Error getting timestamp ", ex);
        }

        return documentResults;
    }

    /**
     * Send result/s to the server.
     * @param result to send
     */
    public void sendDocumentResult(String result) {
        try {
            URL url = new URL(env.getProperty("dataserverUrl") + "/documents/results");

            Response serverResponse = with().body(result).post(url);

            serverResponse.asInputStream().close();
        } catch (Exception ex) {
            logger.error("Error sending document result", ex);
        }
    }
}
