package ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions;

/**
 * @author Nikolay Kuzmenkov.
 */
public class LogiwebDAOException extends Exception {

    public LogiwebDAOException() {
        super();
    }

    public LogiwebDAOException(String message) {
        super(message);
    }

    public LogiwebDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogiwebDAOException(Throwable cause) {
        super(cause);
    }

    public LogiwebDAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
