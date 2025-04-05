package com.jzajas.RatingSystem.Exceptions;

public class GameObjectNotFoundException extends RuntimeException {

    public GameObjectNotFoundException(Long id) {
        super(String.valueOf(id));
    }

}
