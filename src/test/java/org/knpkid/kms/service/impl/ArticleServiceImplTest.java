package org.knpkid.kms.service.impl;

import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.UpdateArticleRequest;
import org.knpkid.kms.repository.ArticleImageRepository;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.repository.TagRepository;
import org.knpkid.kms.service.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ArticleImageRepository articleImageRepository;

    @Mock
    private ValidationService validationService;

    @DisplayName("create() - success")
    @Test
    void create() throws IOException {

        final var multipartFile = mock(MultipartFile.class);
        final var imageBytes = "image".getBytes();
        when(multipartFile.getBytes()).thenReturn(imageBytes);

        final var tags = Set.of("tag1", "tag2", "tag3");
        final var images = List.of(multipartFile, multipartFile, multipartFile);

        final var request = new CreateArticleRequest(
                "title", multipartFile, "body",
                "teaser", tags, images
        );

        final var admin = new Admin();
        admin.setUsername("admin");

        {
            doNothing().when(validationService).validate(request);
            when(tagRepository.saveAll(any())).thenReturn(List.of(new Tag()));
            when(articleRepository.save(any())).then(invocation -> {
                final var article = (Article) invocation.getArgument(0);
                {
                    assertEquals("title", article.getTitle());
                    assertEquals("body", article.getBody());
                    assertEquals("teaser", article.getTeaser());
                    assertEquals(admin, article.getAdmin());
                    assertSame(imageBytes, article.getCoverImage());
                    assertTrue(
                            article.getTags().stream()
                                    .allMatch(tag -> tags.contains(tag.getName()))
                    );
                }
                article.setId("articleId");
                return article;
            });

            when(articleImageRepository.saveAll(any())).then(invocation -> {
                final var articleImages = (List<ArticleImage>) invocation.getArgument(0);
                assertNotNull(articleImages);
                assertFalse(articleImages.isEmpty());
                return articleImages;
            });
        }

        final var articleId = articleService.create(request, admin);

        {
            verify(validationService).validate(any());
            verify(tagRepository).saveAll(any());
            verify(articleRepository).save(any());
            verify(articleImageRepository).saveAll(any());
        }

        assertEquals("articleId", articleId);

    }

    @DisplayName("create() - null tags, images & coverImage")
    @Test
    void create_nullTagsImagesCoverImage() {
        final var request = new CreateArticleRequest(
                "title", null, "body",
                "teaser", null, null
        );

        final var admin = new Admin();

        {
            doNothing().when(validationService).validate(request);
            when(articleRepository.save(any())).then(invocation -> {
                final var article = (Article) invocation.getArgument(0);
                {
                    assertEquals("title", article.getTitle());
                    assertEquals("body", article.getBody());
                    assertEquals("teaser", article.getTeaser());
                    assertEquals(admin, article.getAdmin());
                    assertSame(null, article.getCoverImage());
                    assertEquals(Collections.emptySet(), article.getTags());
                }
                article.setId("articleId");
                return article;
            });
        }

        final var articleId = articleService.create(request, admin);

        {
            verify(validationService).validate(any());
            verify(tagRepository, times(0)).saveAll(any());
            verify(articleRepository).save(any());
            verify(articleImageRepository, times(0)).saveAll(any());
        }

        assertEquals("articleId", articleId);

    }

    @DisplayName("create() - MultipartFile.getBytes() error")
    @Test
    void create_MultipartGetBytesError() throws IOException {
        final var multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenThrow(new IOException());

        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of(multipartFile, multipartFile, multipartFile);

        final var request = new CreateArticleRequest(
                "title", null, "body",
                "teaser", tags, images
        );

        final var admin = new Admin();

        final var errorResponseException = assertThrows(
                ErrorResponseException.class,
                () -> articleService.create(request, admin)
        );

        assertEquals(HttpStatus.BAD_REQUEST, errorResponseException.getStatusCode());
    }

    @Test
    void get() {
        final var article = new Article();
        article.setId("exist");
        {
            when(articleRepository.findById("exist")).thenReturn(Optional.of(article));
            when(articleRepository.findById("not exist")).thenReturn(Optional.empty());
        }

        var articleResponse = assertDoesNotThrow(() -> articleService.get("exist"));
        assertEquals(article.getId(), articleResponse.getId());


        final var exception = assertThrows(ResponseStatusException.class, () -> articleService.get("not exist"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("article with id 'not exist' is not found", exception.getReason());


    }

    @DisplayName("update() - success")
    @Test
    void update() throws IOException {
        final var multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("image".getBytes());

        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of(multipartFile, multipartFile, multipartFile);

        final var request = new UpdateArticleRequest(
                "title", multipartFile, "body", "teaser",
                tags, images
        );

        final var article = new Article();
        final var admin = new Admin();
        admin.setUsername("admin");

        article.setAdmin(admin);
        article.setId("articleId");

        {
            when(articleRepository.findById("articleId")).thenReturn(Optional.of(article));
            when(tagRepository.saveAll(any())).thenReturn(List.of(new Tag()));
            when(articleRepository.save(any())).thenReturn(new Article());
            when(articleImageRepository.saveAll(any())).thenReturn(List.of(new ArticleImage()));
            doNothing().when(validationService).validate(request);
        }

        assertDoesNotThrow(() -> articleService.update("articleId", request, admin));

        {
            verify(validationService).validate(request);
            verify(tagRepository).saveAll(any());
            verify(articleImageRepository).saveAll(any());
        }

    }

    @DisplayName("update() - use Another Account forbidden")
    @Test
    void update_useAnotherAccount() {
        final var multipartFile = mock(MultipartFile.class);

        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of(multipartFile, multipartFile, multipartFile);

        final var request = new UpdateArticleRequest(
                "title", multipartFile, "body", "teaser",
                tags, images
        );

        final var article = new Article();
        final var admin = new Admin();
        admin.setUsername("admin");

        article.setAdmin(admin);
        article.setId("articleId");

        final var anotherAdmin = new Admin();
        anotherAdmin.setUsername("another admin");

        {
            when(articleRepository.findById("articleId")).thenReturn(Optional.of(article));
        }

        final var responseStatusException = assertThrows(
                ResponseStatusException.class,
                () -> articleService.update("articleId", request, anotherAdmin)
        );
        assertEquals(HttpStatus.FORBIDDEN, responseStatusException.getStatusCode());
        assertEquals("this article belongs to someone else", responseStatusException.getReason());
    }

    @DisplayName("update() - article not found")
    @Test
    void update_articleNotFound() {
        final var multipartFile = mock(MultipartFile.class);

        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of(multipartFile, multipartFile, multipartFile);

        final var request = new UpdateArticleRequest(
                "title", multipartFile, "body", "teaser",
                tags, images
        );

        final var article = new Article();
        final var admin = new Admin();
        admin.setUsername("admin");

        article.setAdmin(admin);
        article.setId("articleId");

        {
            when(articleRepository.findById("not found")).thenReturn(Optional.empty());
        }
        final var responseStatusException = assertThrows(
                ResponseStatusException.class,
                () -> articleService.update("not found", request, admin)
        );

        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatusCode());
        assertEquals("article with id 'not found' is not found", responseStatusException.getReason());
    }

    @DisplayName("update() - null coverImage")
    @Test
    void update_nullCoverImage() throws IOException {
        final var multipartFile = mock(MultipartFile.class);

        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of(multipartFile, multipartFile, multipartFile);

        final var request = new UpdateArticleRequest(
                "title", null, "body", "teaser",
                tags, images
        );

        final var article = new Article();
        final var admin = new Admin();
        admin.setUsername("admin");

        article.setAdmin(admin);
        article.setId("articleId");

        {
            when(multipartFile.getBytes()).thenReturn("image".getBytes());
            when(articleRepository.findById("articleId")).thenReturn(Optional.of(article));
        }

        assertDoesNotThrow(() -> articleService.update("articleId", request, admin));
    }

    @DisplayName("update() - MultipartFile.getBytes() error")
    @Test
    void update_MultipartGetBytesError() throws IOException {

        final var multipartFile = mock(MultipartFile.class);

        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of(multipartFile, multipartFile, multipartFile);

        final var request = new UpdateArticleRequest(
                "title", null, "body", "teaser",
                tags, images
        );

        final var article = new Article();
        final var admin = new Admin();
        admin.setUsername("admin");

        article.setAdmin(admin);
        article.setId("articleId");

        {
            when(multipartFile.getBytes()).thenThrow(new IOException());
            when(articleRepository.findById("articleId")).thenReturn(Optional.of(article));
        }

        final var errorResponseException = assertThrows(
                ErrorResponseException.class,
                () -> articleService.update("articleId", request, admin)
        );
        assertEquals(HttpStatus.BAD_REQUEST, errorResponseException.getStatusCode());

    }

    @DisplayName("update() - null tags and images")
    @Test
    void update_nullTagImage() {
        final var request = new UpdateArticleRequest(
                "title", null, "body", "teaser",
                null, null
        );

        final var article = new Article();
        final var admin = new Admin();
        admin.setUsername("admin");

        article.setAdmin(admin);
        article.setId("articleId");

        when(articleRepository.findById("articleId")).thenReturn(Optional.of(article));

        assertDoesNotThrow(() -> articleService.update("articleId", request, admin));
    }

    @DisplayName("delete() - success")
    @Test
    void delete() {
        final var admin = new Admin();
        final var article = new Article();
        article.setAdmin(admin);
        {
            when(articleRepository.findById("articleId")).thenReturn(Optional.of(article));
            doNothing().when(articleImageRepository).deleteAllByArticle(article);
            doNothing().when(articleRepository).delete(article);
        }
        assertDoesNotThrow(() -> articleService.delete("articleId", admin));
        {
            verify(articleImageRepository).deleteAllByArticle(article);
            verify(articleRepository).delete(article);
        }
    }

    @DisplayName("delete() - article not found")
    @Test
    void delete_articleNotFound() {

        final var admin = new Admin();

        {
            when(articleRepository.findById("not found")).thenReturn(Optional.empty());
        }

        final var responseStatusException = assertThrows(
                ResponseStatusException.class,
                () -> articleService.delete("not found", admin)
        );
        assertEquals(HttpStatus.NOT_FOUND, responseStatusException.getStatusCode());
        assertEquals("article with id 'not found' is not found", responseStatusException.getReason());
    }

    @DisplayName("delete() - use another account forbidden")
    @Test
    void delete_useAnotherAccount() {

        final var admin = new Admin();
        admin.setUsername("admin");

        final var article = new Article();
        article.setAdmin(admin);

        {
            when(articleRepository.findById("articleId")).thenReturn(Optional.of(article));
        }

        final var anotherAdmin = new Admin();
        admin.setUsername("another_admin");

        final var responseStatusException = assertThrows(
                ResponseStatusException.class,
                () -> articleService.delete("articleId", anotherAdmin)
        );
        assertEquals(HttpStatus.FORBIDDEN, responseStatusException.getStatusCode());
        assertEquals("this article belongs to someone else", responseStatusException.getReason());
    }

    @Test
    void getAll() {
        final var article = new Article();
        article.setId("id");
        article.setTitle("title");
        article.setTeaser("teaser");

        final var articles = List.of(article);

        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("createdAt")));

        {
            when(articleRepository.findAll(pageable))
                    .thenReturn(
                            new PageImpl<>(
                                    articles,
                                    PageRequest.of(0, 12),
                                    articles.size()
                            )
                    );
        }

        final var onlyArticleResponsePage = articleService.getAll(0, 12);

        {
            verify(articleRepository).findAll(pageable);
        }

        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).getId());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).getTitle());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).getTeaser());
    }

    @Test
    void search() {
        final var article = new Article();
        article.setId("id");
        article.setTitle("keyword title");
        article.setTeaser("keyword teaser");

        final var articles = List.of(article);

        {
            when(articleRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .then(invocation -> {

                        final var root = mock(Root.class);
                        final var query = mock(CriteriaQuery.class);
                        final var builder = mock(CriteriaBuilder.class);

                        final var join = mock(Join.class);
                        final var predicate = mock(Predicate.class);

                        {
                            when(builder.or(any(), any(), any())).thenReturn(predicate);
                            when(root.join(anyString())).thenReturn(join);
                            when(query.where(any(Predicate.class))).thenReturn(query);
                            when(query.getRestriction()).thenReturn(predicate);
                        }

                        final var specification = (Specification<Article>) invocation.getArgument(0);

                        specification.toPredicate(root, query, builder);

                        {
                            verify(builder).or(any(), any(), any());
                            verify(builder, times(3)).like(any(), anyString());
                            verify(root, times(2)).get(anyString());
                            verify(root).join(anyString());
                            verify(join).get(anyString());

                        }

                        return new PageImpl<>(
                                articles,
                                PageRequest.of(0, 12),
                                articles.size()
                        );
                    });
        }

        final var onlyArticleResponsePage = articleService.search("keyword", 0, 12);

        {
            verify(articleRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).getId());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).getTitle());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).getTeaser());

    }

    @Test
    void getArticlesByTag() {

        final var article = new Article();
        article.setId("id");
        article.setTitle("title");
        article.setTeaser("teaser");

        final var articles = List.of(article);

        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("createdAt")));

        {
            when(articleRepository.findByTagsId("tagId", pageable))
                    .thenReturn(
                            new PageImpl<>(
                                    articles,
                                    PageRequest.of(0, 12),
                                    articles.size()
                            )
                    );
        }

        final var onlyArticleResponsePage = articleService.getArticlesByTag("tagId", 0, 12);

        {
            verify(articleRepository).findByTagsId("tagId", pageable);
        }

        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).getId());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).getTitle());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).getTeaser());

    }

    @Test
    void getArticlesByAdmin() {

        final var article = new Article();
        article.setId("id");
        article.setTitle("title");
        article.setTeaser("teaser");

        final var admin = new Admin();
        admin.setUsername("username");
        article.setAdmin(admin);

        final var articles = List.of(article);

        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("createdAt")));

        {
            when(articleRepository.findByAdmin_Username("username", pageable))
                    .thenReturn(
                            new PageImpl<>(
                                    articles,
                                    PageRequest.of(0, 12),
                                    articles.size()
                            )
                    );
        }

        final var onlyArticleResponsePage = articleService.getArticlesByAdmin("username", 0, 12);

        {
            verify(articleRepository).findByAdmin_Username("username", pageable);
        }

        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).getId());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).getTitle());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).getTeaser());

    }
}