package com.flybook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FlybookException extends RuntimeException {

    public FlybookException(String message) {
        super(message);
    }

    public FlybookException(String message, Throwable cause) {
        super(message, cause);
    }
}