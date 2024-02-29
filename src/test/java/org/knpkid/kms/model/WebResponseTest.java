package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebResponseTest {

    private final WebResponse<String> response = new WebResponse<>(
            "data", "errors", new PagingResponse(1, 1, 1)
    );

    @Test
    void data() {
        assertEquals("data", response.data());
    }

    @Test
    void errors() {
        assertEquals("errors", response.errors());
    }

    @Test
    void paging() {
        assertEquals(new PagingResponse(1, 1, 1), response.paging());
    }
}