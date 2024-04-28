package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.repository.AuthorRepository;
import org.knpkid.kms.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Set<Author> saveAll(Set<String> authorsString) {
        final var existingAuthorList = authorRepository.findByNameIn(authorsString);

        // ignoring every existing author
        existingAuthorList.forEach(existingAuthor ->
                authorsString.remove(existingAuthor.getName())
        );

        final var authorsSet = authorsString.stream()
                .map(authorName -> {
                    final var author = new Author();
                    author.setName(authorName);
                    return author;
                })
                .collect(Collectors.toUnmodifiableSet());

        authorRepository.saveAll(authorsSet);

        // merge all author
        existingAuthorList.addAll(authorsSet);
        return existingAuthorList.stream().collect(Collectors.toUnmodifiableSet());
    }

}
