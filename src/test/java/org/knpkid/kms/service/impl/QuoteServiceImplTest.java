package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.entity.Quote;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.repository.AuthorRepository;
import org.knpkid.kms.repository.QuoteRepository;
import org.knpkid.kms.service.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceImplTest {

    @InjectMocks
    private QuoteServiceImpl quoteService;

    @Mock
    private ValidationService validationService;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Test
    void create() {
        final var admin = new Admin();
        admin.setUsername("admin");

        final var author = new Author();
        author.setId(1);
        author.setName("author");

        final var request = new CreateQuoteRequest("body", "author");

        {
            doNothing().when(validationService).validate(request);
            when(authorRepository.findByName("author")).thenReturn(Optional.of(author));

            final var quote = new Quote();
            quote.setId(1);
            quote.setAuthor(author);
            quote.setAdmin(admin);
            quote.setBody(request.body());

            when(quoteRepository.save(any())).thenReturn(quote);
        }

        final var response = quoteService.create(request, admin);
        assertEquals(1, response.id());
        assertEquals("body", response.body());
        assertEquals(author, response.author());
        assertEquals(admin, response.admin());

    }

    @Test
    void create_withNewAuthor() {
        final var admin = new Admin();
        admin.setUsername("admin");

        final var author = new Author();
        author.setId(1);
        author.setName("author");

        final var request = new CreateQuoteRequest("body", "author");

        {
            doNothing().when(validationService).validate(request);
            when(authorRepository.findByName("author")).thenReturn(Optional.empty());
            when(authorRepository.save(any())).thenReturn(author);

            final var quote = new Quote();
            quote.setId(1);
            quote.setAuthor(author);
            quote.setAdmin(admin);
            quote.setBody(request.body());

            when(quoteRepository.save(any())).thenReturn(quote);
        }

        final var response = quoteService.create(request, admin);
        assertEquals(1, response.id());
        assertEquals("body", response.body());
        assertEquals(author, response.author());
        assertEquals(admin, response.admin());
    }
}