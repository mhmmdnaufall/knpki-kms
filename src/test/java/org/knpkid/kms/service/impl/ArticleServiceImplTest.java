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
    private TagRepository tagRepository;

    @Mock
    private ArticleImageRepository articleImageRepository;

    @Mock
    private ValidationService validationService;

    @Test
    void create() {

        // not null tags & images
        var tags = Set.of("tag1", "tag2", "tag3");
        var images = List.of("image1".getBytes(), "image2".getBytes(), "image3".getBytes());
        var coverImage = "coverImage".getBytes();

        var request = new CreateArticleRequest(
                "title", coverImage, "body",
                "teaser", tags, images
        );

        final var admin = new Admin();
        admin.setUsername("admin");

        {
            doNothing().when(validationService).validate(request);
            when(tagRepository.save(any())).thenReturn(new Tag());
            when(articleRepository.save(any())).thenReturn(new Article());
            when(articleImageRepository.save(any())).thenReturn(new ArticleImage());
        }

        var articleResponse = articleService.create(request, admin);

        {
            verify(validationService).validate(any());
            verify(tagRepository, times(tags.size())).save(any());
            verify(articleRepository).save(any());
            verify(articleImageRepository, times(images.size())).save(any());
        }

        assertNotNull(articleResponse.getTags());
        assertNotNull(articleResponse.getImages());
        assertEquals("title", articleResponse.getTitle());
        assertEquals("body", articleResponse.getBody());
        assertEquals("teaser", articleResponse.getTeaser());
        assertSame(coverImage, articleResponse.getCoverImage());

        reset(articleRepository, articleImageRepository, tagRepository, validationService);

        // null tags & images
        tags = null;
        images = null;

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
            verify(tagRepository, times(0)).save(any());
            verify(articleRepository).save(any());
            verify(articleImageRepository, times(0)).save(any());
        }

        assertNull(articleResponse.getTags());
        assertNull(articleResponse.getImages());
        assertEquals("title", articleResponse.getTitle());
        assertEquals("body", articleResponse.getBody());
        assertEquals("teaser", articleResponse.getTeaser());
        assertSame(coverImage, articleResponse.getCoverImage());

    }
}