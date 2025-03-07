package com.jzajas.RatingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String message) {
        super(message);
    }

    public EmailAlreadyInUseException(Throwable tr) {
        super(tr);
    }

    public EmailAlreadyInUseException(String message, Throwable tr) {
        super(message, tr);
    }
}
