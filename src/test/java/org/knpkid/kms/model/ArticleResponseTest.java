package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Image;
import org.knpkid.kms.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArticleResponseTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private static final Image IMAGE = new Image();

    private static final ArticleResponse RESPONSE = new ArticleResponse(
            "id", "title", NOW, NOW, "body", "teaser",
            Set.of(new Tag()), new Admin(), IMAGE, List.of(IMAGE)
    );

    @Test
    void id() {
        assertEquals("id", RESPONSE.id());
    }

    @Test
    void title() {
        assertEquals("title", RESPONSE.title());
    }

    @Test
    void createdAt() {
        assertEquals(NOW, RESPONSE.createdAt());
    }

    @Test
    void updatedAt() {
        assertEquals(NOW, RESPONSE.updatedAt());
    }

    @Test
    void body() {
        assertEquals("body", RESPONSE.body());
    }

    @Test
    void teaser() {
        assertEquals("teaser", RESPONSE.teaser());
    }

    @Test
    void tags() {
        assertEquals(1, RESPONSE.tags().size());
    }

    @Test
    void admin() {
        assertEquals(new Admin(), RESPONSE.admin());
    }

    @Test
    void coverImage() {
        assertEquals(IMAGE, RESPONSE.coverImage());
    }

    @Test
    void getImages() {
        assertEquals(1, RESPONSE.images().size());
    }
}