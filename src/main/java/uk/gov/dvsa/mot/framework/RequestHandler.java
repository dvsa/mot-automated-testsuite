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
import uk.gov.dvsa.mot.framework.csv.CsvDocument;
import uk.gov.dvsa.mot.framework.csv.CsvException;

import java.io.IOException;

public class RequestHandler {

    /** These are the default cookie names for authentication. **/
    private static final String DEFAULT_TOKEN_COOKIE_NAME = "iPlanetDirectoryPro";
    private static final String DEFAULT_SESSION_COOKIE_NAME = "PHPSESSID";

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    /** The web driver to use. */
    private final WebDriver webDriver;

    /**
     * Creates new RequestHandler instance.
     * @param webDriver Web driver to use for retrieving the cookies.
     */
    public RequestHandler(WebDriver webDriver) {
        this.webDriver = webDriver;

        RestAssured.useRelaxedHTTPSValidation();
        int timeout = 60000;

        //TODO: Check for a recommended way to create a config.
        RestAssured.config()
                .httpClient(httpClientConfig()
                        .setParam(ClientPNames.CONN_MANAGER_TIMEOUT, timeout)
                        .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
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

        Response serverRespone = with()
                .cookie(session.getName(), session.getValue())
                .cookie(token.getName(), token.getValue())
                .get(url);

        return PDDocument.load(serverRespone.asInputStream());
    }

    /**
     * Get CSV document.
     * @param url of the file to load.
     * @return csv document as a string.
     */
    public CsvDocument getCsvDocument(String url) throws IOException {
        Cookie session = getCookie(DEFAULT_SESSION_COOKIE_NAME);
        Cookie token = getCookie(DEFAULT_TOKEN_COOKIE_NAME);

        Response serverRespone = with()
                .cookie(session.getName(), session.getValue())
                .cookie(token.getName(), token.getValue())
                .get(url);

        String document = new String(serverRespone.asByteArray());
        
        try {
            return CsvDocument.load(document);
        } catch (CsvException failedToLoadCsv) {
            logger.debug(failedToLoadCsv.getMessage());
            return null;
        }
    }
}
