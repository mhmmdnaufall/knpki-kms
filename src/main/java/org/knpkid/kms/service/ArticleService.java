package org.knpkid.kms.service;

import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.OnlyArticleResponse;
import org.knpkid.kms.model.UpdateArticleRequest;
import org.springframework.data.domain.Page;

public interface ArticleService {

    ArticleResponse create(CreateArticleRequest request, Admin admin);

    ArticleResponse get(Integer articleId);

    ArticleResponse update(Integer articleId, UpdateArticleRequest request, Admin admin);

    void delete(Integer articleId, Admin admin);

    Page<OnlyArticleResponse> getAll(Integer page, Integer size);

    Page<OnlyArticleResponse> search(String keyword, Integer page, Integer size);

    Page<OnlyArticleResponse> getArticlesByTag(String tagId, Integer page, Integer size);

    Page<OnlyArticleResponse> getArticlesByAdmin(String username, Integer page, Integer size);

}
