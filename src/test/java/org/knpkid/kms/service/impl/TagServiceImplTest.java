package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Tag;
import org.knpkid.kms.repository.TagRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Test
    void saveAll() {

        when(tagRepository.saveAll(any())).then(invocation -> {
            final var tagSet = (Set<Tag>) invocation.getArgument(0);

            assertTrue(
                tagSet.stream()
                        .anyMatch(tag -> (tag.getId().equals("tag-1")) && (tag.getName().equals("tag 1")))
            );

            assertTrue(
                    tagSet.stream()
                            .anyMatch(tag -> (tag.getId().equals("tag-2")) && (tag.getName().equals("tag 2")))
            );

            assertTrue(
                    tagSet.stream()
                            .noneMatch(tag -> (tag.getId().equals("tag-3")) && (tag.getName().equals("tag 3")))
            );

            return tagSet.stream().toList();
        });

        final var tagSet = tagService.saveAll(Set.of("tag 1", "tag 2"));

        verify(tagRepository).saveAll(any());

        assertTrue(
                tagSet.stream()
                        .anyMatch(tag -> (tag.getId().equals("tag-1")) && (tag.getName().equals("tag 1")))
        );

        assertTrue(
                tagSet.stream()
                        .anyMatch(tag -> (tag.getId().equals("tag-2")) && (tag.getName().equals("tag 2")))
        );

        assertTrue(
                tagSet.stream()
                        .noneMatch(tag -> (tag.getId().equals("tag-3")) && (tag.getName().equals("tag 3")))
        );

    }

    @Test
    void saveAll_nullParameter() {
        final var tagsSet = tagService.saveAll(null);
        assertEquals(Collections.emptySet(), tagsSet);

        verify(tagRepository, times(0)).saveAll(any());
    }
}