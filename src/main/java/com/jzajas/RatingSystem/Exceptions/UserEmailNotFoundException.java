package com.jzajas.RatingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException() {}

    public UserEmailNotFoundException(String message) {
        super(message);
    }

    public UserEmailNotFoundException(Throwable tr) {
        super(tr);
    }

    public UserEmailNotFoundException(String message, Throwable tr) {
        super(message, tr);
    }
}
