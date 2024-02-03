package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping(
            path = "/api/articles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.CREATED)
    public WebResponse<ArticleResponse> create(@RequestBody CreateArticleRequest request, Admin admin) {
        final var articleResponse = articleService.create(request, admin);
        return new WebResponse<>(articleResponse, null);
    }

    @GetMapping(
            path = "/api/articles/{articleId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ArticleResponse> get(@PathVariable("articleId") String articleId) {
        final var articleResponse = articleService.get(articleId);
        return new WebResponse<>(articleResponse, null);
    }

}
