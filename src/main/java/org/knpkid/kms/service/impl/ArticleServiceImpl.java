package org.knpkid.kms.service.impl;

import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Image;
import org.knpkid.kms.service.ImageService;
import org.springframework.data.domain.*;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.OnlyArticleResponse;
import org.knpkid.kms.model.UpdateArticleRequest;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.repository.TagRepository;
import org.knpkid.kms.service.ArticleService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ImageService imageService;

    private final ValidationService validationService;

    private static final String UPDATED_AT = "updatedAt";

    @Transactional
    @Override
    public ArticleResponse create(CreateArticleRequest request, Admin admin) {
        validationService.validate(request);

        final var article = new Article();
        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setAdmin(admin);
        article.setTags(extractAndSaveTags(request.tags()));
        setArticleCoverImageAndGallery(article, request.coverImage(), request.images());

        articleRepository.save(article);

        log.info("article created with id = '{}'", article.getId());

        return toArticleResponse(article);
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleResponse get(String articleId) {
        return toArticleResponse(getArticleById(articleId));
    }

    @Transactional
    @Override
    public ArticleResponse update(String articleId, UpdateArticleRequest request, Admin admin) {
        final var article = getArticleById(articleId);

        checkArticleAuthor(article, admin);
        validationService.validate(request);

        final var oldCoverImage = article.getCoverImage();
        final var oldArticleImageGallery = article.getImageGallery();

        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setUpdatedAt(LocalDateTime.now());
        article.setTags(extractAndSaveTags(request.tags()));
        setArticleCoverImageAndGallery(article, request.coverImage(), request.images());

        articleRepository.save(article);

        deleteArticleCoverImageAndGallery(oldCoverImage, oldArticleImageGallery);

        log.info("article with id '{}' has been updated", articleId);

        return toArticleResponse(article);
    }

    @Transactional
    @Override
    public void delete(String articleId, Admin admin) {
        final var article = getArticleById(articleId);
        checkArticleAuthor(article, admin);

        articleRepository.delete(article);

        deleteArticleCoverImageAndGallery(article.getCoverImage(), article.getImageGallery());

        log.info("article with id '{}' has been deleted", article.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getAll(Integer page, Integer size) {
        return articleRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT))))
                .map(this::toOnlyArticleResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> search(String keyword, Integer page, Integer size) {
        final var specification = (Specification<Article>) (root, query, builder) -> {
            final var trimmedKeyword = keyword.trim();
            return query.where(builder.or(
                    builder.like(root.get("title"), "%" + trimmedKeyword + "%"),
                    builder.like(root.get("teaser"), "%" + trimmedKeyword + "%"),
                    builder.like(
                            root.join("tags", JoinType.LEFT).get("id"),
                            "%" + trimmedKeyword.replace(' ', '-') + "%"
                    )
            )).getRestriction();
        };

        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT)));

        final var articlesPage = articleRepository.findAll(specification, pageable);

        final var onlyArticleResponses = articlesPage.getContent().stream()
                .map(this::toOnlyArticleResponse)
                .toList();

        return new PageImpl<>(onlyArticleResponses, pageable, articlesPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getArticlesByTag(String tagId, Integer page, Integer size) {
        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT)));
        final var articlesPage = articleRepository.findByTagsId(tagId, pageable);
        final var onlyArticleResponses = articlesPage.getContent().stream()
                .map(this::toOnlyArticleResponse)
                .toList();
        return new PageImpl<>(onlyArticleResponses, pageable, articlesPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getArticlesByAdmin(String username, Integer page, Integer size) {
        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT)));
        final var articlesPage = articleRepository.findByAdmin_Username(username, pageable);
        final var onlyArticleResponses = articlesPage.getContent().stream()
                .map(this::toOnlyArticleResponse)
                .toList();
        return new PageImpl<>(onlyArticleResponses, pageable, articlesPage.getTotalElements());
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
                article.getAdmin(),
                article.getCoverImage(),
                article.getImageGallery()
        );
    }

    private OnlyArticleResponse toOnlyArticleResponse(Article article) {
        return new OnlyArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getBody(),
                article.getTeaser(),
                article.getCoverImage()
        );
    }

    private Article getArticleById(String articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "article with id '" + articleId + "' is not found")
                );
    }

    private void checkArticleAuthor(Article article, Admin admin) {
        if (!admin.equals(article.getAdmin())) {
            log.warn("'{}' tries to modify '{}' article", admin.getUsername(), article.getAdmin().getUsername());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this article belongs to someone else");
        }
    }

    private Set<Tag> extractAndSaveTags(Set<String> tagsString) {
        if (tagsString == null) {
            return Collections.emptySet();
        }

        final var tags = tagsString.stream()
                .map(this::createTag)
                .collect(Collectors.toSet());

        tagRepository.saveAll(tags);

        return tags;
    }

    private Tag createTag(String tagName) {
        final var cleanTagName = tagName.replaceAll("[^a-zA-Z0-9 ]", "").trim().toLowerCase();

        final var tag = new Tag();
        tag.setId(cleanTagName.replace(' ', '-'));
        tag.setName(cleanTagName);
        return tag;

    }

    private void setArticleCoverImageAndGallery(Article article, MultipartFile coverImage, List<MultipartFile> images) {
        article.setCoverImage(null);
        article.setImageGallery(new ArrayList<>());

        if (coverImage != null)
            article.setCoverImage(imageService.save(coverImage));

        Optional.ofNullable(images)
                .ifPresent(it -> {
                    for (var image : it) {
                        article.getImageGallery().add(imageService.save(image));
                    }
                });
    }

    private void deleteArticleCoverImageAndGallery(Image coverImage, List<Image> articleImageGallery) {
        Optional.ofNullable(coverImage)
                .ifPresent(imageService::delete);

        Optional.ofNullable(articleImageGallery)
                .ifPresent(imageService::deleteAll);

    }

}