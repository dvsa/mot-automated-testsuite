package uk.gov.dvsa.mot.browserstack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopBrowserStackLocal {

    private static final Logger logger = LoggerFactory.getLogger(StartBrowserStackLocal.class);

    public static void main(String[] args) {
        logger.debug("Starting local BrowserStack instance...");
        BrowserStackManager.stop();
    }
}
