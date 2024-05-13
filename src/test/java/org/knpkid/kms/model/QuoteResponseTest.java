package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuoteResponseTest {

    private static final LocalDateTime now = LocalDateTime.now();

    private final QuoteResponse response = new QuoteResponse(1, "body", now, now, new Author(), new Admin());

    @Test
    void id() {
        assertEquals(1, response.id());
    }

    @Test
    void body() {
        assertEquals("body", response.body());
    }

    @Test
    void createdAt() {
        assertEquals(now, response.createdAt());
    }

    @Test
    void updatedAt() {
        assertEquals(now, response.updatedAt());
    }

    @Test
    void author() {
        assertEquals(new Author(), response.author());
    }

    @Test
    void admin() {
        assertEquals(new Admin(), response.admin());
    }
}