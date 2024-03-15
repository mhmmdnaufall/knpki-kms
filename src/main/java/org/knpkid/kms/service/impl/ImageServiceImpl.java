package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.knpkid.kms.entity.Image;
import org.knpkid.kms.entity.ImageFormat;
import org.knpkid.kms.repository.ImageRepository;
import org.knpkid.kms.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    @SneakyThrows
    public Image save(MultipartFile imageFile) {
        final var imageFormat = Objects.requireNonNull(imageFile.getOriginalFilename()).split("\\.")[1];

        final var image = new Image();
        image.setId(UUID.randomUUID().toString());
        image.setFormat(ImageFormat.valueOf(imageFormat.toUpperCase()));

        final var imagePath = Path.of("image/%s.%s".formatted(image.getId(), image.getFormat().name().toLowerCase()));

        if (!Files.exists(imagePath.getParent()))
            Files.createDirectory(imagePath.getParent());

        imageFile.transferTo(imagePath);

        return imageRepository.save(image);

    }

}
