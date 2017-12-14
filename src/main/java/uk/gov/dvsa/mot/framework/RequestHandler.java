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
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import uk.gov.dvsa.mot.framework.csv.CsvDocument;
import uk.gov.dvsa.mot.framework.csv.CsvException;

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
    private final WebDriver webDriver;

    /** The configuration settings to use. */
    private final Environment env;

    private boolean saveDocuments;

    /**
     * Creates new RequestHandler instance.
     * @param webDriver Web driver to use for retrieving the cookies.
     */
    public RequestHandler(WebDriver webDriver, Environment env) {
        this.webDriver = webDriver;
        this.env = env;

        RestAssured.useRelaxedHTTPSValidation();
        int timeout = 60000;

        //TODO: Check for a recommended way to create a config.
        RestAssured.config()
                .httpClient(httpClientConfig()
                        .setParam(ClientPNames.CONN_MANAGER_TIMEOUT, timeout)
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));

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
    public PDDocument getPdfDocument(String url) throws IOException {
        Cookie session = getCookie(DEFAULT_SESSION_COOKIE_NAME);
        Cookie token = getCookie(DEFAULT_TOKEN_COOKIE_NAME);

        Response serverResponse = with()
                .cookie(session.getName(), session.getValue())
                .cookie(token.getName(), token.getValue())
                .get(url);

        String filename = url.replaceFirst("https://", "").replaceAll("/", "-") + ".pdf";
        writeFile(filename, url);

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
            String filename = url.replaceFirst("https://", "").replaceAll("/", "-") + ".csv";
            writeFile(filename, url);

            return new CsvDocument(CSVParser.parse(document, CSVFormat.DEFAULT));
        } catch (IOException ex) {
            throw new CsvException("Error parsing CSV file", ex);
        }
    }

    /**
     * Used to save a copy of the document for auditing and verification purposes.
     * @param filename      The filename to save the document as
     * @param fileUrl       The URL of the file to save
     */
    private void writeFile(String filename, String fileUrl) {
        // Check whether we should save documents
        if (this.saveDocuments) {
            Cookie session = getCookie(DEFAULT_SESSION_COOKIE_NAME);
            Cookie token = getCookie(DEFAULT_TOKEN_COOKIE_NAME);
            try {
                File dir = new File("target/documents");
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
        }
    }
}
