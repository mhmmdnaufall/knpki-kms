package org.knpkid.kms.service;

import org.knpkid.kms.entity.Author;
import org.knpkid.kms.model.AuthorResponse;

import java.util.List;
import java.util.Set;

public interface AuthorService {

    Set<Author> saveAll(Set<String> authors);

    Author getOrCreateByName(String authorName);

    AuthorResponse get(int authorId);

}
