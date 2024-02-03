package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.repository.ArticleImageRepository;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.repository.TagRepository;
import org.knpkid.kms.service.ArticleService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ArticleImageRepository articleImageRepository;

    private final ValidationService validationService;

    @Override
    public ArticleResponse create(CreateArticleRequest request, Admin admin) {
        validationService.validate(request);

        final var tags = Objects.nonNull(request.getTags()) ?
                request.getTags().stream()
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .map(tagName -> {
                            final var tag = new Tag();
                            tag.setId(tagName.replace(' ', '-'));
                            tag.setName(tagName);
                            return tagRepository.save(tag);
                        })
                        .collect(Collectors.toSet()) : null;

        final var article = new Article();
        article.setTitle(request.getTitle());
        article.setBody(request.getBody());
        article.setTeaser(request.getTeaser());
        article.setCoverImage(request.getCoverImage());
        article.setAdmin(admin);
        article.setTags(tags);
        articleRepository.save(article);

        final var images = Objects.nonNull(request.getImages()) ?
                request.getImages().stream()
                        .map(image -> {
                            final var articleImage = new ArticleImage();
                            articleImage.setImage(image);
                            articleImage.setArticle(article);
                            return articleImageRepository.save(articleImage);
                        })
                        .toList() : null;


        article.setImages(images);

        return toArticleResponse(article);
    }

    @Override
    public ArticleResponse get(String articleId) {
        return toArticleResponse(
                articleRepository.findById(articleId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "article with id `" + articleId + "` is not found")
                        )
        );
    }

    private ArticleResponse toArticleResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getBody(),
                article.getTeaser(),
                article.getTags(),
                article.getCoverImage(),
                article.getImages()
        );
    }

}
