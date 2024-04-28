package org.knpkid.kms.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.repository.AuthorRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorJobTest {

    @InjectMocks
    private AuthorJob authorJob;

    @Mock
    private AuthorRepository authorRepository;

    @Test
    void removeAbandonedAuthor() {
        final var author = new Author();
        author.setId(1);
        {
            when(authorRepository.findByArticlesEmptyAndQuotesEmpty())
                    .thenReturn(List.of(author));

            doNothing().when(authorRepository).deleteAll(List.of(author));
        }

        authorJob.removeAbandonedAuthor();

        verify(authorRepository).deleteAll(List.of(author));
    }


    @Test
    void removeAbandonedAuthor_emptyAuthors() {
        {
            when(authorRepository.findByArticlesEmptyAndQuotesEmpty())
                    .thenReturn(Collections.emptyList());
        }

        authorJob.removeAbandonedAuthor();

        verify(authorRepository, times(0)).deleteAll(any());
    }
}