package org.knpkid.kms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.knpkid.kms.validation.File;
import org.springframework.web.multipart.MultipartFile;

import static org.knpkid.kms.validation.FileFormat.*;
import static org.knpkid.kms.validation.FileSize.MB;

public record RegisterAdminRequest(

        @NotBlank
        @Size(max = 20)
        String username,

        @NotBlank
        @Size(min = 8, max = 50)
        String password,

        @NotBlank
        @Size(max = 100)
        String name,

        @File(max = 2L, size = MB, format = {JPG, JPEG, PNG})
        MultipartFile image

) { }