package uk.gov.dvsa.mot.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.gov.dvsa.mot.utils.config.TestsuiteConfig;

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
        /*
         * Just in case the data server wasn't successfully shutdown last time the testsuite was run, check here
         * if it responds and if so shut it down before running the new version up
         */
        if (checkIfServerAlreadyRunning()) {
            stopServer();
        }

        try {
            String classpath = calculateServerClasspath("./build/server-libs");
            logger.info("Using classpath {}", classpath);

            String config = System.getProperty("configuration");

            TestsuiteConfig env;

            if (config != null) {
                env = TestsuiteConfig.loadConfigFromString(config);
            } else {
                env = TestsuiteConfig.loadConfig("testsuite");
            }

            // Get the address and port from the config and use it to run the server.
            // This allows to assign these dynamically - for example when running multiple tests on a single machine.
            // Default address and port are localhost and 9999.
            String address = env.getProperty("dataserverUrl", "localhost")
                    .replace("https:", "").replace("http:", "")
                    .replace("/", "")
                    .split(":")[0];
            String port =  env.getProperty("dataserverUrl", "9999")
                    .replace("https:", "").replace("http:", "")
                    .replace("/", "")
                    .split(":")[1];

            // start the server
            serverProcess = new ProcessBuilder(
                    "java",
                    "-Dserver.address=" + address, "-Dserver.port=" + port,
                    config != null ? "-Dconfiguration=" + config : "",
                    "-cp", classpath, "uk.gov.dvsa.mot.server.ServerApplication").start();
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
     * Stops the data server, gracefully if possible (using the /shutdown endpoint) otherwise forcibly.
     */
    public static void stopServer() {
        logger.info("In DataServerManager.stopServer");

        RestTemplate restTemplate = new RestTemplate();

        String config = System.getProperty("configuration");

        TestsuiteConfig env;

        if (config != null) {
            env = TestsuiteConfig.loadConfigFromString(config);
        } else {
            env = TestsuiteConfig.loadConfig("testsuite");
        }

        try {
            // POST to Spring Boot shutdown handler to trigger a clean shutdown...
            // (it returns a short JSON reply message)
            String result = restTemplate.postForObject(
                    env.getProperty("dataserverUrl") + "/shutdown", null, String.class);
            logger.info("Server shutdown triggered, received {}", result);

        } catch (RestClientException ex) {
            String message = "Failed to post server shutdown message: " + ex.getMessage();
            logger.error(message, ex);

            if (serverProcess != null) {
                // attempt to kill the server process as a last resort...
                // not possible if using Courgette runner (as start and stop is in different processes)
                serverProcess.destroy();
                logger.info("Server process killed");
            }
        }
    }

    /**
     * Checks if the data server is already running, by calling the /health endpoint.
     * @return <code>true</code> if running
     */
    private static boolean checkIfServerAlreadyRunning() {
        logger.info("In DataServerManager.checkIfServerAlreadyRunning");

        String config = System.getProperty("configuration");

        TestsuiteConfig env;

        if (config != null) {
            env = TestsuiteConfig.loadConfigFromString(config);
        } else {
            env = TestsuiteConfig.loadConfig("testsuite");
        }

        try {
            // issue a GET to Spring Boot health endpoint to determine if data server is already running...
            // (it returns a short JSON reply message)
            RestTemplate restTemplate = new RestTemplate();
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(500); // set short connect timeout
            requestFactory.setReadTimeout(2000); // set slightly longer read timeout
            restTemplate.setRequestFactory(requestFactory);
            String result = restTemplate.getForObject(env.getProperty("dataserverUrl") + "/health", String.class);
            logger.info("Server is already running, received {}", result);
            return true;

        } catch (RestClientException ex) {
            logger.info("Server not already running");
            return false;
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
