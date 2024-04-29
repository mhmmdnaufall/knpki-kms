package org.knpkid.kms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {

    private long max;

    private FileSize fileSize;

    private FileFormat[] formats;

    @Override
    public void initialize(File constraintAnnotation) {
        max = constraintAnnotation.max();
        fileSize = constraintAnnotation.size();
        formats = constraintAnnotation.format();
    }

    @SneakyThrows
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        // null value = ok
        if (value == null) return true;

        final var errorMessageStringBuilder = new StringBuilder();
        errorMessageStringBuilder.append(context.getDefaultConstraintMessageTemplate());
        context.disableDefaultConstraintViolation(); // disable default constraint violation

        final var sizeOk = checkFileSize(value.getBytes().length, errorMessageStringBuilder);
        final var formatOk = (formats.length == 0) || checkFileFormat(value.getOriginalFilename(), errorMessageStringBuilder);

        if (sizeOk && formatOk) return true;

        // if not ok
        context.buildConstraintViolationWithTemplate(errorMessageStringBuilder.toString())
                .addConstraintViolation();

        return false;
    }

    private boolean checkFileSize(int size, StringBuilder errorMessageStringBuilder) {
        // file size OK
        if (size <= (max * fileSize.getSize()))
            return true;

        createErrorMessage(errorMessageStringBuilder, "maximum file size is %d%s".formatted(max, fileSize));
        return false;
    }

    private boolean checkFileFormat(String fileName, StringBuilder errorMessageStringBuilder) {
        return Optional.ofNullable(fileName)
                // if file format valid
                .filter(s ->
                        Stream.of(formats)
                                .anyMatch(format -> s.endsWith(format.name().toLowerCase()))
                )
                // return true
                .map(s -> true)
                // if file format not valid, return false
                .orElseGet(() -> {
                        createErrorMessage(
                                errorMessageStringBuilder,
                                "only " + Arrays.toString(formats).toLowerCase() + " format are acceptable"
                        );
                        return false;
                });
    }

    private void createErrorMessage(StringBuilder errorMessageStringBuilder, String errorMessage) {
        errorMessageStringBuilder
                .append(", ")
                .append(errorMessage);
    }
}
