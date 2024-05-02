package org.knpkid.kms.service.impl;

import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.*;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.UpdateArticleRequest;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.service.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private TagService tagService;

    @Mock
    private ImageService imageService;

    @Mock
    private AuthorService authorService;

    @Mock
    private ArchiveService archiveService;

    @Mock
    private ValidationService validationService;

    @Test
    void create() {
        final var admin = getAdmin();
        final var coverImageRequest = getCoverImageRequest();
        final var tagsRequest = getTagsRequest();
        final var imagesRequest = getImagesRequest();
        final var authorsRequest = getAuthorsRequest();
        final var archiveRequest = getArchiveRequest();

        final var request = new CreateArticleRequest(
                "title",
                coverImageRequest,
                "body",
                "teaser",
                tagsRequest,
                imagesRequest,
                authorsRequest,
                archiveRequest
        );

        {
            doNothing().when(validationService).validate(request);
            when(articleRepository.save(any())).then(invocation -> {
                final var article = (Article) invocation.getArgument(0);
                article.setId(1);
                return article;
            });
            when(tagService.saveAll(tagsRequest)).thenReturn(getTags());
            when(authorService.saveAll(authorsRequest)).thenReturn(getAuthors());
            when(archiveService.save(archiveRequest)).thenReturn(getArchive());
            when(imageService.save(any())).thenReturn(new Image());
            when(imageService.save(coverImageRequest)).thenReturn(getCoverImage());
        }

        final var response = articleService.create(request, admin);

        // 2 imageGallery + 1 coverImage = 3 imageService.save
        verify(imageService, times(3)).save(any());
        verify(validationService).validate(request);
        verify(tagService).saveAll(tagsRequest);
        verify(authorService).saveAll(authorsRequest);
        verify(archiveService).save(archiveRequest);
        assertEquals(1, response.id());
        assertEquals("title", response.title());
        assertEquals("body", response.body());
        assertEquals("teaser", response.teaser());
        assertEquals(getArchive(), response.archive());
        assertEquals(getAuthors(), response.authors());
        assertEquals(getCoverImage(), response.coverImage());
        assertEquals(2, response.images().size());
        assertEquals(getTags(), response.tags());

    }

    @DisplayName("create() - Set Nullable field to null")
    @Test
    void create_null() {
        final var admin = getAdmin();
        final var authorsRequest = getAuthorsRequest();

        final var request = new CreateArticleRequest(
                "title",
                null,
                "body",
                null,
                null,
                null,
                authorsRequest,
                null
        );

        {
            doNothing().when(validationService).validate(request);
            when(articleRepository.save(any())).then(invocation -> {
                final var article = (Article) invocation.getArgument(0);
                article.setId(1);
                return article;
            });
            when(tagService.saveAll(null)).thenReturn(Collections.emptySet());
            when(authorService.saveAll(authorsRequest)).thenReturn(getAuthors());
        }

        final var response = articleService.create(request, admin);

        verify(imageService, times(0)).save(any());
        verify(validationService).validate(request);
        verify(tagService).saveAll(null);
        verify(authorService).saveAll(authorsRequest);
        verify(archiveService, times(0)).save(any());
        assertEquals(1, response.id());
        assertEquals("title", response.title());
        assertEquals("body", response.body());
        assertNull(response.teaser());
        assertNull(response.archive());
        assertEquals(getAuthors(), response.authors());
        assertNull(response.coverImage());
        assertEquals(Collections.emptyList(), response.images());
        assertEquals(Collections.emptySet(), response.tags());

    }

    @DisplayName("update() - success")
    @Test
    void update() {
        final var now = LocalDateTime.now();

        final var admin = getAdmin();
        final var coverImageRequest = getCoverImageRequest();
        final var tagsRequest = getTagsRequest();
        final var imagesRequest = getImagesRequest();
        final var authorsRequest = getAuthorsRequest();
        final var archiveRequest = getArchiveRequest();

        final var oldCoverImage = new Image();
        final var oldArticleImageGallery = List.of(new Image());
        final var oldArchive = new Archive();
        final var oldAuthors = Set.of(new Author());

        final var article = new Article();
        article.setId(1);
        article.setAdmin(admin);
        article.setCoverImage(oldCoverImage);
        article.setImageGallery(oldArticleImageGallery);
        article.setAuthors(Set.of(new Author()));
        article.setArchive(oldArchive);
        article.setAuthors(oldAuthors);

        final var request = new UpdateArticleRequest(
                "title",
                coverImageRequest,
                "body",
                "teaser",
                tagsRequest,
                imagesRequest,
                authorsRequest,
                archiveRequest
        );

        {
            doNothing().when(validationService).validate(request);
            when(articleRepository.findById(1)).thenReturn(Optional.of(article));
            when(tagService.saveAll(tagsRequest)).thenReturn(getTags());
            when(imageService.save(any())).thenReturn(new Image());
            when(imageService.save(coverImageRequest)).thenReturn(getCoverImage());
            when(authorService.saveAll(authorsRequest)).thenReturn(getAuthors());
            when(archiveService.save(archiveRequest)).thenReturn(getArchive());
            when(articleRepository.save(any())).then(invocation -> {
                final var saveArticle = (Article) invocation.getArgument(0);
                saveArticle.setUpdatedAt(now);
                return saveArticle;
            });
            doNothing().when(archiveService).delete(oldArchive);
        }

        final var response = assertDoesNotThrow(() -> articleService.update(1, request, admin));

        verify(validationService).validate(request);
        verify(articleRepository).findById(1);
        verify(tagService).saveAll(tagsRequest);
        verify(authorService).saveAll(authorsRequest);
        verify(archiveService).save(archiveRequest);
        verify(articleRepository).save(any());
        verify(archiveService).delete(oldArchive);
        verify(imageService).delete(oldCoverImage);
        verify(imageService).deleteAll(oldArticleImageGallery);


        // 2 imageGallery + 1 coverImage = 3 imageService.save
        verify(imageService, times(3)).save(any());

        assertEquals(1, response.id());
        assertEquals("title", response.title());
        assertEquals("body", response.body());
        assertEquals("teaser", response.teaser());
        assertEquals(now, response.updatedAt());
        assertEquals(getArchive(), response.archive());
        assertEquals(getAuthors(), response.authors());
        assertEquals(getCoverImage(), response.coverImage());
        assertEquals(2, response.images().size());
        assertEquals(getTags(), response.tags());
        assertNotEquals(response.archive(), oldArchive);
        assertNotEquals(response.images(), oldArticleImageGallery);
        assertNotEquals(response.coverImage(), oldCoverImage);
        assertNotEquals(oldAuthors, response.authors());
    }

    @DisplayName("update() - Set nullable fields to null. Including old field")
    @Test
    void update_null() {
        final var now = LocalDateTime.now();

        final var admin = getAdmin();
        final var authorsRequest = getAuthorsRequest();

        final var oldAuthors = Set.of(new Author());

        final var article = new Article();
        article.setId(1);
        article.setAdmin(admin);
        article.setAuthors(oldAuthors);

        final var request = new UpdateArticleRequest(
                "title",
                null,
                "body",
                null,
                null,
                null,
                authorsRequest,
                null
        );

        {
            doNothing().when(validationService).validate(request);
            when(articleRepository.findById(1)).thenReturn(Optional.of(article));
            when(tagService.saveAll(null)).thenReturn(Collections.emptySet());
            when(authorService.saveAll(authorsRequest)).thenReturn(getAuthors());
            when(articleRepository.save(any())).then(invocation -> {
                final var saveArticle = (Article) invocation.getArgument(0);
                saveArticle.setUpdatedAt(now);
                return saveArticle;
            });
        }

        final var response = assertDoesNotThrow(() -> articleService.update(1, request, admin));

        verify(validationService).validate(request);
        verify(articleRepository).findById(1);
        verify(tagService).saveAll(null);
        verify(authorService).saveAll(authorsRequest);
        verify(archiveService, times(0)).save(any());
        verify(articleRepository).save(any());
        verify(archiveService, times(0)).delete(any());
        verify(imageService, times(0)).delete(any());
        verify(imageService, times(0)).deleteAll(any());
        verify(imageService, times(0)).save(any());

        assertEquals(1, response.id());
        assertEquals("title", response.title());
        assertEquals("body", response.body());
        assertNull(response.teaser());
        assertEquals(now, response.updatedAt());
        assertEquals(getAuthors(), response.authors());
        assertEquals(0, response.images().size());
        assertEquals(Collections.emptySet(), response.tags());
        assertNull(response.archive());
        assertEquals(Collections.emptyList(), response.images());
        assertNull(response.coverImage());
    }

    @DisplayName("update() - use Another Account forbidden")
    @Test
    void update_useAnotherAccount() {
        final var request = new UpdateArticleRequest(
                "title", null, "body",
                "teaser", null, null, Set.of("author"), null
        );

        final var article = new Article();
        article.setAdmin(getAdmin());

        final var diffAdmin = new Admin();
        diffAdmin.setUsername("differentAdmin");

        {
            when(articleRepository.findById(741237903)).thenReturn(Optional.of(article));
        }

        final var exception = assertThrows(
                ResponseStatusException.class,
                () -> articleService.update(741237903, request, diffAdmin)
        );
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("this article belongs to someone else", exception.getReason());
    }

    @DisplayName("update() - article not found")
    @Test
    void update_articleNotFound() {
        when(articleRepository.findById(1)).thenReturn(Optional.empty());

        final var request = new UpdateArticleRequest(
                "title", null, "body",
                "teaser", null, null, Set.of("author"), null
        );

        final var admin = new Admin();
        final var exception = assertThrows(
                ResponseStatusException.class,
                () -> articleService.update(1, request, admin)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("article with id '%d' is not found".formatted(1), exception.getReason());
    }

    @Test
    void get() {
        final var existId = 2131231;
        final var notExistId = 123021309;
        final var article = new Article();
        article.setId(existId);

        {
            when(articleRepository.findById(existId)).thenReturn(Optional.of(article));
            when(articleRepository.findById(notExistId)).thenReturn(Optional.empty());
        }

        // exist
        var articleResponse = assertDoesNotThrow(() -> articleService.get(existId));
        assertEquals(article.getId(), articleResponse.id());

        // not exist
        final var exception = assertThrows(ResponseStatusException.class, () -> articleService.get(notExistId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("article with id '%d' is not found".formatted(notExistId), exception.getReason());


    }

    @DisplayName("delete() - success")
    @Test
    void delete() {

        final var coverImage = getCoverImage();
        final var images = List.of(new Image(), new Image());
        final var archive = getArchive();
        final var admin = getAdmin();

        final var article = new Article();
        article.setAdmin(admin);
        article.setCoverImage(coverImage);
        article.setImageGallery(images);
        article.setArchive(archive);

        {
            when(articleRepository.findById(123812038)).thenReturn(Optional.of(article));
            doNothing().when(articleRepository).delete(article);
            doNothing().when(imageService).delete(any());
            doNothing().when(imageService).deleteAll(any());
            doNothing().when(archiveService).delete(archive);
        }

        assertDoesNotThrow(() -> articleService.delete(123812038, admin));

        {
            verify(articleRepository).findById(123812038);
            verify(articleRepository).delete(article);
            verify(imageService).delete(any());
            verify(imageService).deleteAll(any());
            verify(archiveService).delete(archive);
        }

    }

    @DisplayName("delete() - null image, imageGallery, archive")
    @Test
    void delete_nullFiles() {

        final var admin = getAdmin();

        final var article = new Article();
        article.setAdmin(admin);

        {
            when(articleRepository.findById(13123123)).thenReturn(Optional.of(article));
            doNothing().when(articleRepository).delete(article);
        }

        assertDoesNotThrow(() -> articleService.delete(13123123, admin));

        {
            verify(articleRepository).findById(13123123);
            verify(articleRepository).delete(article);
            verify(archiveService, times(0)).delete(any());
            verify(imageService, times(0)).delete(any());
            verify(imageService, times(0)).deleteAll(any());
        }

    }

    @DisplayName("delete() - article not found")
    @Test
    void delete_articleNotFound() {

        final var admin = new Admin();

        {
            when(articleRepository.findById(1)).thenReturn(Optional.empty());
        }

        final var exception = assertThrows(
                ResponseStatusException.class,
                () -> articleService.delete(1, admin)
        );

        {
            verify(articleRepository, times(0)).delete(any(Article.class));
            verify(imageService, times(0)).delete(any());
            verify(imageService, times(0)).deleteAll(any());
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
            assertEquals("article with id '%s' is not found".formatted(1L), exception.getReason());
        }

    }

    @DisplayName("delete() - use another account forbidden")
    @Test
    void delete_useAnotherAccount() {

        final var admin = new Admin();
        admin.setUsername("admin");

        final var article = new Article();
        article.setAdmin(admin);

        final var diffAdmin = new Admin();
        diffAdmin.setUsername("different_admin");

        {
            when(articleRepository.findById(1)).thenReturn(Optional.of(article));
        }

        final var exception = assertThrows(
                ResponseStatusException.class,
                () -> articleService.delete(1, diffAdmin)
        );

        {
            verify(articleRepository, times(0)).delete(any(Article.class));
            verify(imageService, times(0)).delete(any());
            verify(imageService, times(0)).deleteAll(any());
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
            assertEquals("this article belongs to someone else", exception.getReason());
        }

    }

    @Test
    void getAll() {
        final var article = new Article();
        article.setId(1);
        article.setTitle("title");
        article.setTeaser("teaser");

        final var articles = List.of(article);

        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("updatedAt")));

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

        verify(articleRepository).findAll(pageable);
        assertEquals(articles.size(), onlyArticleResponsePage.getContent().size());
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());
    }

    @Test
    void search() {
        final var article = new Article();
        article.setId(1);
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
                            when(root.join("tags", JoinType.LEFT)).thenReturn(join);
                            when(query.where(any(Predicate.class))).thenReturn(query);
                            when(query.getRestriction()).thenReturn(predicate);
                        }

                        final var specification = (Specification<Article>) invocation.getArgument(0);

                        specification.toPredicate(root, query, builder);

                        {
                            verify(builder).or(any(), any(), any());
                            verify(builder, times(3)).like(any(), anyString());
                            verify(root, times(2)).get(anyString());
                            verify(root).join("tags", JoinType.LEFT);
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
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());

    }

    @Test
    void getArticlesByTag() {

        final var article = new Article();
        article.setId(1);
        article.setTitle("title");
        article.setTeaser("teaser");

        final var articles = List.of(article);

        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("updatedAt")));

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
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());

    }

    @Test
    void getArticlesByAdmin() {

        final var article = new Article();
        article.setId(1);
        article.setTitle("title");
        article.setTeaser("teaser");

        final var admin = new Admin();
        admin.setUsername("username");
        article.setAdmin(admin);

        final var articles = List.of(article);

        final var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("updatedAt")));

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
        assertEquals(article.getId(), onlyArticleResponsePage.getContent().get(0).id());
        assertEquals(article.getTitle(), onlyArticleResponsePage.getContent().get(0).title());
        assertEquals(article.getTeaser(), onlyArticleResponsePage.getContent().get(0).teaser());

    }

    private Admin getAdmin() {
        final var admin = new Admin();
        admin.setUsername("admin");
        return admin;
    }

    private MultipartFile getCoverImageRequest() {
        return mock(MultipartFile.class);
    }

    private Image getCoverImage() {
        final var image = new Image();
        image.setId("coverImage");
        image.setFormat(ImageFormat.PNG);
        return image;
    }

    private Set<String> getTagsRequest() {
        return Set.of("tag 1", "tag 2");
    }

    private Set<Tag> getTags() {
        final var tag1 = new Tag();
        tag1.setId("tag-1");
        tag1.setName("tag 1");

        final var tag2 = new Tag();
        tag1.setId("tag-2");
        tag1.setName("tag 2");

        return Set.of(tag1, tag2);
    }

    private List<MultipartFile> getImagesRequest() {
        return List.of(mock(MultipartFile.class), mock(MultipartFile.class));
    }

    private Set<String> getAuthorsRequest() {
        return Set.of("author 1", "author 2");
    }

    private Set<Author> getAuthors() {
        final var author1 = new Author();
        author1.setId(1);
        author1.setName("author 1");

        final var author2 = new Author();
        author2.setId(2);
        author2.setName("author 1");

        return Set.of(author1, author2);
    }

    private MultipartFile getArchiveRequest() {
        return mock(MultipartFile.class);
    }

    private Archive getArchive() {
        final var archive = new Archive();
        archive.setId("archive");
        archive.setFormat(ArchiveFormat.PDF);
        return archive;
    }

}