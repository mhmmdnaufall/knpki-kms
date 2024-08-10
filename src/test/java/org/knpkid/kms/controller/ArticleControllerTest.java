package org.knpkid.kms.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.*;
import org.knpkid.kms.model.*;
import org.knpkid.kms.service.ArticleService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
        var now = LocalDateTime.now();

        var admin = new Admin();
        admin.setUsername("admin");

        var request = new CreateArticleRequest(
                "title",
                null,
                "body",
                "teaser",
                null,
                null,
                null,
                null
        );

        var response = new ArticleResponse(
                1, "title", now, now, "body", "teaser",
                null, admin, null, null, null, null
        );

        when(articleService.create(request, admin)).thenReturn(response);

        var webResponseArticleResponse = articleController.create(request, admin);
        verify(articleService).create(request, admin);

        assertNull(webResponseArticleResponse.errors());
        assertNull(webResponseArticleResponse.paging());
        assertEquals(request.title(), webResponseArticleResponse.data().title());
        assertNull(webResponseArticleResponse.data().coverImage());
        assertEquals(request.body(), webResponseArticleResponse.data().body());
        assertEquals(request.teaser(), webResponseArticleResponse.data().teaser());
        assertNull(webResponseArticleResponse.data().tags());
        assertNull(webResponseArticleResponse.data().images());
        assertEquals(now, webResponseArticleResponse.data().createdAt());
        assertEquals(now, webResponseArticleResponse.data().updatedAt());
        assertEquals(admin, webResponseArticleResponse.data().admin());
    }

    @Test
    void get() {
        var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        var now = LocalDateTime.now();
        var tags = Set.of(new Tag());
        var admin = new Admin();
        var authors = Set.of(new Author());
        var archive = new Archive();

        when(articleService.get(1)).thenReturn(
                new ArticleResponse(
                        1, "title", now, now, "body", "teaser",
                        tags, admin, authors, image, List.of(image), archive
                )
        );

        var webResponseArticleResponse = articleController.get(1);

        verify(articleService).get(1);

        assertNotNull(webResponseArticleResponse);
        assertNull(webResponseArticleResponse.errors());
        assertNull(webResponseArticleResponse.paging());
        assertEquals(1, webResponseArticleResponse.data().id());
        assertEquals("title", webResponseArticleResponse.data().title());
        assertEquals(now, webResponseArticleResponse.data().createdAt());
        assertEquals(now, webResponseArticleResponse.data().updatedAt());
        assertEquals("body", webResponseArticleResponse.data().body());
        assertEquals("teaser", webResponseArticleResponse.data().teaser());
        assertEquals(tags, webResponseArticleResponse.data().tags());
        assertEquals(admin, webResponseArticleResponse.data().admin());
        assertEquals(authors, webResponseArticleResponse.data().authors());
        assertEquals(image, webResponseArticleResponse.data().coverImage());
        assertEquals(List.of(image), webResponseArticleResponse.data().images());
        assertEquals(archive, webResponseArticleResponse.data().archive());

    }

    @Test
    void update() {
        var now = LocalDateTime.now();

        var admin = new Admin();
        admin.setUsername("admin");

        var request = new UpdateArticleRequest(
                "title",
                null,
                "body",
                "teaser",
                null,
                null,
                null,
                null
        );

        var response = new ArticleResponse(
                1, "title", now, now, "body", "teaser",
                null, admin, null, null, null, null
        );

        when(articleService.update(1, request, admin)).thenReturn(response);

        var webResponseArticleResponse = articleController.update(1, request, admin);
        verify(articleService).update(1, request, admin);

        assertNull(webResponseArticleResponse.errors());
        assertNull(webResponseArticleResponse.paging());
        assertNotNull(webResponseArticleResponse.data());
        assertEquals(1, webResponseArticleResponse.data().id());
        assertEquals(request.title(), webResponseArticleResponse.data().title());
        assertEquals(now, webResponseArticleResponse.data().createdAt());
        assertEquals(now, webResponseArticleResponse.data().updatedAt());
        assertEquals(request.body(), webResponseArticleResponse.data().body());
        assertEquals(request.teaser(), webResponseArticleResponse.data().teaser());
        assertNull(webResponseArticleResponse.data().tags());
        assertEquals(admin, webResponseArticleResponse.data().admin());
        assertNull(webResponseArticleResponse.data().authors());
        assertNull(webResponseArticleResponse.data().coverImage());
        assertNull(webResponseArticleResponse.data().images());
        assertNull(webResponseArticleResponse.data().archive());
    }

    @Test
    void delete() {
        var admin = new Admin();
        doNothing().when(articleService).delete(1, admin);
        articleController.delete(1, admin);
        verify(articleService).delete(1, admin);
    }

    @DisplayName("getAllOrSearchArticle() - getAll")
    @Test
    void getAllOrSearchArticle_getAll() {
        var image = new Image();
        image.setId("articleId");
        image.setFormat(ImageFormat.PNG);

        var onlyArticleResponses = List.of(
                new OnlyArticleResponse(
                        1, "title",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "body", "teaser", image
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

        var webResponse = articleController.getAllOrSearchArticle(null, 0, 12);

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
        var image = new Image();
        image.setId("articleId");
        image.setFormat(ImageFormat.PNG);

        var onlyArticleResponses = List.of(
                new OnlyArticleResponse(
                        1, "search title",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "body", "search teaser", image
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

        var webResponse = articleController.getAllOrSearchArticle("search", 0, 12);

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
        var image = new Image();
        image.setId("articleId");
        image.setFormat(ImageFormat.PNG);

        var onlyArticleResponses = List.of(
                new OnlyArticleResponse(
                        1, "search title",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "body", "search teaser", image
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

        var webResponse = articleController.getArticlesByTag("tagId", 0, 12);

        assertEquals(onlyArticleResponses, webResponse.data());
        assertNull(webResponse.errors());
        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());

    }

    @Test
    void getAdminArticle() {
        var image = new Image();
        image.setId("articleId");
        image.setFormat(ImageFormat.PNG);

        var onlyArticleResponses = List.of(
                new OnlyArticleResponse(
                        1, "search title",
                        LocalDateTime.now(), LocalDateTime.now(),
                        "body", "search teaser", image
                )
        );

        {
            when(articleService.getArticlesByAdmin("username", 0, 12))
                    .thenReturn(
                            new PageImpl<>(
                                    onlyArticleResponses,
                                    PageRequest.of(0, 12),
                                    onlyArticleResponses.size()
                            )
                    );
        }

        var webResponse = articleController.getAdminArticle("username", 0, 12);

        assertEquals(onlyArticleResponses, webResponse.data());
        assertNull(webResponse.errors());
        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());

    }
}