package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.model.AuthorResponse;
import org.knpkid.kms.repository.AuthorRepository;
import org.knpkid.kms.service.AuthorService;
import org.knpkid.kms.utility.ConvertToModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        var authorStringModifiableSet = new HashSet<>(authorsString);
        var existingAuthorModifiableList = new ArrayList<>(authorRepository.findByNameIn(authorStringModifiableSet));

        // ignoring every existing author
        existingAuthorModifiableList.forEach(existingAuthor ->
                authorStringModifiableSet.remove(existingAuthor.getName())
        );

        var authorsSet = authorStringModifiableSet.stream()
                .map(authorName -> {
                    var author = new Author();
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
                    var newAuthor = new Author();
                    newAuthor.setName(authorName);
                    return authorRepository.save(newAuthor);
                });
    }

    @Override
    public AuthorResponse get(int authorId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "author with id '" + authorId + "' is not found"
                ));
        return ConvertToModel.authorResponse(author);
    }

}
