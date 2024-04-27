//package org.knpkid.kms.service.impl;
//
//import jakarta.persistence.criteria.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.knpkid.kms.entity.*;
//import org.knpkid.kms.model.CreateArticleRequest;
//import org.knpkid.kms.model.UpdateArticleRequest;
//import org.knpkid.kms.repository.ArticleRepository;
//import org.knpkid.kms.repository.TagRepository;
//import org.knpkid.kms.service.ImageService;
//import org.knpkid.kms.service.ValidationService;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ArticleServiceImplTest {
//
//    @InjectMocks
//    private ArticleServiceImpl articleService;
//
//    @Mock
//    private ArticleRepository articleRepository;
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @Mock
//    private ImageService imageService;
//
//    @Mock
//    private ValidationService validationService;
//
//    @DisplayName("create() - success")
//    @Test
//    void create() {
//
//        final var multipartFile = mock(MultipartFile.class);
//        final var image = new Image();
//        image.setId("imageId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var tagsString = Set.of("tag 1", "tag 2", "tag 3");
//        final var images = List.of(multipartFile);
//
//        final var request = new CreateArticleRequest(
//                "title", multipartFile, "body", "teaser", tagsString, images
//        );
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var now = LocalDateTime.now();
//
//        {
//            doNothing().when(validationService).validate(request);
//            when(imageService.save(any())).thenReturn(image);
//            when(tagRepository.saveAll(any())).then(invocation -> {
//                final var tags = (Set<Tag>) invocation.getArgument(0);
//
//                final var cleanTagNameSet = tagsString.stream()
//                        .map(tag -> tag.replaceAll("[^a-zA-Z0-9 ]", "").trim().toLowerCase())
//                        .collect(Collectors.toSet());
//
//                final var tagIdSet = cleanTagNameSet.stream()
//                        .map(tag -> tag.replace(' ', '-'))
//                        .collect(Collectors.toSet());
//
//                {
//                    assertEquals(3, tags.size());
//
//                    tags.forEach(tag -> {
//                        assertTrue(tagIdSet.contains(tag.getId()));
//                        assertFalse(tagIdSet.contains(tag.getName()));
//                        assertTrue(cleanTagNameSet.contains(tag.getName()));
//                        assertFalse(cleanTagNameSet.contains(tag.getId()));
//                    });
//                }
//
//                return tags.stream().toList();
//            });
//
//            when(articleRepository.save(any())).then(invocation -> {
//                final var article = (Article) invocation.getArgument(0);
//                {
//                    assertEquals("title", article.getTitle());
//                    assertEquals("body", article.getBody());
//                    assertEquals("teaser", article.getTeaser());
//                    assertEquals(admin, article.getAdmin());
//                    assertEquals("imageId", article.getCoverImage().getId());
//                    assertEquals(ImageFormat.PNG, article.getCoverImage().getFormat());
//                    assertEquals(1, article.getImageGallery().size());
//                    assertEquals("imageId", article.getImageGallery().get(0).getId());
//                    assertEquals(ImageFormat.PNG, article.getImageGallery().get(0).getFormat());
//                    assertTrue(
//                            article.getTags().stream()
//                                    .allMatch(tag -> tagsString.contains(tag.getName()))
//                    );
//                }
//                article.setId(32998593L);
//                article.setCreatedAt(now);
//                article.setUpdatedAt(now);
//                return article;
//            });
//        }
//
//        final var response = articleService.create(request, admin);
//
//        {
//            assertEquals(32998593L, response.id());
//            assertEquals("title", response.title());
//            assertTrue(
//                    response.tags().stream()
//                            .allMatch(tag -> tagsString.contains(tag.getName()))
//            );
//            assertEquals(admin, response.admin());
//            assertEquals("imageId", response.coverImage().getId());
//            assertEquals(ImageFormat.PNG, response.coverImage().getFormat());
//            assertEquals(1, response.images().size());
//            assertEquals(now, response.createdAt());
//            assertEquals(now, response.updatedAt());
//        }
//
//    }
//
//    @DisplayName("create() - null tags, images & coverImage")
//    @Test
//    void create_nullTagsImagesCoverImage() {
//        final var now = LocalDateTime.now();
//
//        final var request = new CreateArticleRequest(
//                "title", null, "body",
//                "teaser", null, null
//        );
//
//        final var admin = new Admin();
//
//        {
//            doNothing().when(validationService).validate(request);
//            when(articleRepository.save(any())).then(invocation -> {
//                final var article = (Article) invocation.getArgument(0);
//                {
//                    assertEquals("title", article.getTitle());
//                    assertEquals("body", article.getBody());
//                    assertEquals("teaser", article.getTeaser());
//                    assertEquals(admin, article.getAdmin());
//                    assertNull(article.getCoverImage());
//                    assertTrue(article.getTags().isEmpty());
//                    assertTrue(article.getImageGallery().isEmpty());
//                }
//                article.setId(4128415L);
//                article.setCreatedAt(now);
//                article.setUpdatedAt(now);
//                return article;
//            });
//        }
////
//        final var response = articleService.create(request, admin);
//
//        {
//            verify(imageService, times(0)).save(any());
//            verify(tagRepository, times(0)).saveAll(any());
//            assertEquals("title", response.title());
//            assertEquals("body", response.body());
//            assertEquals("teaser", response.teaser());
//            assertEquals(4128415L, response.id());
//            assertNull(response.coverImage());
//            assertEquals(Collections.emptyList(), response.images());
//            assertEquals(Collections.emptySet(), response.tags());
//        }
//
//    }
//
//    @Test
//    void get() {
//        final var existId = 2131231L;
//        final var notExistId = 12302130921L;
//        final var article = new Article();
//        article.setId(existId);
//
//        {
//            when(articleRepository.findById(existId)).thenReturn(Optional.of(article));
//            when(articleRepository.findById(notExistId)).thenReturn(Optional.empty());
//        }
//
//        // exist
//        var articleResponse = assertDoesNotThrow(() -> articleService.get(existId));
//        assertEquals(article.getId(), articleResponse.id());
//
//        // not exist
//        final var exception = assertThrows(ResponseStatusException.class, () -> articleService.get(notExistId));
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//        assertEquals("article with id '%d' is not found".formatted(notExistId), exception.getReason());
//
//
//    }
//
//    @DisplayName("update() - success")
//    @Test
//    void update() {
//        final var now = LocalDateTime.now();
//
//        final var multipartFile = mock(MultipartFile.class);
//
//        final var image = new Image();
//        image.setId("imageId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var oldImage = new Image();
//        oldImage.setId("oldImageId");
//        oldImage.setFormat(ImageFormat.PNG);
//
//        final var oldImageGallery = List.of(oldImage);
//
//        final var tagsString = Set.of("tag 1", "tag 2", "tag 3");
//        final var images = List.of(multipartFile);
//
//        final var request = new UpdateArticleRequest(
//                "title", multipartFile, "body", "teaser",
//                tagsString, images
//        );
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var article = new Article();
//        article.setAdmin(admin);
//        article.setCoverImage(oldImage);
//        article.setImageGallery(oldImageGallery);
//
//        {
//            when(articleRepository.findById(74729173L)).thenReturn(Optional.of(article));
//            doNothing().when(validationService).validate(any());
//            when(imageService.save(any())).thenReturn(image);
//            doNothing().when(imageService).delete(any());
//            doNothing().when(imageService).deleteAll(any());
//            when(tagRepository.saveAll(any())).then(invocation -> {
//                final var tags = (Set<Tag>) invocation.getArgument(0);
//
//                final var cleanTagNameSet = tagsString.stream()
//                        .map(tag -> tag.replaceAll("[^a-zA-Z0-9 ]", "").trim().toLowerCase())
//                        .collect(Collectors.toSet());
//
//                final var tagIdSet = cleanTagNameSet.stream()
//                        .map(tag -> tag.replace(' ', '-'))
//                        .collect(Collectors.toSet());
//                {
//                    assertEquals(3, tags.size());
//
//                    tags.forEach(tag -> {
//                        assertTrue(tagIdSet.contains(tag.getId()));
//                        assertFalse(tagIdSet.contains(tag.getName()));
//                        assertTrue(cleanTagNameSet.contains(tag.getName()));
//                        assertFalse(cleanTagNameSet.contains(tag.getId()));
//                    });
//                }
//
//                return tags.stream().toList();
//            });
//            when(articleRepository.save(article)).then(invocation -> {
//                final var articleUpdate = (Article) invocation.getArgument(0);
//
//                {
//                    assertEquals("title", articleUpdate.getTitle());
//                    assertEquals("body", articleUpdate.getBody());
//                    assertEquals("teaser", articleUpdate.getTeaser());
//                    assertTrue(
//                            articleUpdate.getTags().stream()
//                                    .allMatch(tag -> tagsString.contains(tag.getName()))
//                    );
//                }
//
//                articleUpdate.setId(74729173L);
//                articleUpdate.setCreatedAt(now);
//                articleUpdate.setUpdatedAt(now);
//
//                return articleUpdate;
//            });
//        }
//
//        final var response = assertDoesNotThrow(() ->  articleService.update(74729173L, request, admin));
//
//        {
//            verify(tagRepository).saveAll(any());
//            verify(imageService).delete(any());
//            verify(imageService).deleteAll(any());
//            assertEquals(74729173L, response.id());
//            assertEquals("title", response.title());
//            assertTrue(
//                    response.tags().stream()
//                            .allMatch(tag -> tagsString.contains(tag.getName()))
//            );
//            assertEquals(admin, response.admin());
//            assertEquals("imageId", response.coverImage().getId());
//            assertEquals(ImageFormat.PNG, response.coverImage().getFormat());
//            assertEquals(1, response.images().size());
//            assertEquals(now, response.createdAt());
//            assertEquals(now, response.updatedAt());
//        }
//
//    }
//
//    @DisplayName("update() - null tags, images & coverImage")
//    @Test
//    void update_nullTagsImagesCoverImage() {
//        final var now = LocalDateTime.now();
//
//        final var image = new Image();
//        image.setId("imageId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var request = new UpdateArticleRequest(
//                "title", null, "body",
//                "teaser", null, null
//        );
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var article = new Article();
//        article.setAdmin(admin);
//
//        {
//            when(articleRepository.findById(6423164612L)).thenReturn(Optional.of(article));
//            doNothing().when(validationService).validate(any());
//            when(articleRepository.save(article)).then(invocation -> {
//                final var articleUpdate = (Article) invocation.getArgument(0);
//
//                {
//                    assertEquals("title", articleUpdate.getTitle());
//                    assertEquals("body", articleUpdate.getBody());
//                    assertEquals("teaser", articleUpdate.getTeaser());
//                    assertNull(articleUpdate.getCoverImage());
//                    assertTrue(articleUpdate.getImageGallery().isEmpty());
//                    assertTrue(articleUpdate.getTags().isEmpty());
//                }
//
//                articleUpdate.setId(6423164612L);
//                articleUpdate.setCreatedAt(now);
//                articleUpdate.setUpdatedAt(now);
//
//                return articleUpdate;
//            });
//        }
//
//        final var response = assertDoesNotThrow(() ->  articleService.update(6423164612L, request, admin));
//
//        {
//            verify(imageService, times(0)).save(any());
//            verify(imageService, times(0)).delete(any());
//            verify(imageService, times(0)).deleteAll(any());
//            verify(tagRepository, times(0)).saveAll(any());
//            assertEquals(6423164612L, response.id());
//            assertEquals("title", response.title());
//            assertEquals(admin, response.admin());
//            assertNull(response.coverImage());
//            assertTrue(response.images().isEmpty());
//            assertTrue(response.tags().isEmpty());
//            assertEquals(now, response.createdAt());
//            assertEquals(now, response.updatedAt());
//        }
//    }
//
//    @DisplayName("update() - use Another Account forbidden")
//    @Test
//    void update_useAnotherAccount() {
//        final var request = new UpdateArticleRequest(
//                "title", null, "body",
//                "teaser", null, null
//        );
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var article = new Article();
//        article.setAdmin(admin);
//
//        final var diffAdmin = new Admin();
//        diffAdmin.setUsername("differentAdmin");
//
//        {
//            when(articleRepository.findById(74123790423L)).thenReturn(Optional.of(article));
//        }
//
//        final var exception = assertThrows(
//                ResponseStatusException.class,
//                () -> articleService.update(74123790423L, request, diffAdmin)
//        );
//
//        {
//            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
//            assertEquals("this article belongs to someone else", exception.getReason());
//        }
//    }
//
//    @DisplayName("update() - article not found")
//    @Test
//    void update_articleNotFound() {
//        final var request = new UpdateArticleRequest(
//                "title", null, "body",
//                "teaser", null, null
//        );
//
//        final var admin = new Admin();
//
//        {
//            when(articleRepository.findById(4392794L)).thenReturn(Optional.empty());
//        }
//
//        final var exception = assertThrows(
//                ResponseStatusException.class,
//                () -> articleService.update(4392794L, request, admin)
//        );
//
//        {
//            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//            assertEquals("article with id '%d' is not found".formatted(4392794L), exception.getReason());
//        }
//    }
//
//    @DisplayName("delete() - success")
//    @Test
//    void delete() {
//
//        final var image = new Image();
//        image.setId("imageId");
//        image.setFormat(ImageFormat.PNG);
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var article = new Article();
//        article.setAdmin(admin);
//        article.setCoverImage(image);
//        article.setImageGallery(List.of(image));
//
//        {
//            when(articleRepository.findById(123812038L)).thenReturn(Optional.of(article));
//            doNothing().when(articleRepository).delete(article);
//            doNothing().when(imageService).delete(any());
//            doNothing().when(imageService).deleteAll(any());
//        }
//
//        assertDoesNotThrow(() -> articleService.delete(123812038L, admin));
//
//        {
//            verify(articleRepository).delete(article);
//            verify(imageService).delete(any());
//            verify(imageService).deleteAll(any());
//        }
//
//    }
//
//    @DisplayName("delete() - null image & images")
//    @Test
//    void delete_nullImageAndImages() {
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var article = new Article();
//        article.setAdmin(admin);
//
//        {
//            when(articleRepository.findById(13123123L)).thenReturn(Optional.of(article));
//            doNothing().when(articleRepository).delete(article);
//        }
//
//        assertDoesNotThrow(() -> articleService.delete(13123123L, admin));
//
//        {
//            verify(articleRepository).delete(article);
//            verify(imageService, times(0)).delete(any());
//            verify(imageService, times(0)).deleteAll(any());
//        }
//
//    }
//
//    @DisplayName("delete() - article not found")
//    @Test
//    void delete_articleNotFound() {
//
//        final var admin = new Admin();
//
//        {
//            when(articleRepository.findById(1L)).thenReturn(Optional.empty());
//        }
//
//        final var exception = assertThrows(
//                ResponseStatusException.class,
//                () -> articleService.delete(1L, admin)
//        );
//
//        {
//            verify(articleRepository, times(0)).delete(any(Article.class));
//            verify(imageService, times(0)).delete(any());
//            verify(imageService, times(0)).deleteAll(any());
//            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//            assertEquals("article with id '%s' is not found".formatted(1L), exception.getReason());
//        }
//
//    }
//
//    @DisplayName("delete() - use another account forbidden")
//    @Test
//    void delete_useAnotherAccount() {
//
//        final var admin = new Admin();
//        admin.setUsername("admin");
//
//        final var article = new Article();
//        article.setAdmin(admin);
//
//        final var diffAdmin = new Admin();
//        diffAdmin.setUsername("different_admin");
//
//        {
//            when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
//        }
//
//        final var exception = assertThrows(
//                ResponseStatusException.class,
//                () -> articleService.delete(1L, diffAdmin)
//        );
//
//        {
//            verify(articleRepository, times(0)).delete(any(Article.class));
//            verify(imageService, times(0)).delete(any());
//            verify(imageService, times(0)).deleteAll(any());
//            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
//            assertEquals("this article belongs to someone else", exception.getReason());
//        }
//
//    }
//
//    @Test
//    void getAll() {
//        final var article = new Article();
//        article.setId(1L);
//        article.setTitle("title");
//        article.setTeaser("teaser");
//
//        final var articles = List.of(article);
//
//        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("updatedAt")));
//
//        {
//            when(articleRepository.findAll(pageable))
//                    .thenReturn(
//                            new PageImpl<>(
//                                    articles,
//                                    PageRequest.of(0, 12),
//                                    articles.size()
//                            )
//                    );
//        }
//
//        final var onlyArticleResponsePage = articleService.getAll(0, 12);
//
//        {
//            verify(articleRepository).findAll(pageable);
//        }
//
//        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
//        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
//        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
//        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());
//    }
//
//    @Test
//    void search() {
//        final var article = new Article();
//        article.setId(1L);
//        article.setTitle("keyword title");
//        article.setTeaser("keyword teaser");
//
//        final var articles = List.of(article);
//
//        {
//            when(articleRepository.findAll(any(Specification.class), any(Pageable.class)))
//                    .then(invocation -> {
//
//                        final var root = mock(Root.class);
//                        final var query = mock(CriteriaQuery.class);
//                        final var builder = mock(CriteriaBuilder.class);
//                        final var join = mock(Join.class);
//                        final var predicate = mock(Predicate.class);
//
//                        {
//                            when(builder.or(any(), any(), any())).thenReturn(predicate);
//                            when(root.join("tags", JoinType.LEFT)).thenReturn(join);
//                            when(query.where(any(Predicate.class))).thenReturn(query);
//                            when(query.getRestriction()).thenReturn(predicate);
//                        }
//
//                        final var specification = (Specification<Article>) invocation.getArgument(0);
//
//                        specification.toPredicate(root, query, builder);
//
//                        {
//                            verify(builder).or(any(), any(), any());
//                            verify(builder, times(3)).like(any(), anyString());
//                            verify(root, times(2)).get(anyString());
//                            verify(root).join("tags", JoinType.LEFT);
//                            verify(join).get(anyString());
//
//                        }
//
//                        return new PageImpl<>(
//                                articles,
//                                PageRequest.of(0, 12),
//                                articles.size()
//                        );
//                    });
//        }
//
//        final var onlyArticleResponsePage = articleService.search("keyword", 0, 12);
//
//        {
//            verify(articleRepository).findAll(any(Specification.class), any(Pageable.class));
//        }
//
//        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
//        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
//        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
//        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());
//
//    }
//
//    @Test
//    void getArticlesByTag() {
//
//        final var article = new Article();
//        article.setId(1L);
//        article.setTitle("title");
//        article.setTeaser("teaser");
//
//        final var articles = List.of(article);
//
//        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("updatedAt")));
//
//        {
//            when(articleRepository.findByTagsId("tagId", pageable))
//                    .thenReturn(
//                            new PageImpl<>(
//                                    articles,
//                                    PageRequest.of(0, 12),
//                                    articles.size()
//                            )
//                    );
//        }
//
//        final var onlyArticleResponsePage = articleService.getArticlesByTag("tagId", 0, 12);
//
//        {
//            verify(articleRepository).findByTagsId("tagId", pageable);
//        }
//
//        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
//        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
//        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
//        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());
//
//    }
//
//    @Test
//    void getArticlesByAdmin() {
//
//        final var article = new Article();
//        article.setId(1L);
//        article.setTitle("title");
//        article.setTeaser("teaser");
//
//        final var admin = new Admin();
//        admin.setUsername("username");
//        article.setAdmin(admin);
//
//        final var articles = List.of(article);
//
//        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("updatedAt")));
//
//        {
//            when(articleRepository.findByAdmin_Username("username", pageable))
//                    .thenReturn(
//                            new PageImpl<>(
//                                    articles,
//                                    PageRequest.of(0, 12),
//                                    articles.size()
//                            )
//                    );
//        }
//
//        final var onlyArticleResponsePage = articleService.getArticlesByAdmin("username", 0, 12);
//
//        {
//            verify(articleRepository).findByAdmin_Username("username", pageable);
//        }
//
//        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
//        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
//        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
//        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());
//
//    }
//}