package org.knpkid.kms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.knpkid.kms.validation.MultipartImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public record UpdateArticleRequest(

        @NotBlank
        @Size(max = 100)
        String title,

        @MultipartImage(max = 2_097_151, message = "maximum image size is 2MB")
        MultipartFile coverImage,

        @NotBlank
        @Size(max = 65_535)
        String body,

        @Size(max = 200)
        String teaser,

        Set<@NotBlank @Size(max = 50) String> tags,

        List<@MultipartImage(max = 2_097_151, message = "maximum image size is 2MB") MultipartFile> images


) { }
