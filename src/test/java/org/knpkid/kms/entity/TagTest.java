package org.knpkid.kms.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private final Tag tag = new Tag();

    @Test
    void idGetterSetter() {
        tag.setId("id");
        assertEquals("id", tag.getId());
    }

    @Test
    void nameGetterSetter() {
        tag.setName("name");
        assertEquals("name", tag.getName());
    }

    @Test
    void articlesGetterSetter() {
        final var articleList = List.of(new Article(), new Article());
        tag.setArticles(articleList);
        assertEquals(articleList, tag.getArticles());
        assertEquals(2, tag.getArticles().size());
    }

    @Test
    void testEquals() {
        final var articleList = List.of(new Article(), new Article());
        tag.setId("id");
        tag.setName("name");
        tag.setArticles(articleList);

        final var tagEqual = new Tag();
        tagEqual.setId("id");
        tagEqual.setName("name");
        tagEqual.setArticles(articleList);
        assertEquals(tag, tagEqual);

        final var tagNotEqual = new Tag();
        assertNotEquals(tag, tagNotEqual);
    }

    @Test
    void canEqual() {
        assertTrue(tag.canEqual(new Tag()));
    }

    @Test
    void testHashCode() {
        final var articleList = List.of(new Article(), new Article());
        tag.setId("id");
        tag.setName("name");
        tag.setArticles(articleList);
        assertEquals(-1910584368, tag.hashCode());
        System.out.println(tag.hashCode());
    }

    @Test
    void testToString() {
        final var articleList = List.of(new Article(), new Article());
        tag.setId("id");
        tag.setName("name");
        tag.setArticles(articleList);

        final var tagString = "Tag(id=id, name=name, articles=[Article(id=null, title=null, createdAt=null, updatedAt=null, coverImage=null, body=null, teaser=null, admin=null, tags=null, imageGallery=null), Article(id=null, title=null, createdAt=null, updatedAt=null, coverImage=null, body=null, teaser=null, admin=null, tags=null, imageGallery=null)])";
        assertEquals(tagString, tag.toString());
    }
}