package uk.gov.dvsa.mot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java application to stop the data server.
 */
public class StopDataServer {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(StopDataServer.class);

    /**
     * Stops the data server.
     */
    public static void main(String[] args) {
        System.out.println("Stopping data server.");
        logger.info("In StopDataServer.main");
        DataServerManager.stopServer();
    }
}
