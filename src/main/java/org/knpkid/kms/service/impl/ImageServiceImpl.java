package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Image;
import org.knpkid.kms.entity.ImageFormat;
import org.knpkid.kms.repository.ImageRepository;
import org.knpkid.kms.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

import static org.knpkid.kms.Constant.IMAGE_PATH_DIRECTORY;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    @SneakyThrows
    public Image save(MultipartFile imageFile) {
        var imageFormat = Objects.requireNonNull(imageFile.getOriginalFilename()).split("\\.")[1];

        var image = new Image();
        image.setId(UUID.randomUUID().toString());
        image.setFormat(ImageFormat.valueOf(imageFormat.toUpperCase()));

        var imagePath = IMAGE_PATH_DIRECTORY.resolve(image.toString());

        if (!Files.exists(IMAGE_PATH_DIRECTORY))
            Files.createDirectories(IMAGE_PATH_DIRECTORY);

        imageFile.transferTo(imagePath);

        return imageRepository.save(image);

    }

    @Override
    @SneakyThrows
    public void delete(Image image) {
        imageRepository.delete(image);
        var imagePath = IMAGE_PATH_DIRECTORY.resolve(image.toString());
        if (!Files.deleteIfExists(imagePath)) {
            log.warn("image with id '{}' was missing before deletion", image.getId());
        }
    }

    @Override
    @SneakyThrows
    public void deleteAll(Iterable<Image> images) {
        imageRepository.deleteAll(images);
        for (var image : images) {
            var imagePath = IMAGE_PATH_DIRECTORY.resolve(image.toString());
            if (!Files.deleteIfExists(imagePath)) {
                log.warn("image with id '{}' was missing before deletion", image.getId());
            }
        }
    }

}