package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateQuoteRequestTest {

    private final UpdateQuoteRequest request = new UpdateQuoteRequest("body", "author");

    @Test
    void body() {
        assertEquals("body", request.body());
    }

    @Test
    void author() {
        assertEquals("author", request.author());
    }
}