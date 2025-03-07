package com.jzajas.RatingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable tr) {
        super(tr);
    }

    public UserNotFoundException(String message, Throwable tr) {
        super(message, tr);
    }
}
