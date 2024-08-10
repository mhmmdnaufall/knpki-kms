package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.repository.TagRepository;
import org.knpkid.kms.service.TagService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public Set<Tag> saveAll(Set<String> tagsString) {
        if (tagsString == null) {
            return Collections.emptySet();
        }

        var tags = tagsString.stream()
                .map(this::createTag)
                .collect(Collectors.toSet());

        tagRepository.saveAll(tags);

        return tags;
    }

    private Tag createTag(String tagName) {
        var cleanTagName = tagName.replaceAll("[^a-zA-Z0-9 ]", "").trim().toLowerCase();

        var tag = new Tag();
        tag.setId(cleanTagName.replace(' ', '-'));
        tag.setName(cleanTagName);
        return tag;
    }

}
