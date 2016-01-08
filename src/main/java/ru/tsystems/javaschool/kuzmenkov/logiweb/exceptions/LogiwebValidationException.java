package ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions;

/**
 * @author Nikolay Kuzmenkov.
 */
public class LogiwebValidationException extends LogiwebServiceException {

    public LogiwebValidationException() {
        super();
    }

    public LogiwebValidationException(String message) {
        super(message);
    }

    public LogiwebValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogiwebValidationException(Throwable cause) {
        super(cause);
    }

    public LogiwebValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
