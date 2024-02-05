package org.knpkid.kms.controller;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.knpkid.kms.model.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErrorControllerTest {

    private final ErrorController errorController = new ErrorController();

    @Test
    void responseStatusException() {
        final var responseStatusException = mock(ResponseStatusException.class);
        when(responseStatusException.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        when(responseStatusException.getReason()).thenReturn("responseStatusException message");

        final var responseEntity = errorController.responseStatusException(responseStatusException);

        assertErrorResponse(responseEntity, HttpStatus.UNAUTHORIZED, "responseStatusException message");
    }

    @Test
    void constraintViolationException() {
        final var constraintViolationException = mock(ConstraintViolationException.class);
        when(constraintViolationException.getMessage()).thenReturn("constraintViolationException message");

        final var responseEntity = errorController.constraintViolationException(constraintViolationException);

        assertErrorResponse(responseEntity, HttpStatus.BAD_REQUEST, "constraintViolationException message");
    }

    private <T> void assertErrorResponse(
            ResponseEntity<WebResponse<T>> responseEntity,
            HttpStatus expectedStatus,
            String expectedErrorMessage
    ) {
        assertAll(
                () -> assertNotNull(responseEntity),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertNull(responseEntity.getBody().data()),
                () -> assertNull(responseEntity.getBody().paging()),
                () -> assertNotNull(responseEntity.getBody().errors()),
                () -> assertEquals(expectedErrorMessage, responseEntity.getBody().errors()),
                () -> assertEquals(expectedStatus, responseEntity.getStatusCode())
        );
    }
}