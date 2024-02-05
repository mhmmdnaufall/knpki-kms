package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.UpdateArticleRequest;
import org.knpkid.kms.repository.ArticleImageRepository;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.repository.TagRepository;
import org.knpkid.kms.service.ArticleService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ArticleImageRepository articleImageRepository;

    private final ValidationService validationService;

    @Transactional
    @SneakyThrows
    @Override
    public ArticleResponse create(CreateArticleRequest request, Admin admin) {
        validationService.validate(request);

        final var tags = Optional.ofNullable(request.tags())
                .map(stringSet -> stringSet.stream()
                        .map(tag -> tag.replaceAll("[^a-zA-Z0-9 ]", ""))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .map(tagName -> {
                            final var tag = new Tag();
                            tag.setId(tagName.replace(' ', '-'));
                            tag.setName(tagName);
                            return tag;
                        })
                        .collect(Collectors.toSet())
                )
                .orElse(Collections.emptySet());
        tagRepository.saveAll(tags);

        final var article = new Article();
        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setCoverImage(Objects.nonNull(request.coverImage()) ? request.coverImage().getBytes() : null);
        article.setAdmin(admin);
        article.setTags(tags);
        articleRepository.save(article);

        final var images = Optional.ofNullable(request.images())
                .map(multipartList -> multipartList.stream()
                        .map(image -> {
                            final var articleImage = new ArticleImage();
                            try {
                                articleImage.setImage(image.getBytes());
                            } catch (IOException e) {
                                throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
                            }
                            articleImage.setArticle(article);
                            return articleImage;
                        })
                        .toList()
                )
                .orElse(Collections.emptyList());
        articleImageRepository.saveAll(images);

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

    @Transactional
    @SneakyThrows
    @Override
    public ArticleResponse update(String articleId, UpdateArticleRequest request, Admin admin) {
        final var article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "article with id `" + articleId + "` is not found")
                );

        if (!admin.equals(article.getAdmin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this article belongs to someone else");
        }

        validationService.validate(request);

        final var tags = Optional.ofNullable(request.tags())
                .map(stringSet -> stringSet.stream()
                        .map(tag -> tag.replaceAll("[^a-zA-Z0-9 ]", ""))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .map(tagName -> {
                            final var tag = new Tag();
                            tag.setId(tagName.replace(' ', '-'));
                            tag.setName(tagName);
                            return tag;
                        })
                        .collect(Collectors.toSet())
                )
                .orElse(Collections.emptySet());
        tagRepository.saveAll(tags);

        articleImageRepository.deleteAllByArticle(article);
        final var images = Optional.ofNullable(request.images())
                .map(multipartList -> multipartList.stream()
                        .map(image -> {
                            final var articleImage = new ArticleImage();
                            try {
                                articleImage.setImage(image.getBytes());
                            } catch (IOException e) {
                                throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
                            }
                            articleImage.setArticle(article);
                            return articleImage;
                        })
                        .toList()
                )
                .orElse(Collections.emptyList());
        articleImageRepository.saveAll(images);

        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setUpdatedAt(LocalDateTime.now());
        article.setTags(tags);
        article.setCoverImage(Objects.nonNull(request.coverImage()) ? request.coverImage().getBytes() : null);

        articleRepository.save(article);

        article.setImages(images);

        return toArticleResponse(article);
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
