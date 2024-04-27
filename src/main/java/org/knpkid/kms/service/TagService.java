package org.knpkid.kms.service;

import org.knpkid.kms.entity.Tag;

import java.util.Set;

public interface TagService {

    Set<Tag> saveAll(Set<String> tags);

}
