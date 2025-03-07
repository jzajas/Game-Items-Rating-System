package com.jzajas.RatingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameObjectNotFoundException extends RuntimeException {

    public GameObjectNotFoundException() {}

    public GameObjectNotFoundException(String message) {
        super(message);
    }

    public GameObjectNotFoundException(Throwable tr) {
        super(tr);
    }

    public GameObjectNotFoundException(String message, Throwable tr) {
        super(message, tr);
    }
}
