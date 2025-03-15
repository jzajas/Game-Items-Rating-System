package com.jzajas.RatingSystem.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidReceiverException extends RuntimeException{

    public InvalidReceiverException(Long id) {
        super(String.valueOf(id));
    }
}
