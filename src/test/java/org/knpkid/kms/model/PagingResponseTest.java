package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagingResponseTest {

    private final PagingResponse response = new PagingResponse(1, 1, 1);

    @Test
    void currentPage() {
        assertEquals(1, response.currentPage());
    }

    @Test
    void totalPage() {
        assertEquals(1, response.totalPage());
    }

    @Test
    void size() {
        assertEquals(1, response.size());
    }
}