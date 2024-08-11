package org.knpkid.kms.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErrorControllerTest {

    private final ErrorController errorController = new ErrorController();

    @Test
    void responseStatusException() {
        var responseStatusException = mock(ResponseStatusException.class);
        when(responseStatusException.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        when(responseStatusException.getReason()).thenReturn("responseStatusException message");

        var responseEntity = errorController.responseStatusException(responseStatusException);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertNull(responseEntity.getBody().data());
        assertNull(responseEntity.getBody().paging());
        assertNotNull(responseEntity.getBody().errors());
        assertEquals("responseStatusException message", responseEntity.getBody().errors());
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    void constraintViolationException() {
        var violation1 = mock(ConstraintViolation.class);
        var violation2 = mock(ConstraintViolation.class);

        var path1 = mock(jakarta.validation.Path.class);
        var path2 = mock(jakarta.validation.Path.class);

        {
            when(path1.toString()).thenReturn("field1");
            when(path2.toString()).thenReturn("field2");

            when(violation1.getPropertyPath()).thenReturn(path1);
            when(violation2.getPropertyPath()).thenReturn(path2);

            when(violation1.getMessage()).thenReturn("must not be null");
            when(violation2.getMessage()).thenReturn("must be greater than zero");
        }

        var violationSet = Set.<ConstraintViolation<?>>of(violation1, violation2);

        var constraintViolationException = new ConstraintViolationException(violationSet);

        var response = errorController.constraintViolationException(constraintViolationException);

        var errors = (Map<String, List<String>>) response.errors();
        assertEquals(2, errors.size());
        assertEquals(1, errors.get("field1").size());
        assertEquals("must not be null", errors.get("field1").get(0));
        assertEquals(1, errors.get("field2").size());
        assertEquals("must be greater than zero", errors.get("field2").get(0));
    }
}