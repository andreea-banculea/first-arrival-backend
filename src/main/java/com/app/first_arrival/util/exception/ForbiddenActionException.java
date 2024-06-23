package com.app.first_arrival.util.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenActionException extends RuntimeException {
    private final HttpStatus status;

    public ForbiddenActionException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
