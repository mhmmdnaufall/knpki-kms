package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;

import static org.junit.jupiter.api.Assertions.*;

class QuoteResponseTest {

    private final QuoteResponse response = new QuoteResponse(1, "body", new Author(), new Admin());

    @Test
    void id() {
        assertEquals(1, response.id());
    }

    @Test
    void body() {
        assertEquals("body", response.body());
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