package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.*;
import org.knpkid.kms.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping(
            path = "/api/articles",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.CREATED)
    public WebResponse<ArticleResponse> create(@ModelAttribute CreateArticleRequest request, Admin admin) {
        final var articleResponse = articleService.create(request, admin);
        return new WebResponse<>(articleResponse, null, null);
    }

    @GetMapping(
            path = "/api/articles/{articleId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ArticleResponse> get(@PathVariable("articleId") String articleId) {
        final var articleResponse = articleService.get(articleId);
        return new WebResponse<>(articleResponse, null, null);
    }

    @PutMapping(
            path = "/api/articles/{articleId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ArticleResponse> update(@PathVariable("articleId") String articleId,
                                      @ModelAttribute UpdateArticleRequest request,
                                      Admin admin) {

        final var articleResponse = articleService.update(articleId, request, admin);
        return new WebResponse<>(articleResponse, null, null);
    }

    @DeleteMapping("/api/articles/{articleId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("articleId") String articleId, Admin admin) {
        articleService.delete(articleId, admin);
    }

    @GetMapping(
            path = "/api/articles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OnlyArticleResponse>> getAllOrSearchArticle(
            @RequestParam(name = "search", required = false) String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "12") Integer size
    ) {

        final var onlyArticleResponsePage = keyword == null ?
                articleService.getAll(page, size) : articleService.search(keyword, page, size);

        return new WebResponse<>(
                onlyArticleResponsePage.getContent(), null,
                new PagingResponse(
                        onlyArticleResponsePage.getNumber(),
                        onlyArticleResponsePage.getTotalPages(),
                        onlyArticleResponsePage.getSize()
                )
        );

    }

    @GetMapping(
            path = "/api/tags/{tagId}/articles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OnlyArticleResponse>> getArticlesByTag(
            @PathVariable("tagId") String tagId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "12") Integer size
    ) {

        final var onlyArticleResponsePage = articleService.getArticlesByTag(tagId, page, size);

        return new WebResponse<>(
                onlyArticleResponsePage.getContent(), null,
                new PagingResponse(
                        onlyArticleResponsePage.getNumber(),
                        onlyArticleResponsePage.getTotalPages(),
                        onlyArticleResponsePage.getSize()
                )
        );

    }

    @GetMapping(
            path = "/api/admin/{username}/articles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OnlyArticleResponse>> getAdminArticle(
            @PathVariable("username") String username,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "12") Integer size
    ) {
        final var onlyArticleResponsePage = articleService.getArticlesByAdmin(username, page, size);
        return new WebResponse<>(
                onlyArticleResponsePage.getContent(), null,
                new PagingResponse(
                        onlyArticleResponsePage.getNumber(),
                        onlyArticleResponsePage.getTotalPages(),
                        onlyArticleResponsePage.getSize()
                )
        );

    }

}
