package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.OnlyArticleResponse;
import org.knpkid.kms.model.UpdateArticleRequest;
import org.knpkid.kms.repository.ArticleImageRepository;
import org.knpkid.kms.repository.ArticleRepository;
import org.knpkid.kms.repository.TagRepository;
import org.knpkid.kms.service.ArticleService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final TagRepository tagRepository;

    private final ArticleImageRepository articleImageRepository;

    private final ValidationService validationService;

    private static final String CREATED_AT = "createdAt";

    @Transactional
    @SneakyThrows
    @Override
    public String create(CreateArticleRequest request, Admin admin) {
        validationService.validate(request);

        final var article = new Article();
        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setCoverImage(Objects.nonNull(request.coverImage()) ? request.coverImage().getBytes() : null);
        article.setAdmin(admin);
        article.setTags(extractAndSaveTags(request.tags()));
        articleRepository.save(article);
        article.setImages(extractAndSaveArticleImages(request.images(), article));

        log.info("article created with id = '{}'", article.getId());

        return article.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleResponse get(String articleId) {
        return toArticleResponse(getArticleById(articleId));
    }

    @Transactional
    @SneakyThrows
    @Override
    public ArticleResponse update(String articleId, UpdateArticleRequest request, Admin admin) {
        final var article = getArticleById(articleId);

        checkArticleAuthor(article, admin);
        validationService.validate(request);
        articleImageRepository.deleteAllByArticle(article);

        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setUpdatedAt(LocalDateTime.now());
        article.setTags(extractAndSaveTags(request.tags()));
        article.setCoverImage(Objects.nonNull(request.coverImage()) ? request.coverImage().getBytes() : null);
        article.setImages(extractAndSaveArticleImages(request.images(), article));
        articleRepository.save(article);

        log.info("article with id '{}' has been updated", articleId);

        return toArticleResponse(article);
    }

    @Transactional
    @Override
    public void delete(String articleId, Admin admin) {
        final var article = getArticleById(articleId);
        checkArticleAuthor(article, admin);
        articleImageRepository.deleteAllByArticle(article);
        articleRepository.delete(article);

        log.info("article with id '{}' has been deleted", article.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getAll(Integer page, Integer size) {
        return articleRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc(CREATED_AT))))
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
                            root.join("tags").get("id"),
                            "%" + trimmedKeyword.replace(' ', '-') + "%"
                    )
            )).getRestriction();
        };

        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(CREATED_AT)));

        final var articlesPage = articleRepository.findAll(specification, pageable);

        final var onlyArticleResponses = articlesPage.getContent().stream()
                .map(this::toOnlyArticleResponse)
                .toList();

        return new PageImpl<>(onlyArticleResponses, pageable, articlesPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getArticlesByTag(String tagId, Integer page, Integer size) {
        final var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(CREATED_AT)));
        final var articlesPage = articleRepository.findByTagsId(tagId, pageable);
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
                article.getImages()
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
                        HttpStatus.NOT_FOUND, "article with id `" + articleId + "` is not found")
                );
    }

    private void checkArticleAuthor(Article article, Admin admin) {
        if (!admin.equals(article.getAdmin())) {
            log.warn("'{}' tries to modify '{}' article", admin.getUsername(), article.getAdmin().getUsername());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this article belongs to someone else");
        }
    }

    private List<ArticleImage> extractAndSaveArticleImages(List<MultipartFile> multipartFileImages, Article article) {
        if (multipartFileImages == null) {
            return Collections.emptyList();
        }

        final var articleImages = multipartFileImages.stream()
                .map(image -> createArticleImage(image, article))
                .toList();

        return articleImageRepository.saveAll(articleImages);
    }

    private ArticleImage createArticleImage(MultipartFile multipartFileImage, Article article) {
        final var articleImage = new ArticleImage();
        try {
            articleImage.setImage(multipartFileImage.getBytes());
        } catch (IOException e) {
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST);
        }
        articleImage.setArticle(article);
        return articleImage;

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
        final var cleanedTagName = tagName.replaceAll("[^a-zA-Z0-9 ]", "").trim().toLowerCase();

        final var tag = new Tag();
        tag.setId(cleanedTagName.replace(' ', '-'));
        tag.setName(cleanedTagName);
        return tag;

    }

}
