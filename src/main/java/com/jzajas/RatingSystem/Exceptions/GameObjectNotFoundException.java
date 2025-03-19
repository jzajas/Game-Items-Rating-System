package com.jzajas.RatingSystem.Exceptions;

public class GameObjectNotFoundException extends RuntimeException {

    public GameObjectNotFoundException(String message) {
        super(message);
    }

    public GameObjectNotFoundException(Long id) {
        super(String.valueOf(id));
    }

}
