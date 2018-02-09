package uk.gov.dvsa.mot.framework;


import static com.jayway.restassured.RestAssured.with;
import static com.jayway.restassured.config.HttpClientConfig.httpClientConfig;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvsa.mot.framework.document.Document;
import uk.gov.dvsa.mot.framework.document.Document.IDocument;
import uk.gov.dvsa.mot.framework.document.Document.Type;
import uk.gov.dvsa.mot.framework.document.DocumentException;
import uk.gov.dvsa.mot.framework.document.csv.CsvDocument;
import uk.gov.dvsa.mot.framework.document.csv.CsvException;
import uk.gov.dvsa.mot.framework.document.xml.XmlDocument;
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
    public IDocument getDocument(String url, Type documentType) throws Exception {
        Cookie session = getCookie(DEFAULT_SESSION_COOKIE_NAME);
        Cookie token = getCookie(DEFAULT_TOKEN_COOKIE_NAME);

        Response serverResponse = with()
                .cookie(session.getName(), session.getValue())
                .cookie(token.getName(), token.getValue())
                .get(url);

        String rawDocument = serverResponse.asString();

        return Document.getDocument(rawDocument, documentType);
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
                File dir = new File("target/documents/");
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

    /**
     * Get document results.
     * @return test results for the current session
     */
    public String getTestResults() {
        String documentResults = "";

        try {
            URL url = new URL(env.getProperty("dataserverUrl"));
            Response serverResponse = with().get(url + "/moth/results");

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
    public void sendTestResult(String result) {
        try {
            URL url = new URL(env.getProperty("dataserverUrl") + "/moth/results");

            Response serverResponse = with().body(result).post(url);

            serverResponse.asInputStream().close();
        } catch (Exception ex) {
            logger.error("Error sending document result", ex);
        }
    }
}
