package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateQuoteRequestTest {

    private final CreateQuoteRequest request = new CreateQuoteRequest("body", "author");

    @Test
    void body() {
        assertEquals("body", request.body());
    }

    @Test
    void author() {
        assertEquals("author", request.author());
    }
}