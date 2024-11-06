package com.tracker.backend.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;

    public ServiceException(String message) {
        super(message);
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ServiceException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }
}
