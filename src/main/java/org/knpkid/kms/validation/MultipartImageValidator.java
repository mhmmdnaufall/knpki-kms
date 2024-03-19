package org.knpkid.kms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.knpkid.kms.entity.ImageFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

public class MultipartImageValidator implements ConstraintValidator<MultipartImage, MultipartFile> {

    private long maxImageSize;

    @Override
    public void initialize(MultipartImage constraintAnnotation) {
        maxImageSize = constraintAnnotation.max();
    }

    @SneakyThrows
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        Optional.ofNullable(value.getOriginalFilename())
                .ifPresent(fileName -> {
                    final var isWrongFormat = Arrays.stream(ImageFormat.values())
                            .noneMatch(format -> fileName.endsWith(format.name().toLowerCase()));

                    if (isWrongFormat) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "only jpg, jpeg, and png format are acceptable"
                        );
                    }
                });

        return value.getBytes().length <= maxImageSize;
    }

}
