package org.knpkid.kms.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.*;
import org.knpkid.kms.service.ArticleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    void create() {
        final var request = new CreateArticleRequest(
                "title",
                null,
                "body",
                "teaser",
                null,
                null
        );

        final var admin = new Admin();
        admin.setUsername("admin");
        when(articleService.create(request, admin)).thenReturn("articleId");

        final var webResponse = articleController.create(request, admin);
        verify(articleService).create(request, admin);

        assertNull(webResponse.errors());
        assertNull(webResponse.paging());
        assertEquals("article created with id 'articleId'", webResponse.data());
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
        final var admin = new Admin();

        when(articleService.get(anyString())).thenReturn(
                new ArticleResponse(
                        articleId, title, now,
                        now, body, teaser,
                        tags, admin, coverImage, images
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
        assertNull(webResponse.errors());
        assertNull(webResponse.paging());

    }

    @Test
    void update() throws IOException {
        final var multipartFile = mock(MultipartFile.class);

        final var request = new UpdateArticleRequest(
                "title",
                multipartFile,
                "body",
                "teaser",
                null,
                null
        );

        final var admin = new Admin();
        admin.setUsername("admin");

        final var response = new ArticleResponse(
                "id",
                request.title(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                request.body(),
                request.teaser(),
                null, admin, request.coverImage().getBytes(), null
        );

        when(articleService.update("articleId", request, admin)).thenReturn(response);

        final var webResponse = articleController.update("articleId", request, admin);
        verify(articleService).update("articleId", request, admin);

        assertNull(webResponse.errors());
        assertNull(webResponse.paging());
        assertEquals(response, webResponse.data());
    }

    @Test
    void delete() {
        final var admin = new Admin();
        doNothing().when(articleService).delete("articleId", admin);
        articleController.delete("articleId", admin);
        verify(articleService).delete("articleId", admin);
    }

    @DisplayName("getAllOrSearchArticle() - getAll")
    @Test
    void getAllOrSearchArticle_getAll() {
        final var onlyArticleResponses = List.of(
                new OnlyArticleResponse(
                        "id", "title",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "body", "teaser", "coverImage".getBytes()
                )
        );

        {
            when(articleService.getAll(0, 12))
                    .thenReturn(
                            new PageImpl<>(
                                    onlyArticleResponses,
                                    PageRequest.of(0, 12),
                                    onlyArticleResponses.size()
                            )
                    );
        }

        final var webResponse = articleController.getAllOrSearchArticle(null, 0, 12);

        {
            verify(articleService, times(0)).search(anyString(), anyInt(), anyInt());
            verify(articleService).getAll(0, 12);
        }

        assertEquals(onlyArticleResponses, webResponse.data());
        assertNull(webResponse.errors());
        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());

    }

    @DisplayName("getAllOrSearchArticle() - search")
    @Test
    void getAllOrSearchArticle_search() {
        final var onlyArticleResponses = List.of(
                new OnlyArticleResponse(
                        "id", "search title",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "body", "search teaser", "coverImage".getBytes()
                )
        );

        {
            when(articleService.search("search", 0, 12))
                    .thenReturn(
                            new PageImpl<>(
                                    onlyArticleResponses,
                                    PageRequest.of(0, 12),
                                    onlyArticleResponses.size()
                            )
                    );
        }

        final var webResponse = articleController.getAllOrSearchArticle("search", 0, 12);

        {
            verify(articleService).search(anyString(), anyInt(), anyInt());
            verify(articleService, times(0)).getAll(anyInt(), anyInt());
        }

        assertEquals(onlyArticleResponses, webResponse.data());
        assertNull(webResponse.errors());
        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());

    }

    @Test
    void getArticlesByTag() {

        final var onlyArticleResponses = List.of(
                new OnlyArticleResponse(
                        "tagId", "search title",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "body", "search teaser", "coverImage".getBytes()
                )
        );

        {
            when(articleService.getArticlesByTag("tagId", 0, 12))
                    .thenReturn(
                            new PageImpl<>(
                                    onlyArticleResponses,
                                    PageRequest.of(0, 12),
                                    onlyArticleResponses.size()
                            )
                    );
        }

        final var webResponse = articleController.getArticlesByTag("tagId", 0, 12);

        assertEquals(onlyArticleResponses, webResponse.data());
        assertNull(webResponse.errors());
        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());

    }
}