package org.knpkid.kms.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * The annotated element must be an image format (jpg, jpeg, png) <br><br>
 *
 * {@code null} elements are considered valid.
 */
@Documented
@Constraint(validatedBy = {MultipartImageValidator.class})
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartImage {

    /**
     * @return image size must be lower or equal to
     */
    long max();

    String message() default "image size is too large";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
