package org.knpkid.kms.repository;

import org.knpkid.kms.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    List<Tag> findByArticlesEmpty();

}
