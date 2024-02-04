package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.service.ArticleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @InjectMocks
    private ArticleController articleController;

    @Mock
    private ArticleService articleService;

    @Test
    void create() throws IOException {
        final var multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("image".getBytes());

        final var request = new CreateArticleRequest(
                "title",
                multipartFile,
                "body",
                "teaser",
                null,
                null
        );
        final var response = new ArticleResponse(
                "id",
                request.title(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                request.body(),
                request.teaser(),
                null, request.coverImage().getBytes(), null
        );
        final var admin = new Admin();
        admin.setUsername("admin");
        when(articleService.create(request, admin)).thenReturn(response);

        final var webResponse = articleController.create(request, admin);
        verify(articleService).create(request, admin);

        assertNull(webResponse.errors());
        assertEquals(response, webResponse.data());
    }

    @Test
    void get() {
        final var articleId = "articleId";
        final var title = "title";
        final var now = LocalDateTime.now();
        final var body = "body";
        final var teaser = "teaser";
        final var coverImage = "coverImage".getBytes();
        final var tags = Set.of(new Tag());
        final var images = List.of(new ArticleImage());

        when(articleService.get(anyString())).thenReturn(
                new ArticleResponse(
                        articleId, title, now,
                        now, body, teaser,
                        tags, coverImage, images
                )
        );

        final var webResponse = articleController.get(articleId);

        verify(articleService).get(articleId);

        assertNotNull(webResponse);
        assertEquals(articleId, webResponse.data().getId());
        assertEquals(title, webResponse.data().getTitle());
        assertEquals(now, webResponse.data().getCreatedAt());
        assertEquals(now, webResponse.data().getUpdatedAt());
        assertEquals(body, webResponse.data().getBody());
        assertEquals(teaser, webResponse.data().getTeaser());
        assertEquals(coverImage, webResponse.data().getCoverImage());
        assertEquals(tags, webResponse.data().getTags());
        assertEquals(images, webResponse.data().getImages());

    }
}