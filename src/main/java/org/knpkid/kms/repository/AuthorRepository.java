package org.knpkid.kms.repository;

import org.knpkid.kms.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByName(String name);

    List<Author> findByNameIn(Set<String> authorsString);

    List<Author> findByArticlesEmptyAndQuotesEmpty();
}
