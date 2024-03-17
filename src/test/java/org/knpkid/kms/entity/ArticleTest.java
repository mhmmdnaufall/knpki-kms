package org.knpkid.kms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    private final Article ARTICLE = new Article();

    @BeforeEach
    void setUp() {
        ARTICLE.setId(null);
        ARTICLE.setTitle(null);
        ARTICLE.setCreatedAt(null);
        ARTICLE.setUpdatedAt(null);
        ARTICLE.setBody(null);
        ARTICLE.setTeaser(null);
        ARTICLE.setCoverImage(null);
        ARTICLE.setAdmin(null);
        ARTICLE.setTags(null);
        ARTICLE.setImageGallery(null);
    }

    @Test
    void idGetterSetter() {
        ARTICLE.setId("id");
        assertEquals("id", ARTICLE.getId());
    }

    @Test
    void titleGetterSetter() {
        ARTICLE.setTitle("title");
        assertEquals("title", ARTICLE.getTitle());
    }

    @Test
    void createdAtGetterSetter() {
        final var localDateTimeNow = LocalDateTime.now();
        ARTICLE.setCreatedAt(localDateTimeNow);
        assertEquals(localDateTimeNow, ARTICLE.getCreatedAt());
    }

    @Test
    void updatedAtGetterSetter() {
        final var localDateTimeNow = LocalDateTime.now();
        ARTICLE.setUpdatedAt(localDateTimeNow);
        assertEquals(localDateTimeNow, ARTICLE.getUpdatedAt());
    }

    @Test
    void coverImageGetterSetter() {
        final var image = new Image();
        ARTICLE.setCoverImage(image);
        assertEquals(image, ARTICLE.getCoverImage());
    }

    @Test
    void bodyGetterSetter() {
        ARTICLE.setBody("body");
        assertEquals("body", ARTICLE.getBody());
    }

    @Test
    void teaserGetterSetter() {
        ARTICLE.setTeaser("teaser");
        assertEquals("teaser", ARTICLE.getTeaser());
    }

    @Test
    void adminGetterSetter() {
        final var admin = new Admin();
        ARTICLE.setAdmin(admin);
        assertEquals(admin, ARTICLE.getAdmin());
    }

    @Test
    void tagsGetterSetter() {
        final var tags = Set.of(new Tag());
        ARTICLE.setTags(tags);
        assertEquals(tags, ARTICLE.getTags());
        assertEquals(1, ARTICLE.getTags().size());
    }

    @Test
    void imagesGetterSetter() {
        final var images = List.of(new Image(), new Image());
        ARTICLE.setImageGallery(images);
        assertEquals(images, ARTICLE.getImageGallery());
        assertEquals(2, ARTICLE.getImageGallery().size());
    }

    @Test
    void testEquals() {
        final var now = LocalDateTime.now();
        ARTICLE.setId("id");
        ARTICLE.setTitle("title");
        ARTICLE.setBody("body");
        ARTICLE.setTeaser("teaser");
        ARTICLE.setAdmin(new Admin());
        ARTICLE.setCoverImage(new Image());
        ARTICLE.setUpdatedAt(now);
        ARTICLE.setCreatedAt(now);
        ARTICLE.setImageGallery(List.of(new Image()));
        ARTICLE.setTags(Set.of(new Tag()));

        final var articleEqual = new Article();
        articleEqual.setId("id");
        articleEqual.setTitle("title");
        articleEqual.setBody("body");
        articleEqual.setTeaser("teaser");
        articleEqual.setAdmin(new Admin());
        articleEqual.setCoverImage(new Image());
        articleEqual.setUpdatedAt(now);
        articleEqual.setCreatedAt(now);
        articleEqual.setImageGallery(List.of(new Image()));
        articleEqual.setTags(Set.of(new Tag()));

        assertEquals(ARTICLE, articleEqual);

        final var articleNotEqual = new Article();
        assertNotEquals(ARTICLE, articleNotEqual);


    }

    @Test
    void canEqual() {
        assertTrue(ARTICLE.canEqual(new Article()));
        assertFalse(ARTICLE.canEqual(""));
    }

    @Test
    void testHashCode() {
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setId("id");
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setTitle("title");
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setBody("body");
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setTeaser("teaser");
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setAdmin(new Admin());
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setCoverImage(new Image());
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setCreatedAt(LocalDateTime.now());
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setUpdatedAt(LocalDateTime.now());
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setImageGallery(List.of(new Image()));
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setTags(Set.of(new Tag()));
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

    }

    @Test
    void testToString() {

        final var now = LocalDateTime.now();

        final var image = new Image();
        image.setId("articleId");
        image.setFormat(ImageFormat.PNG);

        ARTICLE.setId("id");
        ARTICLE.setTitle("title");
        ARTICLE.setBody("body");
        ARTICLE.setTeaser("teaser");
        ARTICLE.setAdmin(new Admin());
        ARTICLE.setCoverImage(image);
        ARTICLE.setUpdatedAt(now);
        ARTICLE.setCreatedAt(now);
        ARTICLE.setImageGallery(List.of(image));
        ARTICLE.setTags(Set.of(new Tag()));

        final var articleString = "Article("
                + "id=id, title=title, createdAt=" + now
                + ", updatedAt=" + now + ", coverImage=" + image + ", body=body, teaser=teaser, "
                + "admin=Admin(username=null, password=null, name=null, image=null, articles=null), "
                + "tags=[Tag(id=null, name=null, articles=null)], imageGallery=" + List.of(image) + ")";
        assertEquals(articleString, ARTICLE.toString());

    }

    private int hashCodeCalculate() {
        final var idHashCode = ARTICLE.getId() == null ? 43 : ARTICLE.getId().hashCode();
        final var titleHashCode = ARTICLE.getTitle() == null ? 43 : ARTICLE.getTitle().hashCode();
        final var createdAtHashCode = ARTICLE.getCreatedAt() == null ? 43 : ARTICLE.getCreatedAt().hashCode();
        final var updatedAtHashCode = ARTICLE.getUpdatedAt() == null ? 43 : ARTICLE.getUpdatedAt().hashCode();
        final var coverImageHashCode = ARTICLE.getCoverImage() == null ? 43 : ARTICLE.getCoverImage().hashCode();
        final var bodyHashCode = ARTICLE.getBody() == null ? 43 : ARTICLE.getBody().hashCode();
        final var teaserHashCode = ARTICLE.getTeaser() == null ? 43 : ARTICLE.getTeaser().hashCode();
        final var adminHashCode = ARTICLE.getAdmin() == null ? 43 : ARTICLE.getAdmin().hashCode();
        final var tagsHashCode = ARTICLE.getTags() == null ? 43 : ARTICLE.getTags().hashCode();
        final var imageGalleryHashCode = ARTICLE.getImageGallery() == null ? 43 : ARTICLE.getImageGallery().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + titleHashCode;
        result = result * 59 + createdAtHashCode;
        result = result * 59 + updatedAtHashCode;
        result = result * 59 + coverImageHashCode;
        result = result * 59 + bodyHashCode;
        result = result * 59 + teaserHashCode;
        result = result * 59 + adminHashCode;
        result = result * 59 + tagsHashCode;
        result = result * 59 + imageGalleryHashCode;

        return result;
    }
}