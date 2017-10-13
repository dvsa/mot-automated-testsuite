package uk.gov.dvsa.mot.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    /**
     * The server startup point.
     * @param args   Command line arguments
     */
    public static void main(String[] args) {
        new SpringApplication(ServerApplication.class).run(args);

        logger.debug("Automated Testsuite Server started");
    }
}
