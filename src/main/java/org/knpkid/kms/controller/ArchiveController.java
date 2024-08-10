package org.knpkid.kms.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;

import static org.knpkid.kms.Constant.ARCHIVE_PATH_DIRECTORY;

@RestController
public class ArchiveController {

    @GetMapping(
            path = "/archives/{archiveName}",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public byte[] getOrDownload(
            @PathVariable String archiveName,
            @RequestParam(
                    name = "download",
                    defaultValue = "false",
                    required = false
            )
            boolean wantToDownload,
            HttpServletResponse response
    ) {
        var filePath = ARCHIVE_PATH_DIRECTORY.resolve(archiveName);
        try {
            if (wantToDownload) {
                response.setHeader(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"%s\"".formatted(filePath.getFileName())
                );
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "'%s' is not found".formatted(archiveName));
        }
    }

}
