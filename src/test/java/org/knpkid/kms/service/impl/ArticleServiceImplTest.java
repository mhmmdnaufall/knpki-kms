package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.repository.ArticleImageRepository;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.repository.TagRepository;
import org.knpkid.kms.service.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Test
    void create() throws IOException {

        final var multipartFile = mock(MultipartFile.class);
        when(multipartFile.getBytes()).thenReturn("image".getBytes());

        // not null tags & images
        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of(multipartFile, multipartFile, multipartFile);
        var coverImage = multipartFile;

        var request = new CreateArticleRequest(
                "title", coverImage, "body",
                "teaser", tags, images
        );

        final var admin = new Admin();
        admin.setUsername("admin");

        {
            doNothing().when(validationService).validate(request);
            when(tagRepository.saveAll(any())).thenReturn(List.of(new Tag()));
            when(articleRepository.save(any())).thenReturn(new Article());
            when(articleImageRepository.saveAll(any())).thenReturn(List.of(new ArticleImage()));
        }

        var articleResponse = articleService.create(request, admin);

        {
            verify(validationService).validate(any());
            verify(tagRepository).saveAll(any());
            verify(articleRepository).save(any());
            verify(articleImageRepository).saveAll(any());
        }

        assertNotNull(articleResponse.getTags());
        assertNotNull(articleResponse.getImages());
        assertEquals("title", articleResponse.getTitle());
        assertEquals("body", articleResponse.getBody());
        assertEquals("teaser", articleResponse.getTeaser());
        assertSame(coverImage.getBytes(), articleResponse.getCoverImage());


        // null tags, images & coverImage
        reset(articleRepository, articleImageRepository, tagRepository, validationService);

        tags = null;
        images = null;
        coverImage = null;

        request = new CreateArticleRequest(
                "title", coverImage, "body",
                "teaser", tags, images
        );

        {
            doNothing().when(validationService).validate(request);
            when(articleRepository.save(any())).thenReturn(new Article());
        }

        articleResponse = articleService.create(request, admin);

        {
            verify(validationService).validate(any());
            verify(tagRepository).saveAll(any());
            verify(articleRepository).save(any());
            verify(articleImageRepository).saveAll(any());
        }

        assertEquals(Collections.emptySet(), articleResponse.getTags());
        assertEquals(Collections.emptyList(), articleResponse.getImages());
        assertEquals("title", articleResponse.getTitle());
        assertEquals("body", articleResponse.getBody());
        assertEquals("teaser", articleResponse.getTeaser());
        assertSame(null, articleResponse.getCoverImage());

        // Multipart.getBytes() error
        when(multipartFile.getBytes()).thenThrow(new IOException());

        images = List.of(multipartFile, multipartFile, multipartFile);
        request = new CreateArticleRequest(
                "title", coverImage, "body",
                "teaser", tags, images
        );

        final var finalRequest = request;
        final var errorResponseException = assertThrows(
                ErrorResponseException.class,
                () -> articleService.create(finalRequest, admin)
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
        assertEquals("article with id `not exist` is not found", exception.getReason());


    }
}