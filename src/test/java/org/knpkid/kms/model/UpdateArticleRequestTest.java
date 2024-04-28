package org.knpkid.kms.model;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateArticleRequestTest {

    private final UpdateArticleRequest request = new UpdateArticleRequest(
            "title", new MockMultipartFile("coverImage.png", "coverImage".getBytes()), "body",
            "teaser",
            Set.of("tag-1", "tag-2"),
            List.of(
                    new MockMultipartFile("image1.png", "image1".getBytes()),
                    new MockMultipartFile("image2.png", "image2".getBytes())
            ),
            Set.of("author", "author2"),
            new MockMultipartFile("file.pdf", "file".getBytes())
    );

    @Test
    void title() {
        assertEquals("title", request.title());
    }

    @Test
    void coverImage() throws IOException {
        assertEquals("coverImage.png", request.coverImage().getName());
        assertArrayEquals("coverImage".getBytes(), request.coverImage().getBytes());
    }

    @Test
    void body() {
        assertEquals("body", request.body());
    }

    @Test
    void teaser() {
        assertEquals("teaser", request.teaser());
    }

    @Test
    void tags() {
        assertEquals(2, request.tags().size());
        assertTrue(request.tags().contains("tag-1"));
        assertTrue(request.tags().contains("tag-2"));
    }

    @Test
    void images() throws IOException {
        assertEquals(2, request.images().size());
        assertArrayEquals("image1".getBytes(), request.images().get(0).getBytes());
        assertArrayEquals("image2".getBytes(), request.images().get(1).getBytes());
        assertEquals("image1.png", request.images().get(0).getName());
        assertEquals("image2.png", request.images().get(1).getName());

    }

    @Test
    void authors() {
        assertEquals(2, request.authors().size());
        assertTrue(request.authors().contains("author"));
        assertTrue(request.authors().contains("author2"));
    }

    @Test
    void archive() throws IOException {
        assertEquals("file.pdf", request.archive().getName());
        assertArrayEquals("file".getBytes(), request.archive().getBytes());
    }
}