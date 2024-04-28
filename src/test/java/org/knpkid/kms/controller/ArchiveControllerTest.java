package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.knpkid.kms.Constant.ARCHIVE_PATH_DIRECTORY;
import static org.mockito.Mockito.*;

class ArchiveControllerTest {

    private final ArchiveController archiveController = new ArchiveController();

    @ParameterizedTest(name = "{displayName}, wantToDownload={0}")
    @ValueSource(booleans = {true, false})
    void getOrDownload(boolean wantToDownload) {
        final var httpServletResponse = new MockHttpServletResponse();
        final var fileName = "file.pdf";
        final var filePath = ARCHIVE_PATH_DIRECTORY.resolve(fileName);

        try (final var filesMock = mockStatic(Files.class)) {

            filesMock.when(() -> Files.readAllBytes(filePath)).thenReturn(fileName.getBytes());

            final var fileBytes = archiveController.getOrDownload(fileName, wantToDownload, httpServletResponse);
            assertArrayEquals(fileName.getBytes(), fileBytes);

            if (wantToDownload) {
                assertEquals(
                        "attachment; filename=\"%s\"".formatted(fileName),
                        httpServletResponse.getHeader(HttpHeaders.CONTENT_DISPOSITION)
                );
            } else {
                assertNull(httpServletResponse.getHeader(HttpHeaders.CONTENT_DISPOSITION));
            }

        }
    }

    @Test
    void getOrDownload_readBytesError() {
        final var httpServletResponse = new MockHttpServletResponse();
        final var fileName = "file.pdf";
        final var filePath = ARCHIVE_PATH_DIRECTORY.resolve(fileName);

        try (final var filesMock = mockStatic(Files.class)) {

            filesMock.when(() -> Files.readAllBytes(filePath)).thenThrow(new IOException());

            final var error = assertThrows(
                    ResponseStatusException.class,
                    () -> archiveController.getOrDownload(fileName, false, httpServletResponse)
            );
            assertNull(httpServletResponse.getHeader(HttpHeaders.CONTENT_DISPOSITION));
            assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
            assertEquals("'file.pdf' is not found", error.getReason());

        }
    }
}