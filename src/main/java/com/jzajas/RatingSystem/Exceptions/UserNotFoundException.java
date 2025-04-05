package com.jzajas.RatingSystem.Exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long id) {
        super(String.valueOf(id));
    }
}
