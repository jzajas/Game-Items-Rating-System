package com.jzajas.RatingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountNotApprovedException extends RuntimeException {

    public AccountNotApprovedException(String message) {
        super(message);
    }

    public AccountNotApprovedException(String message, Throwable tr) {
        super(message, tr);
    }
}
