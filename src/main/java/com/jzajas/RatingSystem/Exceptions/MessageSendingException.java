package com.jzajas.RatingSystem.Exceptions;

public class MessageSendingException extends RuntimeException {

    public MessageSendingException(String message) {
        super(message);
    }

    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}