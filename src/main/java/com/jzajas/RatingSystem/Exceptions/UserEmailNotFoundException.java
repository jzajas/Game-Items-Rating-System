package com.jzajas.RatingSystem.Exceptions;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
