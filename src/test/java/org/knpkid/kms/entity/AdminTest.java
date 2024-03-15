package org.knpkid.kms.entity;

import org.junit.jupiter.api.Test;

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
        final var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        admin.setImage(image);
        assertEquals(image, admin.getImage());
    }

    @Test
    void articlesGetterSetter() {
        admin.setArticles(Collections.emptyList());
        assertEquals(Collections.emptyList(), admin.getArticles());
    }

    @Test
    void testEquals() {
        final var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        admin.setUsername("username");
        admin.setPassword("password");
        admin.setName("name");
        admin.setImage(image);
        admin.setArticles(Collections.emptyList());

        final var image2 = new Image();
        image2.setId("imageId");
        image2.setFormat(ImageFormat.PNG);

        final var adminEqual = new Admin();
        adminEqual.setUsername("username");
        adminEqual.setPassword("password");
        adminEqual.setName("name");
        adminEqual.setImage(image2);
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
        final var admin = new Admin();
        admin.setUsername("username");
        admin.setPassword("password");
        admin.setName("name");
        admin.setArticles(Collections.emptyList());
        assertEquals(-523470833, admin.hashCode());
    }

    @Test
    void testToString() {
        final var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        admin.setUsername("username");
        admin.setPassword("password");
        admin.setName("name");
        admin.setImage(image);
        admin.setArticles(Collections.emptyList());

        final var adminString = "Admin(username=username, password=password, name=name, image=" + image + ", articles=" + Collections.emptyList() + ")";
        assertEquals(adminString, admin.toString());

    }
}