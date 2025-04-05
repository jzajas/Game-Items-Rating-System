package com.jzajas.RatingSystem.Exceptions;

public class InvalidRatingValueException extends RuntimeException {

    public InvalidRatingValueException(int rating) {
        super(String.valueOf(rating));
    }
}
