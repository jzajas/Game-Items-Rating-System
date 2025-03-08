package com.jzajas.RatingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException() {}

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(Long id) {
        super(String.valueOf(id));
    }

    public CommentNotFoundException(Throwable tr) {
        super(tr);
    }

    public CommentNotFoundException(String message, Throwable tr) {
        super(message, tr);
    }
}
