package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.entity.Quote;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.UpdateQuoteRequest;
import org.knpkid.kms.repository.QuoteRepository;
import org.knpkid.kms.service.AuthorService;
import org.knpkid.kms.service.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceImplTest {

    @InjectMocks
    private QuoteServiceImpl quoteService;

    @Mock
    private QuoteRepository quoteRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private ValidationService validationService;

    @Test
    void create() {
        var request = new CreateQuoteRequest("body", "author");
        var author = new Author();
        author.setId(1);
        author.setName("author");

        var admin = new Admin();
        admin.setUsername("naufal");

        {
            doNothing().when(validationService).validate(request);
            when(authorService.getOrCreateByName(request.author())).thenReturn(author);
            when(quoteRepository.save(any())).then(invocation -> {
                var quote = (Quote) invocation.getArgument(0);
                assertEquals(request.body(), quote.getBody());
                assertEquals(author, quote.getAuthor());
                assertEquals(admin, quote.getAdmin());
                return quote;
            });
        }

        var response = quoteService.create(request, admin);

        verify(validationService).validate(request);
        verify(authorService).getOrCreateByName(request.author());
        verify(quoteRepository).save(any());
        assertEquals(request.body(), response.body());
        assertEquals(admin, response.admin());
        assertEquals(author, response.author());
    }

    @Test
    void delete_success() {
        var admin = new Admin();
        admin.setUsername("admin");

        var quote = new Quote();
        quote.setId(1);
        quote.setBody("body");
        quote.setAdmin(admin);

        {
            when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
            doNothing().when(quoteRepository).delete(quote);
        }

        quoteService.delete(1, admin);
        verify(quoteRepository).findById(1);
        verify(quoteRepository).delete(quote);
    }

    @Test
    void delete_idNotFound() {
        when(quoteRepository.findById(1)).thenReturn(Optional.empty());

        var admin = new Admin();
        var error = assertThrows(ResponseStatusException.class, () -> quoteService.delete(1, admin));
        assertEquals("quote with id '1' is not found", error.getReason());
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
    }

    @Test
    void delete_adminForbidden() {
        var admin = new Admin();
        admin.setUsername("admin");

        var quote = new Quote();
        quote.setAdmin(new Admin()); // different admin

        {
            when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
        }

        var error = assertThrows(ResponseStatusException.class, () -> quoteService.delete(1, admin));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        assertEquals("this quote belongs to someone else", error.getReason());
    }

    @Test
    void update() {
        var request = new UpdateQuoteRequest("body update", "author update");
        var admin = new Admin();
        admin.setUsername("username");

        var author = new Author();
        author.setName("author");

        var quote = new Quote();
        quote.setId(1);
        quote.setBody("body");

        var now = LocalDateTime.now();
        quote.setCreatedAt(now.minusDays(1));
        quote.setUpdatedAt(now.minusDays(1));
        quote.setAuthor(author);
        quote.setAdmin(admin);

        {
            when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
            when(authorService.getOrCreateByName(request.author())).then(invocation -> {
                var newAuthor = new Author();
                newAuthor.setName(invocation.getArgument(0));
                return newAuthor;
            });
            when(quoteRepository.save(any())).then(invocation -> {
                var updatedQuote = (Quote) invocation.getArgument(0);
                assertNotNull(updatedQuote);
                assertEquals(now.minusDays(1), updatedQuote.getCreatedAt());
                assertNotEquals(now, updatedQuote.getUpdatedAt());
                assertEquals(request.body(), updatedQuote.getBody());
                assertEquals(request.author(), updatedQuote.getAuthor().getName());
                return updatedQuote;
            });
        }

        var response = quoteService.update(1, request, admin);

        verify(quoteRepository).findById(1);
        verify(authorService).getOrCreateByName(request.author());
        verify(quoteRepository).save(any());
        assertEquals(request.body(), response.body());
        assertEquals(request.author(), response.author().getName());
    }

    @Test
    void update_idNotFound() {
        when(quoteRepository.findById(1)).thenReturn(Optional.empty());

        var request = new UpdateQuoteRequest("body", "author");
        var admin = new Admin();
        var error = assertThrows(
                ResponseStatusException.class,
                () -> quoteService.update(1, request, admin)
        );
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertEquals("quote with id '1' is not found", error.getReason());
    }

    @Test
    void update_adminForbidden() {
        var admin = new Admin();
        admin.setUsername("admin");

        var quote = new Quote();
        quote.setAdmin(new Admin()); // different admin

        {
            when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));
        }

        var request = new UpdateQuoteRequest("body", "author");
        var error = assertThrows(ResponseStatusException.class, () -> quoteService.update(1, request, admin));
        assertEquals(HttpStatus.FORBIDDEN, error.getStatusCode());
        assertEquals("this quote belongs to someone else", error.getReason());
    }

    @Test
    void getAll() {
        var quote = new Quote();
        quote.setId(1);
        quote.setBody("body");

        var quoteList = List.of(quote);

        var pageable = PageRequest.of(0, 12, Sort.by(Sort.Order.desc("updatedAt")));
        {
            when(quoteRepository.findAll(pageable))
                    .thenReturn(
                            new PageImpl<>(
                                    quoteList,
                                    PageRequest.of(0, 12),
                                    quoteList.size()
                            )
                    );
        }

        var quoteResponsePage = quoteService.getAll(0, 12);

        verify(quoteRepository).findAll(pageable);
        assertEquals(quoteList.size(), quoteResponsePage.getContent().size());
        assertEquals(quote.getId(), quoteResponsePage.getContent().get(0).id());
    }

    @Test
    void get() {
        var quote = new Quote();
        quote.setId(1);
        quote.setBody("body");

        when(quoteRepository.findById(1)).thenReturn(Optional.of(quote));

        var quoteResponse = quoteService.get(1);

        verify(quoteRepository).findById(1);
        assertEquals(quote.getId(), quoteResponse.id());
        assertEquals(quote.getBody(), quoteResponse.body());
    }

    @Test
    void get_notFound() {

        when(quoteRepository.findById(1)).thenReturn(Optional.empty());

        var error = assertThrows(ResponseStatusException.class, () -> quoteService.get(1));

        verify(quoteRepository).findById(1);
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertEquals("quote with id '1' is not found", error.getReason());
    }
}