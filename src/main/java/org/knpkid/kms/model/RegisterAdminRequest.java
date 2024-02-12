package org.knpkid.kms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.knpkid.kms.validation.MultipartImage;
import org.springframework.web.multipart.MultipartFile;

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

        @MultipartImage(max = 2_097_151, message = "maximum image size is 2MB")
        MultipartFile image

) { }