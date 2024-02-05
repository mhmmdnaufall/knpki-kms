package org.knpkid.kms.service;

import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;
import org.knpkid.kms.model.UpdateArticleRequest;

public interface ArticleService {

    ArticleResponse create(CreateArticleRequest request, Admin admin);

    ArticleResponse get(String articleId);

    ArticleResponse update(String articleId, UpdateArticleRequest request, Admin admin);
}
