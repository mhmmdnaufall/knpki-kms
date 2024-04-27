package org.knpkid.kms.repository;

import org.knpkid.kms.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    Page<Article> findByTagsId(String tagId, Pageable pageable);

    Page<Article> findByAdmin_Username(String username, Pageable pageable);

}
