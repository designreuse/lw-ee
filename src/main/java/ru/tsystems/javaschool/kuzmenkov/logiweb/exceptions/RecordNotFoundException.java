package ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Throw if requested record not exist in data storage.
 *
 * @author Nikolay Kuzmenkov.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record not found.")
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException() {
        super();
    }
    public RecordNotFoundException(String message) {
        super(message);
    }
}
