package org.knpkid.kms.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotated elements must match the created configuration <br><br>
 *
 * {@code null} elements are considered valid.
 */
@Documented
@Constraint(validatedBy = {FileValidator.class})
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface File {

    /**
     * @return image size must be lower or equal to
     */
    long max() default 15L;

    FileSize size() default FileSize.KB;

    FileFormat[] format() default {};

    String message() default "maximum file size is 15Kb";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
