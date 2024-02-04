package org.knpkid.kms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
                    if (!(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"))) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "only jpg, jpeg, and png format are acceptable"
                        );
                    }
                });

        return value.getBytes().length <= maxImageSize;
    }

}
