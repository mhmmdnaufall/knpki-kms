package org.knpkid.kms.service;

import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.CreateArticleRequest;

public interface ArticleService {

    ArticleResponse create(CreateArticleRequest request, Admin admin);

}
