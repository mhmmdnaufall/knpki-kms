package org.knpkid.kms.repository;

import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, String> {

    @Transactional
    void deleteAllByArticle(Article article);

}
