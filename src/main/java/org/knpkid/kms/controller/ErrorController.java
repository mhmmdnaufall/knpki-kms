package org.knpkid.kms.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.knpkid.kms.model.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<Object>> responseStatusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(new WebResponse<>(null, exception.getReason(), null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public WebResponse<Object> constraintViolationException(ConstraintViolationException exception) {
        var violationMap = exception.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                        violation -> violation.getPropertyPath().toString(),
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));
        return new WebResponse<>(null, violationMap, null);
    }

}
