package org.knpkid.kms.validation;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MultipartImageValidatorTest {

    private final MultipartImageValidator imageValidator = new MultipartImageValidator();

    @Test
    void initialize() {
        final var multipartImageAnnotation = mock(MultipartImage.class);
        when(multipartImageAnnotation.max()).thenReturn(5L);

        imageValidator.initialize(multipartImageAnnotation);

        verify(multipartImageAnnotation).max();
    }

    @Test
    void isValid() throws IOException {
        final var multipartFile = mock(MultipartFile.class);

        // multipartfile == null
        var isValid = imageValidator.isValid(null, null);
        assertTrue(isValid);

        // not image format error
        when(multipartFile.getOriginalFilename()).thenReturn("this is pdf file.pdf");

        final var responseStatusException = assertThrows(
                ResponseStatusException.class,
                () -> imageValidator.isValid(multipartFile, null)
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseStatusException.getStatusCode());
        assertEquals("only jpg, jpeg, and png format are acceptable", responseStatusException.getReason());

        // isValid
        initialize();
        when(multipartFile.getOriginalFilename()).thenReturn("this is image.jpg");

        // isValid equal
        when(multipartFile.getBytes()).thenReturn("12345".getBytes());
        isValid = imageValidator.isValid(multipartFile, null);
        assertTrue(isValid);

        // isValid lower
        when(multipartFile.getBytes()).thenReturn("1234".getBytes());
        isValid = imageValidator.isValid(multipartFile, null);
        assertTrue(isValid);

        // isValid higher
        when(multipartFile.getBytes()).thenReturn("123456".getBytes());
        isValid = imageValidator.isValid(multipartFile, null);
        assertFalse(isValid);

        // jpg
        when(multipartFile.getOriginalFilename()).thenReturn("this is image.jpg");
        assertDoesNotThrow(() -> imageValidator.isValid(multipartFile, null));

        // jpeg
        when(multipartFile.getOriginalFilename()).thenReturn("this is image.jpeg");
        assertDoesNotThrow(() -> imageValidator.isValid(multipartFile, null));

        // png
        when(multipartFile.getOriginalFilename()).thenReturn("this is image.png");
        assertDoesNotThrow(() -> imageValidator.isValid(multipartFile, null));


    }
}