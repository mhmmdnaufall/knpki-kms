package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArticleResponseTest {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2024, 2, 29, 17, 20);

    private final ArticleResponse response = new ArticleResponse(
            "id", "title", LOCAL_DATE_TIME, LOCAL_DATE_TIME, "body", "teaser",
            Set.of(new Tag()), new Admin(), "coverImage".getBytes(), List.of(new ArticleImage(), new ArticleImage())
    );

    @Test
    void getId() {
        assertEquals("id", response.getId());
    }

    @Test
    void getTitle() {
        assertEquals("title", response.getTitle());
    }

    @Test
    void getCreatedAt() {
        assertEquals(LOCAL_DATE_TIME, response.getCreatedAt());
    }

    @Test
    void getUpdatedAt() {
        assertEquals(LOCAL_DATE_TIME, response.getUpdatedAt());
    }

    @Test
    void getBody() {
        assertEquals("body", response.getBody());
    }

    @Test
    void getTeaser() {
        assertEquals("teaser", response.getTeaser());
    }

    @Test
    void getTags() {
        assertEquals(1, response.getTags().size());
    }

    @Test
    void getAdmin() {
        assertEquals(new Admin(), response.getAdmin());
    }

    @Test
    void getCoverImage() {
        assertArrayEquals("coverImage".getBytes(), response.getCoverImage());
    }

    @Test
    void getImages() {
        assertEquals(2, response.getImages().size());
    }

    @Test
    void testEquals() {
        final var responseEqual = new ArticleResponse(
                "id", "title", LOCAL_DATE_TIME, LOCAL_DATE_TIME, "body", "teaser",
                Set.of(new Tag()), new Admin(), "coverImage".getBytes(), List.of(new ArticleImage(), new ArticleImage())
        );
        assertEquals(response, responseEqual);

        final var responseNotEqual = new ArticleResponse(
                "notEqual", "notEqual", LOCAL_DATE_TIME, LOCAL_DATE_TIME, "notEqual", "notEqual",
                Set.of(new Tag()), new Admin(), "notEqual".getBytes(), List.of(new ArticleImage(), new ArticleImage())
        );
        assertNotEquals(response, responseNotEqual);
    }

    @Test
    void testHashCode() {
        assertEquals(886054528, response.hashCode());
    }

    @Test
    void testToString() {
        final var responseString = "ArticleResponse(id=id, title=title, createdAt=" + LOCAL_DATE_TIME + ", updatedAt=" + LOCAL_DATE_TIME + ", body=body, teaser=teaser, tags=[Tag(id=null, name=null, articles=null)], admin=Admin(username=null, password=null, name=null, image=null, articles=null), coverImage=" + Arrays.toString("coverImage".getBytes()) + ", images=[ArticleImage(id=null, image=null, article=null), ArticleImage(id=null, image=null, article=null)])";
        assertEquals(responseString, response.toString());

    }
}