package uk.gov.dvsa.mot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * Provides static methods for the various runners to control the data server.
 */
public class DataServerManager {

    /** The logger to use. */
    private static final Logger logger = LoggerFactory.getLogger(DataServerManager.class);


    /** The server process. */
    private static Process serverProcess;

    /**
     * Starts the data server.
     */
    public static void startServer() {
        logger.info("In DataServerManager.startServer");

        try {
            String classpath = calculateServerClasspath("./build/server-libs");
            logger.info("Using classpath {}", classpath);

            // start the server
            serverProcess = new ProcessBuilder(
                    "java", "-cp", classpath, "uk.gov.dvsa.mot.server.ServerApplication").start();
            logger.info("Started server process, waiting for it to startup");

            // wait 5 seconds for the server to start up
            Thread.sleep(5000L);

            logger.info("Wait completed, starting testing...");

        } catch (IOException ex) {
            String message = "Failed to start server: " + ex.getMessage();
            logger.error(message, ex);
            throw new IllegalStateException(message, ex);

        } catch (InterruptedException ex) {
            String message = "Error waiting for server to start: " + ex.getMessage();
            logger.error(message, ex);
            throw new IllegalStateException(message, ex);
        }

    }

    /**
     * Stops the data server, gracefully if possible otherwise forcibly.
     */
    public static void stopServer() {
        logger.info("In DataServerManager.stopServer");

        try {
            if (serverProcess != null) {
                // POST to Spring Boot shutdown handler to trigger a clean shutdown...
                // (it returns a short JSON reply message)
                RestTemplate restTemplate = new RestTemplate();
                String result = restTemplate.postForObject(
                        "http://localhost:9999/shutdown", null, String.class);
                logger.info("Server shutdown triggered, received {}", result);
            }
        } catch (RestClientException ex) {
            String message = "Failed to post server shutdown message: " + ex.getMessage();
            logger.error(message, ex);

            // attempt to kill the server process as a last resort...
            serverProcess.destroy();
            logger.info("Server proess killed");
        }
    }


    /**
     * Calculates the server classpath, which is a colon-delimited string of directories and .jar files.
     * @param baseLibDir    The base directory of server libs and classes
     * @return The classpath
     */
    private static String calculateServerClasspath(String baseLibDir) {
        try {
            Path baseLibPath = Paths.get(baseLibDir).toAbsolutePath().normalize();
            Stream<Path> jars = Files.find(baseLibPath, 1, (path, attrs) -> path.toString().endsWith(".jar"));
            StringJoiner joiner = new StringJoiner(":");

            // server libs directory (containing the server Java classes)
            joiner.add(baseLibPath.toString());

            // all .jar files in the same directory
            jars.forEach((path) -> joiner.add(path.toString()));

            return joiner.toString();

        } catch (IOException ex) {
            String message = "Error resolving server base lib dir: " + ex.getMessage();
            logger.error(message, ex);
            throw new IllegalArgumentException(message, ex);
        }
    }
}
