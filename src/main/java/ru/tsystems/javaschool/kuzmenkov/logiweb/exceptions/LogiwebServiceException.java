package ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions;

/**
 * @author Nikolay Kuzmenkov.
 */
public class LogiwebServiceException extends Exception {

    public LogiwebServiceException() {
        super();
    }

    public LogiwebServiceException(String message) {
        super(message);
    }

    public LogiwebServiceException(Throwable cause) {
        super(cause);
    }

    public LogiwebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogiwebServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
