package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.*;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.service.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
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

        // 2 imageGallery + 1 coverImage = 3 imageService.save
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