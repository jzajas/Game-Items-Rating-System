package com.jzajas.RatingSystem.Exceptions;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(Long id) {
        super(String.valueOf(id));
    }
}
