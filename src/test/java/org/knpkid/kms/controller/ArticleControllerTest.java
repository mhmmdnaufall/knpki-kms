package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.service.ArticleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @InjectMocks
    private ArticleController articleController;

    @Mock
    private ArticleService articleService;

    @Test
    void create() {
        final var request = new CreateArticleRequest(
                "title",
                "coverImage".getBytes(),
                "body",
                "teaser",
                null,
                null
        );
        final var response = new ArticleResponse(
                "id",
                request.getTitle(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                request.getBody(),
                request.getTeaser(),
                null, request.getCoverImage(), null
        );
        final var admin = new Admin();
        admin.setUsername("admin");
        when(articleService.create(request, admin)).thenReturn(response);

        final var webResponse = articleController.create(request, admin);
        verify(articleService).create(request, admin);

        assertNull(webResponse.errors());
        assertEquals(response, webResponse.data());
    }

}