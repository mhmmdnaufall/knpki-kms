package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.model.AuthorResponse;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.AuthorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(path = "/api/authors/{authorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AuthorResponse> get(@PathVariable Integer authorId) {
        final var authorResponse = authorService.get(authorId);
        return new WebResponse<>(authorResponse, null, null);
    }

}
