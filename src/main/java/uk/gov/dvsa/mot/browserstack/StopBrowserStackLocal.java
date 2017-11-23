package uk.gov.dvsa.mot.browserstack;

import com.browserstack.local.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class StopBrowserStackLocal {

    private static final Logger logger = LoggerFactory.getLogger(StartBrowserStackLocal.class);

    public static void main(String[] args) {
        logger.debug("Starting local BrowserStack instance...");
        BrowserStackManager.stop();
    }
}
