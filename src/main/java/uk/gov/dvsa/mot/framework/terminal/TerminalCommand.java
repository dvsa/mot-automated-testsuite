package uk.gov.dvsa.mot.framework.terminal;

import org.omg.CORBA.DynAnyPackage.Invalid;
import org.openqa.selenium.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TerminalCommand {

    /** Logger to use. */
    private static Logger logger = LoggerFactory.getLogger(TerminalCommand.class);

    /** Process used to execute the command. */
    private Process terminalProcess;

    /** Command to execute. */
    private String command;

    /** Command parameters. */
    private String[] parameters;

    /**
     * Constructor to create a new command.
     *
     * @param command to execute.
     * @param parameters to append to the command.
     */
    public TerminalCommand(String command, String... parameters) {
        if (command == null) {
            String errorMessage = "Command is equal to null. Must provide non-null command string.";
            logger.error(errorMessage);
            throw new InvalidArgumentException(errorMessage);
        }

        this.command = command;
        this.parameters = parameters;
    }

    /**
     * Execute the command.
     */
    public void start() {
        StringBuilder parameterString = new StringBuilder();

        for (String parameter : parameters) {
            parameterString.append(parameter).append(' ');
        }

        try {
            terminalProcess = new ProcessBuilder(command, parameterString.toString()).start();
        } catch (IOException io) {
            String errorMessage = "Unable to start terminal command. %s, with parameters %s.";
            logger.error(String.format(errorMessage, command, parameterString));
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Execute the command with callback methods.
     *
     * <p>Does not execute TerminalCommandCallback::doCallback(), just doFirst & doLast.</p>
     */
    public void start(TerminalCommandCallback callbackObject) {
        if (callbackObject == null) {
            String errorMessage = "Failed to execute callback as object passed is null.";
            logger.debug(errorMessage);
            throw new InvalidArgumentException(errorMessage);
        }

        start();
    }

    /**
     * Destroy the process.
     */
    public void stop() {
        terminalProcess.destroy();
    }

    /**
     * Provides a way to shut down processes gracefully, by executing a callback method first, allowing for cleanup.
     *
     * @param callbackObject function to execute before destroying the process.
     */
    public void stop(TerminalCommandCallback callbackObject) {
        if (callbackObject == null) {
            String errorMessage = "Failed to execute callback as object passed is null.";
            logger.debug(errorMessage);
            throw new InvalidArgumentException(errorMessage);
        }

        callbackObject.doCallback();
        stop();
    }
}
