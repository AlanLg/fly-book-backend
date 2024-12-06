package com.flybook.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FlybookException extends RuntimeException {
    private final HttpStatus httpStatus;

    public FlybookException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public FlybookException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

}
