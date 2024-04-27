package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.model.CreateQuoteRequest;
import org.knpkid.kms.model.QuoteResponse;
import org.knpkid.kms.service.QuoteService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
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

        {
            when(quoteService.create(request, admin)).
                    thenReturn(new QuoteResponse(
                            1, "body", author, admin
                    ));
        }

        final var response = quoteController.create(request, admin);
        assertEquals(1, response.id());
        assertEquals("body", response.body());
        assertEquals(author, response.author());
        assertEquals(admin, response.admin());

    }
}