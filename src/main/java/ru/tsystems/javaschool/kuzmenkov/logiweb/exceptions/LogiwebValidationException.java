package ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions;

/**
 * Created by Nikolay on 23.11.2015.
 */
public class LogiwebValidationException extends Exception {

    public LogiwebValidationException() {
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
