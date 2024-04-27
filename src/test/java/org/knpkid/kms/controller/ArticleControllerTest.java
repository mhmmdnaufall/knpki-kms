//package org.knpkid.kms.controller;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.knpkid.kms.entity.Admin;
//import org.knpkid.kms.entity.Image;
//import org.knpkid.kms.entity.ImageFormat;
//import org.knpkid.kms.entity.Tag;
//import org.knpkid.kms.model.*;
//import org.knpkid.kms.service.ArticleService;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ArticleControllerTest {
//
//    @InjectMocks
//    private ArticleController articleController;
//
//    @Mock
//    private ArticleService articleService;
//
//    @Test
//    void create() {
//        final var now = LocalDateTime.now();
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var request = new CreateArticleRequest(
//                "title",
//                null,
//                "body",
//                "teaser",
//                null,
//                null
//        );
//
//        final var response = new ArticleResponse(
//                2371312987L, "title", now, now, "body", "teaser",
//                null, admin, null, null
//        );
//
//        when(articleService.create(request, admin)).thenReturn(response);
//
//        final var webResponse = articleController.create(request, admin);
//        verify(articleService).create(request, admin);
//
//        assertNull(webResponse.errors());
//        assertNull(webResponse.paging());
//        assertEquals(request.title(), webResponse.data().title());
//        assertNull(webResponse.data().coverImage());
//        assertEquals(request.body(), webResponse.data().body());
//        assertEquals(request.teaser(), webResponse.data().teaser());
//        assertNull(webResponse.data().tags());
//        assertNull(webResponse.data().images());
//        assertEquals(now, webResponse.data().createdAt());
//        assertEquals(now, webResponse.data().updatedAt());
//        assertEquals(admin, webResponse.data().admin());
//    }
//
//    @Test
//    void get() {
//        final var image = new Image();
//        image.setId("imageId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var now = LocalDateTime.now();
//        final var tags = Set.of(new Tag());
//        final var admin = new Admin();
//
//        when(articleService.get(57327592735L)).thenReturn(
//                new ArticleResponse(
//                        57327592735L, "title", now, now, "body", "teaser",
//                        tags, admin, image, List.of(image)
//                )
//        );
//
//        final var webResponse = articleController.get(57327592735L);
//
//        verify(articleService).get(57327592735L);
//
//        assertNotNull(webResponse);
//        assertNull(webResponse.errors());
//        assertNull(webResponse.paging());
//        assertEquals(57327592735L, webResponse.data().id());
//        assertEquals("title", webResponse.data().title());
//        assertEquals(now, webResponse.data().createdAt());
//        assertEquals(now, webResponse.data().updatedAt());
//        assertEquals("body", webResponse.data().body());
//        assertEquals("teaser", webResponse.data().teaser());
//        assertEquals(image, webResponse.data().coverImage());
//        assertEquals(tags, webResponse.data().tags());
//        assertEquals(List.of(image), webResponse.data().images());
//        assertEquals(admin, webResponse.data().admin());
//
//    }
//
//    @Test
//    void update() {
//        final var now = LocalDateTime.now();
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var request = new UpdateArticleRequest(
//                "title",
//                null,
//                "body",
//                "teaser",
//                null,
//                null
//        );
//
//        final var response = new ArticleResponse(
//                4594737594394L, "title", now, now, "body", "teaser",
//                null, admin, null, null
//        );
//
//        when(articleService.update(4594737594394L, request, admin)).thenReturn(response);
//
//        final var webResponse = articleController.update(4594737594394L, request, admin);
//        verify(articleService).update(4594737594394L, request, admin);
//
//        assertNull(webResponse.errors());
//        assertNull(webResponse.paging());
//        assertEquals(request.title(), webResponse.data().title());
//        assertNull(webResponse.data().coverImage());
//        assertEquals(request.body(), webResponse.data().body());
//        assertEquals(request.teaser(), webResponse.data().teaser());
//        assertNull(webResponse.data().tags());
//        assertNull(webResponse.data().images());
//        assertEquals(now, webResponse.data().createdAt());
//        assertEquals(now, webResponse.data().updatedAt());
//        assertEquals(admin, webResponse.data().admin());
//    }
//
//    @Test
//    void delete() {
//        final var admin = new Admin();
//        doNothing().when(articleService).delete(4594737594394L, admin);
//        articleController.delete(4594737594394L, admin);
//        verify(articleService).delete(4594737594394L, admin);
//    }
//
//    @DisplayName("getAllOrSearchArticle() - getAll")
//    @Test
//    void getAllOrSearchArticle_getAll() {
//        final var image = new Image();
//        image.setId("articleId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var onlyArticleResponses = List.of(
//                new OnlyArticleResponse(
//                        343242L, "title",
//                        LocalDateTime.now(), LocalDateTime.now(),
//                        "body", "teaser", image
//                )
//        );
//
//        {
//            when(articleService.getAll(0, 12))
//                    .thenReturn(
//                            new PageImpl<>(
//                                    onlyArticleResponses,
//                                    PageRequest.of(0, 12),
//                                    onlyArticleResponses.size()
//                            )
//                    );
//        }
//
//        final var webResponse = articleController.getAllOrSearchArticle(null, 0, 12);
//
//        {
//            verify(articleService, times(0)).search(anyString(), anyInt(), anyInt());
//            verify(articleService).getAll(0, 12);
//        }
//
//        assertEquals(onlyArticleResponses, webResponse.data());
//        assertNull(webResponse.errors());
//        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());
//
//    }
//
//    @DisplayName("getAllOrSearchArticle() - search")
//    @Test
//    void getAllOrSearchArticle_search() {
//        final var image = new Image();
//        image.setId("articleId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var onlyArticleResponses = List.of(
//                new OnlyArticleResponse(
//                        93240203L, "search title",
//                        LocalDateTime.now(), LocalDateTime.now(),
//                        "body", "search teaser", image
//                )
//        );
//
//        {
//            when(articleService.search("search", 0, 12))
//                    .thenReturn(
//                            new PageImpl<>(
//                                    onlyArticleResponses,
//                                    PageRequest.of(0, 12),
//                                    onlyArticleResponses.size()
//                            )
//                    );
//        }
//
//        final var webResponse = articleController.getAllOrSearchArticle("search", 0, 12);
//
//        {
//            verify(articleService).search(anyString(), anyInt(), anyInt());
//            verify(articleService, times(0)).getAll(anyInt(), anyInt());
//        }
//
//        assertEquals(onlyArticleResponses, webResponse.data());
//        assertNull(webResponse.errors());
//        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());
//
//    }
//
//    @Test
//    void getArticlesByTag() {
//        final var image = new Image();
//        image.setId("articleId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var onlyArticleResponses = List.of(
//                new OnlyArticleResponse(
//                        23509803258032L, "search title",
//                        LocalDateTime.now(), LocalDateTime.now(),
//                        "body", "search teaser", image
//                )
//        );
//
//        {
//            when(articleService.getArticlesByTag("tagId", 0, 12))
//                    .thenReturn(
//                            new PageImpl<>(
//                                    onlyArticleResponses,
//                                    PageRequest.of(0, 12),
//                                    onlyArticleResponses.size()
//                            )
//                    );
//        }
//
//        final var webResponse = articleController.getArticlesByTag("tagId", 0, 12);
//
//        assertEquals(onlyArticleResponses, webResponse.data());
//        assertNull(webResponse.errors());
//        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());
//
//    }
//
//    @Test
//    void getArticlesByUsername() {
//        final var image = new Image();
//        image.setId("articleId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var onlyArticleResponses = List.of(
//                new OnlyArticleResponse(
//                        342080352359L, "search title",
//                        LocalDateTime.now(), LocalDateTime.now(),
//                        "body", "search teaser", image
//                )
//        );
//
//        {
//            when(articleService.getArticlesByAdmin("username", 0, 12))
//                    .thenReturn(
//                            new PageImpl<>(
//                                    onlyArticleResponses,
//                                    PageRequest.of(0, 12),
//                                    onlyArticleResponses.size()
//                            )
//                    );
//        }
//
//        final var webResponse = articleController.getAdminArticle("username", 0, 12);
//
//        assertEquals(onlyArticleResponses, webResponse.data());
//        assertNull(webResponse.errors());
//        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());
//
//    }
//}