package org.knpkid.kms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
public class CreateArticleRequest {

    @NotBlank
    @Size(max = 100)
    String title;

    @Size(max = 16_777_215, message = "maximum image size is 16MB")
    byte[] coverImage;

    @NotBlank
    @Size(max = 65_535)
    String body;

    @Size(max = 200)
    String teaser;

    Set<@NotBlank @Size(max = 50) String> tags;

    List<@Size(max = 16_777_215, message = "maximum image size is 16MB") byte[]> images;

}
