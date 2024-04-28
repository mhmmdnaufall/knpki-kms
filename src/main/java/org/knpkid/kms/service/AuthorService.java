package org.knpkid.kms.service;

import org.knpkid.kms.entity.Author;

import java.util.Set;

public interface AuthorService {

    Set<Author> saveAll(Set<String> authors);

}
