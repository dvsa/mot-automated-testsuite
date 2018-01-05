package uk.gov.dvsa.mot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java application to start the data server.
 */
public class StartDataServer {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(StartDataServer.class);

    /**
     * Starts the data server.
     */
    public static void main(String[] args) {
        logger.info("In StartDataServer.main");
        DataServerManager.startServer();
    }
}
