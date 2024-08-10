package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.repository.AuthorRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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
        var existingAuthor = new Author();
        existingAuthor.setId(1);
        existingAuthor.setName("author 1");

        {
            when(authorRepository.findByNameIn(any()))
                    .then(invocation -> {
                        var authorStringModifiableSet = (Set<String>) invocation.getArgument(0);
                        assertTrue(authorStringModifiableSet.contains("author 1"));
                        assertTrue(authorStringModifiableSet.contains("author 2"));
                        return List.of(existingAuthor);
                    });

            when(authorRepository.saveAll(any())).then(invocation -> {
                var authorSet = (Set<Author>) invocation.getArgument(0);

                // authors that have been saved shouldn't need to be saved again
                // only save "unsaved authors"
                assertFalse(authorSet.contains(existingAuthor));
                assertTrue(authorSet.stream().anyMatch(author -> author.getName().equals("author 2")));
                return authorSet.stream().toList();
            });
        }

        var authorSet = authorService.saveAll(Set.of("author 1", "author 2"));

        verify(authorRepository).findByNameIn(any());
        verify(authorRepository).saveAll(any());
        assertEquals(2, authorSet.size());
        assertTrue(authorSet.contains(existingAuthor));
        assertTrue(authorSet.stream().anyMatch(author -> author.getName().equals("author 2")));

    }

    @Test
    void getOrCreateByName_get() {
        var existAuthor = new Author();
        existAuthor.setId(1);
        existAuthor.setName("author");

        when(authorRepository.findByName("author")).thenReturn(Optional.of(existAuthor));
        var author = authorService.getOrCreateByName("author");

        verify(authorRepository).findByName("author");
        verify(authorRepository, times(0)).save(any());
        assertEquals(existAuthor.getId(), author.getId());
        assertEquals(existAuthor.getName(), author.getName());
    }

    @Test
    void getOrCreateByName_create() {
        when(authorRepository.findByName("author")).thenReturn(Optional.empty());
        when(authorRepository.save(any())).then(invocation -> {
            var author = (Author) invocation.getArgument(0);
            assertEquals("author", author.getName());
            return author;
        });
        var author = authorService.getOrCreateByName("author");

        verify(authorRepository).findByName("author");
        verify(authorRepository).save(any());
        assertEquals("author", author.getName());
    }

    @Test
    void get() {
        var author = new Author();
        author.setId(1);
        author.setName("author");

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        var authorResponse = authorService.get(1);

        verify(authorRepository).findById(1);
        assertEquals(author.getId(), authorResponse.id());
        assertEquals(author.getName(), authorResponse.name());
        assertEquals(Collections.emptyList(), authorResponse.articles());
        assertEquals(Collections.emptyList(), authorResponse.quotes());
    }

    @Test
    void get_authorNotFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        var error = assertThrows(ResponseStatusException.class, () -> authorService.get(1));
        verify(authorRepository).findById(1);
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertEquals("author with id '1' is not found", error.getReason());

    }
}