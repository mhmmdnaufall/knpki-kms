package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArticleResponseTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    private static final Image IMAGE = new Image();

    private static final ArticleResponse RESPONSE = new ArticleResponse(
            1, "title", NOW, NOW, "body", "teaser",
            Set.of(new Tag()), new Admin(), Set.of(new Author()), IMAGE, List.of(IMAGE), new Archive()
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
    void tags() {
        assertEquals(1, RESPONSE.tags().size());
    }

    @Test
    void admin() {
        assertEquals(new Admin(), RESPONSE.admin());
    }

    @Test
    void authors() {
        assertEquals(1, RESPONSE.authors().size());
        assertTrue(RESPONSE.authors().contains(new Author()));
    }

    @Test
    void coverImage() {
        assertEquals(IMAGE, RESPONSE.coverImage());
    }

    @Test
    void images() {
        assertEquals(1, RESPONSE.images().size());
    }

    @Test
    void archive() {
        assertEquals(new Archive(), RESPONSE.archive());
    }

}