package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OnlyArticleResponseTest {

    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2024, 2, 29, 17, 20);

    private final OnlyArticleResponse response = new OnlyArticleResponse(
            "id", "title", LOCAL_DATE_TIME, LOCAL_DATE_TIME,
            "body", "teaser", "coverImage".getBytes()
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
    void getCoverImage() {
        assertArrayEquals("coverImage".getBytes(), response.getCoverImage());
    }

    @Test
    void testEquals() {
        final var responseEqual = new OnlyArticleResponse(
                "id", "title", LOCAL_DATE_TIME, LOCAL_DATE_TIME,
                "body", "teaser", "coverImage".getBytes()
        );
        assertEquals(response, responseEqual);

        final var responseNotEqual = new OnlyArticleResponse(
                "notEqual", "notEqual", LOCAL_DATE_TIME, LOCAL_DATE_TIME,
                "notEqual", "notEqual", "notEqual".getBytes()
        );

        assertNotEquals(response, responseNotEqual);
    }

    @Test
    void testHashCode() {
        assertEquals(1718101529, response.hashCode());
    }

    @Test
    void testToString() {
        final var responseString = "OnlyArticleResponse(id=id, title=title, createdAt=" + LOCAL_DATE_TIME + ", updatedAt=" + LOCAL_DATE_TIME + ", body=body, teaser=teaser, coverImage=" + Arrays.toString("coverImage".getBytes()) + ")";
        assertEquals(responseString, response.toString());
    }
}