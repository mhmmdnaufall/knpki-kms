package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Image;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AdminResponseTest {
    private final AdminResponse response = new AdminResponse("username", "name", null);

    @Test
    void username() {
        assertEquals("username", response.username());
    }

    @Test
    void name() {
        assertEquals("name", response.name());
    }

    @Test
    void imageId() {
        assertNull(response.image());
    }

}