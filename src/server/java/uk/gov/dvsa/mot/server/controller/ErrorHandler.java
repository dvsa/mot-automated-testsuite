package uk.gov.dvsa.mot.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.gov.dvsa.mot.server.model.ErrorBean;

/**
 * Common error handling for all REST controllers.
 */
@ControllerAdvice(basePackages = "uk.gov.dvsa.mot.server.controller")
public class ErrorHandler extends ResponseEntityExceptionHandler {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * Handles any <code>IllegalStateException</code>s thrown from a controller as a 404 response.
     * @param ex    The exception thrown
     * @return The response to use
     */
    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<?> notFoundErrors(IllegalStateException ex) {
        logger.debug("In handleIllegalStateException");

        logger.error("Returning not found", ex);
        return ResponseEntity.notFound().build();
    }

    /**
     * Handles any other <code>Exception</code>s thrown from a controller as a 500 response.
     *
     * @param ex The exception thrown
     * @return The response to use
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> allOtherErrors(Exception ex) {
        logger.debug("In handleException");

        logger.error("Returning internal server error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorBean(ex.getMessage()));
    }
}