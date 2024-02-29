package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginAdminRequestTest {

    private final LoginAdminRequest request = new LoginAdminRequest("username", "password");

    @Test
    void username() {
        assertEquals("username", request.username());
    }

    @Test
    void password() {
        assertEquals("password", request.password());
    }
}