package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Archive;
import org.knpkid.kms.entity.ArchiveFormat;
import org.knpkid.kms.repository.ArchiveRepository;
import org.knpkid.kms.service.ArchiveService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;

import static org.knpkid.kms.Constant.ARCHIVE_PATH_DIRECTORY;

@Service
@RequiredArgsConstructor @Slf4j
public class ArchiveServiceImpl implements ArchiveService {

    private final ArchiveRepository archiveRepository;

    @Override
    @SneakyThrows
    public Archive save(MultipartFile archiveFile) {
        final var fileFormat = Objects.requireNonNull(archiveFile.getOriginalFilename()).split("\\.")[1];

        final var archive = new Archive();
        archive.setId(UUID.randomUUID().toString());
        archive.setFormat(ArchiveFormat.valueOf(fileFormat.toUpperCase()));

        final var archivePath = ARCHIVE_PATH_DIRECTORY.resolve(archive.toString());

        if (!Files.exists(archivePath))
            Files.createDirectories(ARCHIVE_PATH_DIRECTORY);

        archiveFile.transferTo(archivePath);

        return archiveRepository.save(archive);
    }

    @Override
    @SneakyThrows
    public void delete(Archive archive) {
        archiveRepository.delete(archive);
        final var archivePath = ARCHIVE_PATH_DIRECTORY.resolve(archive.toString());
        if (!Files.deleteIfExists(archivePath)) {
            log.warn("archive with id '{}' was missing before deletion", archive.getId());
        }
    }
}
