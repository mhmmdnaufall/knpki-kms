package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.repository.AuthorRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Test
    void saveAll() {
        final var existingAuthor = new Author();
        existingAuthor.setId(1);
        existingAuthor.setName("author 1");

        {
            when(authorRepository.findByNameIn(any()))
                    .then(invocation -> {
                        final var authorStringModifiableSet = (Set<String>) invocation.getArgument(0);
                        assertTrue(authorStringModifiableSet.contains("author 1"));
                        assertTrue(authorStringModifiableSet.contains("author 2"));
                        return List.of(existingAuthor);
                    });

            when(authorRepository.saveAll(any())).then(invocation -> {
                final var authorSet = (Set<Author>) invocation.getArgument(0);

                // authors that have been saved shouldn't need to be saved again
                // only save "unsaved authors"
                assertFalse(authorSet.contains(existingAuthor));
                assertTrue(authorSet.stream().anyMatch(author -> author.getName().equals("author 2")));
                return authorSet.stream().toList();
            });
        }

        final var authorSet = authorService.saveAll(Set.of("author 1", "author 2"));

        verify(authorRepository).findByNameIn(any());
        verify(authorRepository).saveAll(any());
        assertEquals(2, authorSet.size());
        assertTrue(authorSet.contains(existingAuthor));
        assertTrue(authorSet.stream().anyMatch(author -> author.getName().equals("author 2")));

    }

    @Test
    void getOrCreateByName_get() {
        final var existAuthor = new Author();
        existAuthor.setId(1);
        existAuthor.setName("author");

        when(authorRepository.findByName("author")).thenReturn(Optional.of(existAuthor));
        final var author = authorService.getOrCreateByName("author");

        verify(authorRepository).findByName("author");
        verify(authorRepository, times(0)).save(any());
        assertEquals(existAuthor.getId(), author.getId());
        assertEquals(existAuthor.getName(), author.getName());
    }

    @Test
    void ngetOrCreateByName_create() {
        when(authorRepository.findByName("author")).thenReturn(Optional.empty());
        when(authorRepository.save(any())).then(invocation -> {
            final var author = (Author) invocation.getArgument(0);
            assertEquals("author", author.getName());
            return author;
        });
        final var author = authorService.getOrCreateByName("author");

        verify(authorRepository).findByName("author");
        verify(authorRepository).save(any());
        assertEquals("author", author.getName());
    }
}