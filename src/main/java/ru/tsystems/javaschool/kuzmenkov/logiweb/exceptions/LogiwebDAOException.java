package ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions;

/**
 * Created by Nikolay on 23.11.2015.
 */
public class LogiwebDAOException extends Exception {

    public LogiwebDAOException() {
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
