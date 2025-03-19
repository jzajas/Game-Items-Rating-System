package com.jzajas.RatingSystem.Exceptions;


public class InvalidReceiverException extends RuntimeException {

    public InvalidReceiverException(Long id) {
        super(String.valueOf(id));
    }
}
