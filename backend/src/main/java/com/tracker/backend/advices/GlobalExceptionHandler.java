package com.tracker.backend.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.tracker.backend.exceptions.ServiceException;
import com.tracker.backend.models.CustomErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Validation error handling
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(WebExchangeBindException ex) {
        CustomErrorResponse error = new CustomErrorResponse();
        StringBuilder message = new StringBuilder();
        int n = ex.getBindingResult().getFieldErrors().size();
        for(int i = 0; i < n; i++) {
            FieldError fieldError = ex.getBindingResult().getFieldErrors().get(i);
            message.append(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            if(i != n - 1) {
                message.append(", ");
            }
        }
        error.setMessage(message.toString());
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(ServiceException ex) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setMessage(ex.getMessage());
        error.setStatusCode(ex.getStatus().value());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setMessage(ex.getMessage());
        error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
