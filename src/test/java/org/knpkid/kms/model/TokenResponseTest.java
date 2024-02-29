package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseTest {

    private final TokenResponse response = new TokenResponse("token");

    @Test
    void token() {
        assertEquals("token", response.token());
    }
}