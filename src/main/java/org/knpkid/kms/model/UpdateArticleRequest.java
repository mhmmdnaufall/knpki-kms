package org.knpkid.kms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.knpkid.kms.validation.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static org.knpkid.kms.validation.FileFormat.*;
import static org.knpkid.kms.validation.FileSize.MB;

public record UpdateArticleRequest(

        @NotBlank
        @Size(max = 100)
        String title,

        @File(max = 2L, size = MB, format = {JPG, JPEG, PNG}, message = "maximum image size is 2MB")
        MultipartFile coverImage,

        @NotBlank
        @Size(max = 65_535)
        String body,

        @Size(max = 200)
        String teaser,

        Set<@NotBlank @Size(max = 50) String> tags,

        List<@File(max = 2L, size = MB, format = {JPG, JPEG, PNG}, message = "maximum image size is 2MB") MultipartFile> images,

        @NotEmpty
        Set<@NotBlank @Size(max = 100) String> authors,

        @File(max = 2, size = MB, format = PDF, message = "maximum pdf size is 2MB")
        MultipartFile archive

) { }
