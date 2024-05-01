package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.repository.AuthorRepository;
import org.knpkid.kms.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Set<Author> saveAll(Set<String> authorsString) {
        final var authorStringModifiableSet = new HashSet<>(authorsString);
        final var existingAuthorModifiableList = new ArrayList<>(authorRepository.findByNameIn(authorStringModifiableSet));

        // ignoring every existing author
        existingAuthorModifiableList.forEach(existingAuthor ->
                authorStringModifiableSet.remove(existingAuthor.getName())
        );

        final var authorsSet = authorStringModifiableSet.stream()
                .map(authorName -> {
                    final var author = new Author();
                    author.setName(authorName);
                    return author;
                })
                .collect(Collectors.toUnmodifiableSet());

        authorRepository.saveAll(authorsSet);

        // merge all author
        existingAuthorModifiableList.addAll(authorsSet);
        return existingAuthorModifiableList.stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Author getOrCreateByName(String authorName) {
        return authorRepository.findByName(authorName)
                .orElseGet(() -> {
                    final var newAuthor = new Author();
                    newAuthor.setName(authorName);
                    return authorRepository.save(newAuthor);
                });
    }
}
