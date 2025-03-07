package com.jzajas.RatingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRatingValueException extends RuntimeException{

    public InvalidRatingValueException() {}

    public InvalidRatingValueException(String message) {
        super(message);
    }

    public InvalidRatingValueException(Throwable tr) {
        super(tr);
    }

    public InvalidRatingValueException(String message, Throwable tr) {
        super(message, tr);
    }
}
