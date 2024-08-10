package org.knpkid.kms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
        ARTICLE.setId(3401094);
        assertEquals(3401094, ARTICLE.getId());
    }

    @Test
    void titleGetterSetter() {
        ARTICLE.setTitle("title");
        assertEquals("title", ARTICLE.getTitle());
    }

    @Test
    void createdAtGetterSetter() {
        var localDateTimeNow = LocalDateTime.now();
        ARTICLE.setCreatedAt(localDateTimeNow);
        assertEquals(localDateTimeNow, ARTICLE.getCreatedAt());
    }

    @Test
    void updatedAtGetterSetter() {
        var localDateTimeNow = LocalDateTime.now();
        ARTICLE.setUpdatedAt(localDateTimeNow);
        assertEquals(localDateTimeNow, ARTICLE.getUpdatedAt());
    }

    @Test
    void coverImageGetterSetter() {
        var image = new Image();
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
        var admin = new Admin();
        ARTICLE.setAdmin(admin);
        assertEquals(admin, ARTICLE.getAdmin());
    }

    @Test
    void tagsGetterSetter() {
        var tags = Set.of(new Tag());
        ARTICLE.setTags(tags);
        assertEquals(tags, ARTICLE.getTags());
        assertEquals(1, ARTICLE.getTags().size());
    }

    @Test
    void imagesGetterSetter() {
        var images = List.of(new Image(), new Image());
        ARTICLE.setImageGallery(images);
        assertEquals(images, ARTICLE.getImageGallery());
        assertEquals(2, ARTICLE.getImageGallery().size());
    }

    @Test
    void authorsGetterSetter() {
        var author1 = new Author();
        author1.setName("author1");
        author1.setId(1);

        var author2 = new Author();
        author1.setName("author2");
        author1.setId(2);

        ARTICLE.setAuthors(Set.of(author1, author2));
        assertTrue(ARTICLE.getAuthors().contains(author1));
        assertTrue(ARTICLE.getAuthors().contains(author2));
    }

    @Test
    void archiveGetterSetter() {
        var uuid = UUID.randomUUID();

        var archive = new Archive();
        archive.setId(uuid.toString());
        archive.setFormat(ArchiveFormat.PDF);

        ARTICLE.setArchive(archive);

        assertEquals(archive, ARTICLE.getArchive());
    }

    @Test
    void testEquals() {
        var now = LocalDateTime.now();

        var author1 = new Author();
        author1.setId(1);

        var author2 = new Author();
        author2.setId(2);

        var articleEqual = new Article();
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setId(1);
        articleEqual.setId(1);
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setTitle("title");
        articleEqual.setTitle("title");
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setBody("body");
        articleEqual.setBody("body");
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setTeaser("teaser");
        articleEqual.setTeaser("teaser");
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setAdmin(new Admin());
        articleEqual.setAdmin(new Admin());
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setCoverImage(new Image());
        articleEqual.setCoverImage(new Image());
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setCreatedAt(now);
        articleEqual.setCreatedAt(now);
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setUpdatedAt(now);
        articleEqual.setUpdatedAt(now);
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setImageGallery(List.of(new Image()));
        articleEqual.setImageGallery(List.of(new Image()));
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setTags(Set.of(new Tag()));
        articleEqual.setTags(Set.of(new Tag()));
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setAuthors(Set.of(author1, author2));
        articleEqual.setAuthors(Set.of(author1, author2));
        assertEquals(ARTICLE, articleEqual);

        ARTICLE.setArchive(new Archive());
        articleEqual.setArchive(new Archive());
        assertEquals(ARTICLE, articleEqual);

        assertNotEquals(ARTICLE, new Article());

    }

    @Test
    void canEqual() {
        assertTrue(ARTICLE.canEqual(new Article()));
        assertFalse(ARTICLE.canEqual(""));
    }

    @Test
    void testHashCode() {
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        ARTICLE.setId(384810242);
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

        var author1 = new Author();
        author1.setName("author1");
        author1.setId(1);

        var author2 = new Author();
        author1.setName("author2");
        author1.setId(2);

        ARTICLE.setAuthors(Set.of(author1, author2));
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

        var archive = new Archive();
        archive.setId(UUID.randomUUID().toString());
        archive.setFormat(ArchiveFormat.PDF);

        ARTICLE.setArchive(archive);
        assertEquals(hashCodeCalculate(), ARTICLE.hashCode());

    }

    @Test
    void testToString() {

        var now = LocalDateTime.now();

        var image = new Image();
        image.setId("articleId");
        image.setFormat(ImageFormat.PNG);

        ARTICLE.setId(48120402);
        ARTICLE.setTitle("title");
        ARTICLE.setBody("body");
        ARTICLE.setTeaser("teaser");
        ARTICLE.setAdmin(new Admin());
        ARTICLE.setCoverImage(image);
        ARTICLE.setUpdatedAt(now);
        ARTICLE.setCreatedAt(now);
        ARTICLE.setImageGallery(List.of(image));
        ARTICLE.setTags(Set.of(new Tag()));

        var articleString = "Article("
                + "id=48120402, title=title, createdAt=" + now
                + ", updatedAt=" + now + ", coverImage=" + image + ", body=body, teaser=teaser, "
                + "authors=null, "
                + "tags=[Tag(id=null, name=null, articles=null)], imageGallery=" + List.of(image) + ", archive=null, "
                + "admin=Admin(username=null, password=null, name=null, image=null, articles=null, quotes=null))";
        assertEquals(articleString, ARTICLE.toString());

    }

    private int hashCodeCalculate() {
        var idHashCode = ARTICLE.getId() == null ? 43 : ARTICLE.getId().hashCode();
        var titleHashCode = ARTICLE.getTitle() == null ? 43 : ARTICLE.getTitle().hashCode();
        var createdAtHashCode = ARTICLE.getCreatedAt() == null ? 43 : ARTICLE.getCreatedAt().hashCode();
        var updatedAtHashCode = ARTICLE.getUpdatedAt() == null ? 43 : ARTICLE.getUpdatedAt().hashCode();
        var coverImageHashCode = ARTICLE.getCoverImage() == null ? 43 : ARTICLE.getCoverImage().hashCode();
        var bodyHashCode = ARTICLE.getBody() == null ? 43 : ARTICLE.getBody().hashCode();
        var teaserHashCode = ARTICLE.getTeaser() == null ? 43 : ARTICLE.getTeaser().hashCode();
        var authorsHashCode = ARTICLE.getAuthors() == null ? 43 : ARTICLE.getAuthors().hashCode();
        var tagsHashCode = ARTICLE.getTags() == null ? 43 : ARTICLE.getTags().hashCode();
        var imageGalleryHashCode = ARTICLE.getImageGallery() == null ? 43 : ARTICLE.getImageGallery().hashCode();
        var archiveHashCode = ARTICLE.getArchive() == null ? 43 : ARTICLE.getArchive().hashCode();
        var adminHashCode = ARTICLE.getAdmin() == null ? 43 : ARTICLE.getAdmin().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + titleHashCode;
        result = result * 59 + createdAtHashCode;
        result = result * 59 + updatedAtHashCode;
        result = result * 59 + coverImageHashCode;
        result = result * 59 + bodyHashCode;
        result = result * 59 + teaserHashCode;
        result = result * 59 + authorsHashCode;
        result = result * 59 + tagsHashCode;
        result = result * 59 + imageGalleryHashCode;
        result = result * 59 + archiveHashCode;
        result = result * 59 + adminHashCode;

        return result;
    }
}