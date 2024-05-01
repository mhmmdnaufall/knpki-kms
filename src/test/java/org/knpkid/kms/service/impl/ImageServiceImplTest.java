package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.knpkid.kms.entity.Image;
import org.knpkid.kms.entity.ImageFormat;
import org.knpkid.kms.repository.ImageRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private ImageRepository imageRepository;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void save(boolean isImageDirectoryExist) throws IOException {

        try (final var filesMock = mockStatic(Files.class)) {
            final var multipartFileMock = mock(MultipartFile.class);

            {
                when(multipartFileMock.getOriginalFilename()).thenReturn("image.jpg");
                filesMock.when(() -> Files.exists(any())).thenReturn(isImageDirectoryExist);
                doNothing().when(multipartFileMock).transferTo(any(Path.class));
                when(imageRepository.save(any())).then(invocation -> {
                    final var image = (Image) invocation.getArgument(0);
                    assertEquals(ImageFormat.JPG, image.getFormat());

                    return image;
                });
            }

            assertDoesNotThrow(() -> imageService.save(multipartFileMock));

            {
                if (!isImageDirectoryExist) {
                    filesMock.verify(() -> Files.createDirectories(any()), times(1));

                    // error test
                    filesMock.when(() -> Files.createDirectories(any())).thenThrow(new IOException());
                    assertThrows(IOException.class, () -> imageService.save(multipartFileMock));
                } else
                    filesMock.verify(() -> Files.createDirectories(any()), times(0));

            }

        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void delete(boolean isImagePathExist) {

        try (final var filesMock = mockStatic(Files.class)) {

            final var image = new Image();
            image.setId("imageId");
            image.setFormat(ImageFormat.PNG);

            {
                doNothing().when(imageRepository).delete(image);
                filesMock.when(() -> Files.deleteIfExists(any())).thenReturn(isImagePathExist);
            }

            assertDoesNotThrow(() -> imageService.delete(image));

            {
                verify(imageRepository).delete(image);
                filesMock.verify(() -> Files.deleteIfExists(any()));
            }

            // error delete path
            filesMock.when(() -> Files.deleteIfExists(any())).thenThrow(new IOException());
            assertThrows(IOException.class, () -> imageService.delete(image));

        }

    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void deleteAll(boolean isImagePathExist) {

        try (final var filesMock = mockStatic(Files.class)) {

            final var image1 = new Image();
            image1.setId("image1");
            image1.setFormat(ImageFormat.PNG);

            final var image2 = new Image();
            image2.setId("image2");
            image2.setFormat(ImageFormat.JPG);

            final var image3 = new Image();
            image3.setId("image3");
            image3.setFormat(ImageFormat.JPEG);

            final var images = List.of(image1, image2, image3);

            {
                doNothing().when(imageRepository).deleteAll(images);
                filesMock.when(() -> Files.deleteIfExists(any())).thenReturn(isImagePathExist);
            }

            assertDoesNotThrow(() -> imageService.deleteAll(images));

            {
                verify(imageRepository).deleteAll(images);
                filesMock.verify(() -> Files.deleteIfExists(any()), times(images.size()));
            }

            // error delete path
            filesMock.when(() -> Files.deleteIfExists(any())).thenThrow(new IOException());
            assertThrows(IOException.class, () -> imageService.deleteAll(images));

        }
    }

}