package org.knpkid.kms.controller;

import jakarta.validation.ConstraintViolationException;
import org.knpkid.kms.model.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<Object>> responseStatusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(new WebResponse<>(null, exception.getReason(), null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<Object>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new WebResponse<>(null, exception.getMessage(), null));
    }

}
