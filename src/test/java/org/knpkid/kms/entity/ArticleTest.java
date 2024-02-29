package org.knpkid.kms.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    private final Article article = new Article();

    @Test
    void idGetterSetter() {
        article.setId("id");
        assertEquals("id", article.getId());
    }

    @Test
    void titleGetterSetter() {
        article.setTitle("title");
        assertEquals("title", article.getTitle());
    }

    @Test
    void createdAtGetterSetter() {
        final var localDateTimeNow = LocalDateTime.now();
        article.setCreatedAt(localDateTimeNow);
        assertEquals(localDateTimeNow, article.getCreatedAt());
    }

    @Test
    void updatedAtGetterSetter() {
        final var localDateTimeNow = LocalDateTime.now();
        article.setUpdatedAt(localDateTimeNow);
        assertEquals(localDateTimeNow, article.getUpdatedAt());
    }

    @Test
    void coverImageGetterSetter() {
        final var byteArrayCoverImage = "coverImage".getBytes();
        article.setCoverImage(byteArrayCoverImage);
        assertArrayEquals(byteArrayCoverImage, article.getCoverImage());
    }

    @Test
    void bodyGetterSetter() {
        article.setBody("body");
        assertEquals("body", article.getBody());
    }

    @Test
    void teaserGetterSetter() {
        article.setTeaser("teaser");
        assertEquals("teaser", article.getTeaser());
    }

    @Test
    void adminGetterSetter() {
        final var admin = new Admin();
        article.setAdmin(admin);
        assertEquals(admin, article.getAdmin());
    }

    @Test
    void tagsGetterSetter() {
        final var tags = Set.of(new Tag());
        article.setTags(tags);
        assertEquals(tags, article.getTags());
        assertEquals(1, article.getTags().size());
    }

    @Test
    void imagesGetterSetter() {
        final var images = List.of(new ArticleImage(), new ArticleImage());
        article.setImages(images);
        assertEquals(images, article.getImages());
        assertEquals(2, article.getImages().size());
    }

    @Test
    void testEquals() {
        final var localDateTimeNow = LocalDateTime.now();

        article.setId("id");
        article.setTitle("title");
        article.setBody("body");
        article.setTeaser("teaser");
        article.setAdmin(new Admin());
        article.setCoverImage("coverImage".getBytes());
        article.setUpdatedAt(localDateTimeNow);
        article.setCreatedAt(localDateTimeNow);
        article.setImages(List.of(new ArticleImage(), new ArticleImage()));
        article.setTags(Set.of(new Tag()));

        final var articleEqual = new Article();
        articleEqual.setId("id");
        articleEqual.setTitle("title");
        articleEqual.setBody("body");
        articleEqual.setTeaser("teaser");
        articleEqual.setAdmin(new Admin());
        articleEqual.setCoverImage("coverImage".getBytes());
        articleEqual.setUpdatedAt(localDateTimeNow);
        articleEqual.setCreatedAt(localDateTimeNow);
        articleEqual.setImages(List.of(new ArticleImage(), new ArticleImage()));
        articleEqual.setTags(Set.of(new Tag()));

        assertEquals(article, articleEqual);

        final var articleNotEqual = new Article();
        assertNotEquals(article, articleNotEqual);


    }

    @Test
    void canEqual() {
        assertTrue(article.canEqual(new Article()));
        assertFalse(article.canEqual(""));
    }

    @Test
    void testHashCode() {

        article.setId("id");
        article.setTitle("title");
        article.setBody("body");
        article.setTeaser("teaser");
        article.setAdmin(new Admin());
        article.setCoverImage("coverImage".getBytes());
        article.setUpdatedAt(null);
        article.setCreatedAt(null);
        article.setImages(List.of(new ArticleImage(), new ArticleImage()));
        article.setTags(Set.of(new Tag()));

        assertEquals(908857076, article.hashCode());
    }

    @Test
    void testToString() {

        final var localDateTimeNow = LocalDateTime.now();

        article.setId("id");
        article.setTitle("title");
        article.setBody("body");
        article.setTeaser("teaser");
        article.setAdmin(new Admin());
        article.setCoverImage("coverImage".getBytes());
        article.setUpdatedAt(localDateTimeNow);
        article.setCreatedAt(localDateTimeNow);
        article.setImages(List.of(new ArticleImage(), new ArticleImage()));
        article.setTags(Set.of(new Tag()));

        final var articleString = "Article(id=id, title=title, createdAt=" + localDateTimeNow + ", updatedAt=" + localDateTimeNow + ", coverImage=" + Arrays.toString("coverImage".getBytes()) + ", body=body, teaser=teaser, admin=Admin(username=null, password=null, name=null, image=null, articles=null), tags=[Tag(id=null, name=null, articles=null)], images=[ArticleImage(id=null, image=null, article=null), ArticleImage(id=null, image=null, article=null)])";
        assertEquals(articleString, article.toString());

    }
}