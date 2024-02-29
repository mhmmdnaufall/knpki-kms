package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RegisterAdminRequestTest {

    private final RegisterAdminRequest request = new RegisterAdminRequest(
            "username", "password", "name", new MockMultipartFile("image.png", "image".getBytes())
    );

    @Test
    void username() {
        assertEquals("username", request.username());
    }

    @Test
    void password() {
        assertEquals("password", request.password());
    }

    @Test
    void name() {
        assertEquals("name", request.name());
    }

    @Test
    void image() throws IOException {
        assertArrayEquals("image".getBytes(), request.image().getBytes());
        assertEquals("image.png", request.image().getName());
    }
}