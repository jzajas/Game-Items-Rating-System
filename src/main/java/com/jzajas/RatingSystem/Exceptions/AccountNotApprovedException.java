package com.jzajas.RatingSystem.Exceptions;

import org.springframework.security.core.AuthenticationException;

public class AccountNotApprovedException extends AuthenticationException {

    public AccountNotApprovedException() {
        super("");
    }

    public AccountNotApprovedException(String message) {
        super(message);
    }
}
