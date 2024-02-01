package org.knpkid.kms.service.impl;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.model.LoginAdminRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceImplTest {

    @InjectMocks
    private ValidationServiceImpl validationService;

    @Mock
    private Validator validator;

    @Test
    void validate() {
        final var request = new LoginAdminRequest("", "");

        // validated
        when(validator.validate(request)).thenReturn(Collections.emptySet());
        assertDoesNotThrow(() -> validationService.validate(request));

        // unvalidated
        when(validator.validate(request)).thenReturn(Set.of(ConstraintViolationImpl.forReturnValueValidation(
                null, null, null, null,
                null, null, null, null, null, null,
                null, null
        )));
        assertThrows(ConstraintViolationException.class, () -> validationService.validate(request));
    }
}