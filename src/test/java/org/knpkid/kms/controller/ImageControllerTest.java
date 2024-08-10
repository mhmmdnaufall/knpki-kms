package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageControllerTest {

    private static final ImageController IMAGE_CONTROLLER = new ImageController();


    @Test
    void getImage() {

        try (var filesMock = mockStatic(Files.class)) {

            var imageByte = "image".getBytes();

            // success
            {
                filesMock.when(() -> Files.readAllBytes(any())).thenReturn(imageByte);
            }

            var imageByteArrayResponse = assertDoesNotThrow(() -> IMAGE_CONTROLLER.getImage("image.png"));
            assertEquals(imageByte, imageByteArrayResponse);

            // error
            {
                filesMock.when(() -> Files.readAllBytes(any())).thenThrow(IOException.class);
            }

            var exception = assertThrows(
                    ResponseStatusException.class,
                    () -> IMAGE_CONTROLLER.getImage("image.png")
            );

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals("'image.png' is not found", exception.getReason());

        }
    }
}