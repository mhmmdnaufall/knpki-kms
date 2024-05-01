package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.knpkid.kms.entity.Archive;
import org.knpkid.kms.entity.ArchiveFormat;
import org.knpkid.kms.repository.ArchiveRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchiveServiceImplTest {

    @InjectMocks
    private ArchiveServiceImpl archiveService;

    @Mock
    private ArchiveRepository archiveRepository;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void save(boolean isDirectoriesExist) throws IOException {
        final var uuid = UUID.randomUUID();
        try (final var filesMock = mockStatic(Files.class);
             final var uuidMock = mockStatic(UUID.class)) {

            final var archiveFile = mock(MultipartFile.class);
            {
                when(archiveFile.getOriginalFilename()).thenReturn("file.pdf");
                doNothing().when(archiveFile).transferTo(any(Path.class));
                uuidMock.when(UUID::randomUUID).thenReturn(uuid);
                when(archiveRepository.save(any()))
                        .then(invocation -> {
                            final var archive = (Archive) invocation.getArgument(0);
                            assertEquals(uuid.toString(), archive.getId());
                            assertEquals("pdf", archive.getFormat().name().toLowerCase());
                            return archive;
                        });
                filesMock.when(() -> Files.exists(any())).thenReturn(isDirectoriesExist);
            }

            assertDoesNotThrow(() -> archiveService.save(archiveFile));

            verify(archiveFile).getOriginalFilename();
            filesMock.verify(() -> Files.exists(any()));

            if (!isDirectoriesExist) filesMock.verify(() -> Files.createDirectories(any()));
            else filesMock.verify(() -> Files.createDirectories(any()), times(0));
        }
    }

    @Test
    @DisplayName("save - createDirectories Error")
    void save_errorIOException() throws IOException {
        try (final var filesMock = mockStatic(Files.class)) {
            final var archiveFile = mock(MultipartFile.class);

            {
                when(archiveFile.getOriginalFilename()).thenReturn("file.pdf");
                filesMock.when(() -> Files.exists(any())).thenReturn(false);
                filesMock.when(() -> Files.createDirectories(any())).thenThrow(IOException.class);
            }

            final var error = assertThrows(IOException.class, () -> archiveService.save(archiveFile));

            assertNotNull(error);
            filesMock.verify(() -> Files.exists(any()));
            filesMock.verify(() -> Files.createDirectories(any()));
            verify(archiveFile, times(0)).transferTo(any(Path.class));
            verify(archiveRepository, times(0)).save(any());
        }
    }

    @Test
    @DisplayName("save - tranferTo Error")
    void save_errorIOException_2() throws IOException {
        try (final var filesMock = mockStatic(Files.class)) {
            final var archiveFile = mock(MultipartFile.class);

            {
                when(archiveFile.getOriginalFilename()).thenReturn("file.pdf");
                filesMock.when(() -> Files.exists(any())).thenReturn(true);
                doThrow(IOException.class).when(archiveFile).transferTo(any(Path.class));
            }

            final var error = assertThrows(IOException.class, () -> archiveService.save(archiveFile));

            assertNotNull(error);
            filesMock.verify(() -> Files.exists(any()));
            filesMock.verify(() -> Files.createDirectories(any()), times(0));
            verify(archiveFile).transferTo(any(Path.class));
            verify(archiveRepository, times(0)).save(any());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void delete(boolean isArchiveFileExist) {
        final var uuid = UUID.randomUUID();
        final var archive = new Archive();
        archive.setId(uuid.toString());
        archive.setFormat(ArchiveFormat.PDF);

        try (final var filesMock = mockStatic(Files.class)) {
            {
                doNothing().when(archiveRepository).delete(archive);
                filesMock.when(() -> Files.deleteIfExists(any()))
                        .thenReturn(isArchiveFileExist);
            }

            assertDoesNotThrow(() -> archiveService.delete(archive));

            verify(archiveRepository).delete(archive);
            filesMock.verify(() -> Files.deleteIfExists(any()));
        }
    }

    @Test
    void delete_errorIOException() {
        final var uuid = UUID.randomUUID();
        final var archive = new Archive();
        archive.setId(uuid.toString());
        archive.setFormat(ArchiveFormat.PDF);

        try (final var filesMock = mockStatic(Files.class)) {
            {
                doNothing().when(archiveRepository).delete(archive);
                filesMock.when(() -> Files.deleteIfExists(any()))
                        .thenThrow(IOException.class);
            }

            final var error = assertThrows(IOException.class, () -> archiveService.delete(archive));

            assertNotNull(error);
            verify(archiveRepository).delete(archive);
            filesMock.verify(() -> Files.deleteIfExists(any()));
        }
    }
}