package org.knpkid.kms.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private final Admin admin = new Admin();

    @Test
    void getAuthorities() {
        assertEquals(Collections.emptyList(), admin.getAuthorities());
    }

    @Test
    void isAccountNonExpired() {
        assertTrue(admin.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        assertTrue(admin.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        assertTrue(admin.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        assertTrue(admin.isEnabled());
    }

    @Test
    void usernameGetterSetter() {
        admin.setUsername("usernameGetterSetter");
        assertEquals("usernameGetterSetter", admin.getUsername());
    }

    @Test
    void passwordGetterSetter() {
        admin.setPassword("passwordGetterSetter");
        assertEquals("passwordGetterSetter", admin.getPassword());
    }

    @Test
    void nameGetterSetter() {
        admin.setName("nameGetterSetter");
        assertEquals("nameGetterSetter", admin.getName());
    }

    @Test
    void imageGetterSetter() {
        admin.setImage("imageGetterSetter".getBytes());
        assertArrayEquals("imageGetterSetter".getBytes(), admin.getImage());
    }

    @Test
    void articlesGetterSetter() {
        admin.setArticles(Collections.emptyList());
        assertEquals(Collections.emptyList(), admin.getArticles());
    }

    @Test
    void testEquals() {
        admin.setUsername("username");
        admin.setPassword("password");
        admin.setName("name");
        admin.setImage("image".getBytes());
        admin.setArticles(Collections.emptyList());

        final var adminEqual = new Admin();
        adminEqual.setUsername("username");
        adminEqual.setPassword("password");
        adminEqual.setName("name");
        adminEqual.setImage("image".getBytes());
        adminEqual.setArticles(Collections.emptyList());

        assertEquals(admin, adminEqual);

        final var adminNotEqual = new Admin();
        assertNotEquals(admin, adminNotEqual);


    }

    @Test
    void canEqual() {
        assertTrue(admin.canEqual(new Admin()));
        assertFalse(admin.canEqual(""));
    }

    @Test
    void testHashCode() {
        admin.setUsername("username");
        admin.setPassword("password");
        admin.setName("name");
        admin.setImage("image".getBytes());
        admin.setArticles(Collections.emptyList());
        assertEquals(1721583385, admin.hashCode());
    }

    @Test
    void testToString() {

        admin.setUsername("username");
        admin.setPassword("password");
        admin.setName("name");
        admin.setImage("image".getBytes());
        admin.setArticles(Collections.emptyList());

        final var adminString = "Admin(username=username, password=password, name=name, image=" + Arrays.toString("image".getBytes()) + ", articles=" + Collections.emptyList() + ")";
        assertEquals(adminString, admin.toString());

    }
}