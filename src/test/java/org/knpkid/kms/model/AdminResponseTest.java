package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AdminResponseTest {

    private final AdminResponse response = new AdminResponse("username", "name", "image".getBytes());

    @Test
    void getUsername() {
        assertEquals("username", response.getUsername());
    }

    @Test
    void getName() {
        assertEquals("name", response.getName());
    }

    @Test
    void getImage() {
        assertArrayEquals("image".getBytes(), response.getImage());
    }

    @Test
    void testEquals() {
        final var responseEqual = new AdminResponse("username", "name", "image".getBytes());
        assertEquals(responseEqual, response);

        final var responseNotEqual = new AdminResponse("", "", "".getBytes());
        assertNotEquals(responseNotEqual, response);
    }

    @Test
    void testHashCode() {
        assertEquals(-1202354132, response.hashCode());
    }

    @Test
    void testToString() {
        final var responseString = "AdminResponse(username=username, name=name, image=" + Arrays.toString("image".getBytes()) + ")";
        assertEquals(responseString, response.toString());
    }
}