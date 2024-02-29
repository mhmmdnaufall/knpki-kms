package org.knpkid.kms.entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ArticleImageTest {

    private final ArticleImage articleImage = new ArticleImage();

    @Test
    void idGetterSetter() {
        articleImage.setId("id");
        assertEquals("id", articleImage.getId());
    }

    @Test
    void imageGetterSetter() {
        articleImage.setImage("image".getBytes());
        assertArrayEquals("image".getBytes(), articleImage.getImage());
    }

    @Test
    void articleGetterSetter() {
        final var article = new Article();
        articleImage.setArticle(article);
        assertEquals(article, articleImage.getArticle());
    }

    @Test
    void testEquals() {
        articleImage.setId("id");
        articleImage.setArticle(new Article());
        articleImage.setImage("image".getBytes());

        final var articleImageEqual = new ArticleImage();
        articleImageEqual.setId("id");
        articleImageEqual.setArticle(new Article());
        articleImageEqual.setImage("image".getBytes());

        assertEquals(articleImage, articleImageEqual);

        final var articleImageNotEqual = new ArticleImage();
        assertNotEquals(articleImage, articleImageNotEqual);
    }

    @Test
    void canEqual() {
        assertTrue(articleImage.canEqual(new ArticleImage()));
    }

    @Test
    void testHashCode() {
        articleImage.setId("id");
        articleImage.setArticle(new Article());
        articleImage.setImage("image".getBytes());

        assertEquals(-908402744, articleImage.hashCode());
    }

    @Test
    void testToString() {
        articleImage.setId("id");
        articleImage.setArticle(new Article());
        articleImage.setImage("image".getBytes());

        final var articleImageString = "ArticleImage(id=id, image="+ Arrays.toString("image".getBytes()) +", article=Article(id=null, title=null, createdAt=null, updatedAt=null, coverImage=null, body=null, teaser=null, admin=null, tags=null, images=null))";
        assertEquals(articleImageString, articleImage.toString());
    }
}