package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Image;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OnlyArticleResponseTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private static final Image IMAGE = new Image();

    private static final OnlyArticleResponse RESPONSE = new OnlyArticleResponse(
            1, "title", NOW, NOW,
            "body", "teaser", new Image()
    );

    @Test
    void id() {
        assertEquals(1, RESPONSE.id());
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
    void coverImage() {
        assertEquals(IMAGE, RESPONSE.coverImage());
    }
}