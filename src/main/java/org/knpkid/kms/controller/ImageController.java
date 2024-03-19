package org.knpkid.kms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ImageController {

    private static final Path IMAGE_PATH_DIRECTORY = Path.of("image");

    @GetMapping(
            path = "/image/{imageFileName}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, "image/jpg"}
    )
    public byte[] getImage(@PathVariable("imageFileName") String imageFileName) {
        try {
            return Files.readAllBytes(IMAGE_PATH_DIRECTORY.resolve(imageFileName));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "'%s' is not found".formatted(imageFileName));
        }
    }

}
