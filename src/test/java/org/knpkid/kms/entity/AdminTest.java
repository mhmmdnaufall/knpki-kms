package org.knpkid.kms.entity;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private final Admin ADMIN = new Admin();

    @Test
    void getAuthorities() {
        assertEquals(Collections.emptyList(), ADMIN.getAuthorities());
    }

    @Test
    void isAccountNonExpired() {
        assertTrue(ADMIN.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        assertTrue(ADMIN.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        assertTrue(ADMIN.isCredentialsNonExpired());
    }

    @Test
    void isEnabled() {
        assertTrue(ADMIN.isEnabled());
    }

    @Test
    void usernameGetterSetter() {
        ADMIN.setUsername("usernameGetterSetter");
        assertEquals("usernameGetterSetter", ADMIN.getUsername());
    }

    @Test
    void passwordGetterSetter() {
        ADMIN.setPassword("passwordGetterSetter");
        assertEquals("passwordGetterSetter", ADMIN.getPassword());
    }

    @Test
    void nameGetterSetter() {
        ADMIN.setName("nameGetterSetter");
        assertEquals("nameGetterSetter", ADMIN.getName());
    }

    @Test
    void imageGetterSetter() {
        final var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        ADMIN.setImage(image);
        assertEquals(image, ADMIN.getImage());
    }

    @Test
    void articlesGetterSetter() {
        ADMIN.setArticles(Collections.emptyList());
        assertEquals(Collections.emptyList(), ADMIN.getArticles());
    }

    @Test
    void quotesGetterSetter() {
        ADMIN.setQuotes(Collections.emptyList());
        assertEquals(Collections.emptyList(), ADMIN.getQuotes());
    }

    @Test
    void testEquals() {
        final var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        ADMIN.setUsername("username");
        ADMIN.setPassword("password");
        ADMIN.setName("name");
        ADMIN.setImage(image);
        ADMIN.setArticles(Collections.emptyList());

        final var image2 = new Image();
        image2.setId("imageId");
        image2.setFormat(ImageFormat.PNG);

        final var adminEqual = new Admin();
        adminEqual.setUsername("username");
        adminEqual.setPassword("password");
        adminEqual.setName("name");
        adminEqual.setImage(image2);
        adminEqual.setArticles(Collections.emptyList());

        assertEquals(ADMIN, adminEqual);

        ADMIN.setUsername(null);
        adminEqual.setUsername(null);
        assertEquals(ADMIN, adminEqual);

        ADMIN.setPassword(null);
        adminEqual.setPassword(null);
        assertEquals(ADMIN, adminEqual);

        ADMIN.setName(null);
        adminEqual.setName(null);
        assertEquals(ADMIN, adminEqual);

        ADMIN.setImage(null);
        adminEqual.setImage(null);
        assertEquals(ADMIN, adminEqual);

        ADMIN.setArticles(null);
        adminEqual.setArticles(null);
        assertEquals(ADMIN, adminEqual);

        final var adminNotEqual = new Admin();
        adminNotEqual.setName("unequal");
        assertNotEquals(ADMIN, adminNotEqual);


    }

    @Test
    void canEqual() {
        assertTrue(ADMIN.canEqual(new Admin()));
        assertFalse(ADMIN.canEqual(""));
    }

    @Test
    void testHashCode() {
        final var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        final var admin = new Admin();
        assertEquals(hashCodeCalculate(admin), admin.hashCode());

        admin.setUsername("username");
        assertEquals(hashCodeCalculate(admin), admin.hashCode());

        admin.setPassword("password");
        assertEquals(hashCodeCalculate(admin), admin.hashCode());

        admin.setName("name");
        assertEquals(hashCodeCalculate(admin), admin.hashCode());

        admin.setImage(image);
        assertEquals(hashCodeCalculate(admin), admin.hashCode());
    }

    @Test
    void testToString() {
        final var image = new Image();
        image.setId("imageId");
        image.setFormat(ImageFormat.PNG);

        ADMIN.setUsername("username");
        ADMIN.setPassword("password");
        ADMIN.setName("name");
        ADMIN.setImage(image);
        ADMIN.setArticles(Collections.emptyList());
        ADMIN.setQuotes(Collections.emptyList());

        final var adminString = "Admin(username=username, password=password, name=name, image=" + image + ", articles=" + Collections.emptyList() + ", quotes=" + Collections.emptyList() + ")";
        assertEquals(adminString, ADMIN.toString());

    }

    private int hashCodeCalculate(Admin admin) {
        final var usernameHashCode = admin.getUsername() == null ? 43 : admin.getUsername().hashCode();
        final var passwordHashCode = admin.getPassword() == null ? 43 : admin.getPassword().hashCode();
        final var nameHashCode = admin.getName() == null ? 43 : admin.getName().hashCode();
        final var imageHashCode = admin.getImage() == null ? 43 : admin.getImage().hashCode();

        var result = 1;
        result = result * 59 + usernameHashCode;
        result = result * 59 + passwordHashCode;
        result = result * 59 + nameHashCode;
        result = result * 59 + imageHashCode;

        return result;
    }
}