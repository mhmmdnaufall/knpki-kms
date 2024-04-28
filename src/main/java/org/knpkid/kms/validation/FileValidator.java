package org.knpkid.kms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

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

        if (value == null) return true;

        Optional.ofNullable(value.getOriginalFilename())
                .ifPresent(fileName -> {
                    if (formats.length == 0) {
                        return;
                    }

                    final var isWrongFormat = Arrays.stream(formats)
                            .noneMatch(format -> fileName.endsWith(format.name().toLowerCase()));

                    if (isWrongFormat) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "only " + Arrays.toString(formats).toLowerCase() + " format are acceptable"
                        );
                    }
                });

        return value.getBytes().length <= max * fileSize.getSize();
    }
}
