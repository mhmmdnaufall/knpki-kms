package org.knpkid.kms.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.knpkid.kms.validation.FileFormat.JPG;
import static org.mockito.Mockito.*;

class FileValidatorTest {

    private final FileValidator fileValidator = new FileValidator();

    @Test
    void initialize() {
        var fileAnnotation = mock(File.class);
        {
            when(fileAnnotation.max()).thenReturn(5L);
            when(fileAnnotation.size()).thenReturn(FileSize.KB);
            when(fileAnnotation.format()).thenReturn(new FileFormat[]{JPG});
        }

        fileValidator.initialize(fileAnnotation);

        verify(fileAnnotation).max();
        verify(fileAnnotation).size();
        verify(fileAnnotation).format();
    }

    @Test
    void isValid() throws IOException {
        initialize();
        var multipartFile = mock(MultipartFile.class);
        var context = mock(ConstraintValidatorContext.class);

        {
            when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
            when(multipartFile.getBytes()).thenReturn(new byte[5 * FileSize.KB.getSize()]);
            doNothing().when(context).disableDefaultConstraintViolation();
        }

        var isValid = fileValidator.isValid(multipartFile, context);

        assertTrue(isValid);
        verify(context).disableDefaultConstraintViolation();
        verify(context, times(0)).buildConstraintViolationWithTemplate(anyString());
    }

    @Test
    void isValid_multipartFile_null() {
        var isValid = fileValidator.isValid(null, null);
        assertTrue(isValid);
    }

    @Test
    void isValid_wrongFileSize() throws IOException {
        initialize();
        var multipartFile = mock(MultipartFile.class);
        var context = mock(ConstraintValidatorContext.class);
        var builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        {
            when(multipartFile.getOriginalFilename()).thenReturn("file.jpg");
            when(multipartFile.getBytes()).thenReturn(new byte[6 * FileSize.KB.getSize()]); // should lte 5kb
            doNothing().when(context).disableDefaultConstraintViolation();
            when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
            when(builder.addConstraintViolation()).thenReturn(context);
        }

        var isValid = fileValidator.isValid(multipartFile, context);

        assertFalse(isValid);
        verify(context).buildConstraintViolationWithTemplate(anyString());
        verify(builder).addConstraintViolation();
    }

    @Test
    void isValid_wrongFileFormat() throws IOException {
        initialize();
        var multipartFile = mock(MultipartFile.class);
        var context = mock(ConstraintValidatorContext.class);
        var builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);

        {
            when(multipartFile.getOriginalFilename()).thenReturn("file.pdf"); // should only accept `jpg` file
            when(multipartFile.getBytes()).thenReturn(new byte[5 * FileSize.KB.getSize()]);
            doNothing().when(context).disableDefaultConstraintViolation();
            when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
            when(builder.addConstraintViolation()).thenReturn(context);
        }

        var isValid = fileValidator.isValid(multipartFile, context);

        assertFalse(isValid);
        verify(context).buildConstraintViolationWithTemplate(anyString());
        verify(builder).addConstraintViolation();
    }

    @ParameterizedTest
    @ValueSource(strings = {"jpg", "jpeg", "png", "pdf"})
    void isValid_validForEveryFileFormat(String format) throws IOException {
        // init
        var fileAnnotation = mock(File.class);
        {
            when(fileAnnotation.max()).thenReturn(5L);
            when(fileAnnotation.size()).thenReturn(FileSize.KB);
            when(fileAnnotation.format()).thenReturn(new FileFormat[]{});
        }
        fileValidator.initialize(fileAnnotation);

        var multipartFile = mock(MultipartFile.class);
        var context = mock(ConstraintValidatorContext.class);

        // jpg
        {
            when(multipartFile.getOriginalFilename()).thenReturn("test.%s".formatted(format));
            when(multipartFile.getBytes()).thenReturn(new byte[5 * FileSize.KB.getSize()]);
            doNothing().when(context).disableDefaultConstraintViolation();
        }

        var isValid = fileValidator.isValid(multipartFile, context);

        assertTrue(isValid);
        verify(context).disableDefaultConstraintViolation();
        verify(context, times(0)).buildConstraintViolationWithTemplate(anyString());


    }

    @Test
    void isValid_getBytesError() throws IOException {
        var multipartFile = mock(MultipartFile.class);
        var context = mock(ConstraintValidatorContext.class);

        {
            doNothing().when(context).disableDefaultConstraintViolation();
            when(multipartFile.getBytes()).thenThrow(IOException.class);
        }

        assertThrows(IOException.class, () -> fileValidator.isValid(multipartFile, context));
    }
}