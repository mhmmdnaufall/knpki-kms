package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.PagingResponse;
import org.knpkid.kms.model.QuoteResponse;
import org.knpkid.kms.model.UpdateQuoteRequest;
import org.knpkid.kms.service.QuoteService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {

    @InjectMocks
    private QuoteController quoteController;

    @Mock
    private QuoteService quoteService;

    @Test
    void create() {
        final var request = new CreateQuoteRequest("body", "author");

        final var author = new Author();
        author.setName("author");

        final var admin = new Admin();
        admin.setUsername("admin");

        final var now = LocalDateTime.now();
        {
            when(quoteService.create(request, admin)).
                    thenReturn(new QuoteResponse(
                            1, "body", now, now, author, admin
                    ));
        }

        final var webResponseQuoteResponse = quoteController.create(request, admin);
        assertEquals(1, webResponseQuoteResponse.data().id());
        assertEquals("body", webResponseQuoteResponse.data().body());
        assertEquals(now, webResponseQuoteResponse.data().createdAt());
        assertEquals(now, webResponseQuoteResponse.data().updatedAt());
        assertEquals(author, webResponseQuoteResponse.data().author());
        assertEquals(admin, webResponseQuoteResponse.data().admin());

    }

    @Test
    void delete() {
        final var admin = new Admin();
        doNothing().when(quoteService).delete(1, admin);
        quoteController.delete(1, admin);
        verify(quoteService).delete(1, admin);
    }

    @Test
    void update() {
        final var author = new Author();
        author.setName("author");

        final var admin = new Admin();
        admin.setUsername("admin");

        final var request = new UpdateQuoteRequest("body", author.getName());
        final var now = LocalDateTime.now();

        when(quoteService.update(1, request, admin))
                .thenReturn(new QuoteResponse(1, request.body(), now, now, author, admin));

        final var webResponseQuoteResponse = quoteController.update(1, request, admin);

        verify(quoteService).update(1, request, admin);
        assertEquals("author", webResponseQuoteResponse.data().author().getName());
        assertEquals("admin", webResponseQuoteResponse.data().admin().getUsername());
        assertEquals("body", webResponseQuoteResponse.data().body());
        assertEquals(now, webResponseQuoteResponse.data().createdAt());
        assertEquals(now, webResponseQuoteResponse.data().updatedAt());
        assertEquals(1, webResponseQuoteResponse.data().id());
    }

    @Test
    void getAll() {
        final var now = LocalDateTime.now();
        final var author = new Author();
        author.setId(1);
        author.setName("author");

        final var admin = new Admin();
        admin.setUsername("admin");
        final var quoteResponseList = List.of(new QuoteResponse(1, "body", now, now, author, admin));

        {
            when(quoteService.getAll(0, 12))
                    .thenReturn(
                            new PageImpl<>(
                                    quoteResponseList,
                                    PageRequest.of(0, 12),
                                    quoteResponseList.size()
                            )
                    );
        }

        final var webResponse = quoteController.getAll(0, 12);

        verify(quoteService).getAll(0, 12);
        assertEquals(quoteResponseList, webResponse.data());
        assertNull(webResponse.errors());
        assertEquals(new PagingResponse(0, 1, 12), webResponse.paging());
    }
}