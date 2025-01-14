package org.knpkid.kms.service.impl;

import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Image;
import org.knpkid.kms.service.*;
import org.knpkid.kms.utility.ConvertToModel;
import org.springframework.data.domain.*;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.OnlyArticleResponse;
import org.knpkid.kms.model.UpdateArticleRequest;
import org.knpkid.kms.repository.ArticleRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.knpkid.kms.Constant.UPDATED_AT;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final TagService tagService;

    private final ImageService imageService;

    private final AuthorService authorService;

    private final ArchiveService archiveService;

    private final ValidationService validationService;

    @Transactional
    @Override
    public ArticleResponse create(CreateArticleRequest request, Admin admin) {
        validationService.validate(request);

        var article = new Article();
        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setAdmin(admin);
        article.setTags(tagService.saveAll(request.tags()));
        setArticleCoverImageAndGallery(article, request.coverImage(), request.images());
        article.setAuthors(authorService.saveAll(request.authors()));
        article.setArchive(!Objects.isNull(request.archive()) ? archiveService.save(request.archive()) : null);

        articleRepository.save(article);

        log.info("article created with id = '{}'", article.getId());

        return ConvertToModel.articleResponse(article);
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleResponse get(Integer articleId) {
        return ConvertToModel.articleResponse(getArticleById(articleId));
    }

    @Transactional
    @Override
    public ArticleResponse update(Integer articleId, UpdateArticleRequest request, Admin admin) {
        var article = getArticleById(articleId);

        checkArticleAdmin(article, admin);
        validationService.validate(request);

        // for deletion
        var oldCoverImage = article.getCoverImage();
        var oldArticleImageGallery = article.getImageGallery();
        var oldArchive = article.getArchive();

        article.setTitle(request.title());
        article.setBody(request.body());
        article.setTeaser(request.teaser());
        article.setUpdatedAt(LocalDateTime.now());
        article.setTags(tagService.saveAll(request.tags()));
        setArticleCoverImageAndGallery(article, request.coverImage(), request.images());
        article.setAuthors(new HashSet<>(authorService.saveAll(request.authors())));

        article.setArchive(!Objects.isNull(request.archive()) ? archiveService.save(request.archive()) : null);

        articleRepository.save(article);

        deleteArticleCoverImageAndGallery(oldCoverImage, oldArticleImageGallery); // delete old image
        if (oldArchive != null) archiveService.delete(oldArchive); // delete old archive

        log.info("article with id '{}' has been updated", articleId);

        return ConvertToModel.articleResponse(article);
    }

    @Transactional
    @Override
    public void delete(Integer articleId, Admin admin) {
        var article = getArticleById(articleId);
        checkArticleAdmin(article, admin);

        articleRepository.delete(article);
        if (article.getArchive() != null) archiveService.delete(article.getArchive());
        deleteArticleCoverImageAndGallery(article.getCoverImage(), article.getImageGallery());

        log.info("article with id '{}' has been deleted", article.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getAll(Integer page, Integer size) {
        return articleRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT))))
                .map(ConvertToModel::onlyArticleResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> search(String keyword, Integer page, Integer size) {
        var specification = (Specification<Article>) (root, query, builder) -> {
            var trimmedKeyword = keyword.trim();
            return query.where(builder.or(
                    builder.like(root.get("title"), "%" + trimmedKeyword + "%"),
                    builder.like(root.get("teaser"), "%" + trimmedKeyword + "%"),
                    builder.like(
                            root.join("tags", JoinType.LEFT).get("id"),
                            "%" + trimmedKeyword.replace(' ', '-') + "%"
                    ),
                    builder.like(
                            root.join("authors", JoinType.LEFT).get("name"),
                            "%" + trimmedKeyword + "%"
                    )
            )).getRestriction();
        };

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT)));

        var articlesPage = articleRepository.findAll(specification, pageable);

        var onlyArticleResponses = articlesPage.getContent().stream()
                .map(ConvertToModel::onlyArticleResponse)
                .toList();

        return new PageImpl<>(onlyArticleResponses, pageable, articlesPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getArticlesByTag(String tagId, Integer page, Integer size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT)));
        var articlesPage = articleRepository.findByTagsId(tagId, pageable);
        var onlyArticleResponses = articlesPage.getContent().stream()
                .map(ConvertToModel::onlyArticleResponse)
                .toList();
        return new PageImpl<>(onlyArticleResponses, pageable, articlesPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<OnlyArticleResponse> getArticlesByAdmin(String username, Integer page, Integer size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(UPDATED_AT)));
        var articlesPage = articleRepository.findByAdmin_Username(username, pageable);
        var onlyArticleResponses = articlesPage.getContent().stream()
                .map(ConvertToModel::onlyArticleResponse)
                .toList();
        return new PageImpl<>(onlyArticleResponses, pageable, articlesPage.getTotalElements());
    }

    private Article getArticleById(Integer articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "article with id '" + articleId + "' is not found")
                );
    }

    private void checkArticleAdmin(Article article, Admin admin) {
        if (!admin.equals(article.getAdmin())) {
            log.warn("'{}' tries to modify '{}' article", admin.getUsername(), article.getAdmin().getUsername());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this article belongs to someone else");
        }
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