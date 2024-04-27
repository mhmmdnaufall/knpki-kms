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
        final var tag = new Tag();
        assertEquals(hashCodeCalculate(tag), tag.hashCode());

        tag.setId("id");
        assertEquals(hashCodeCalculate(tag), tag.hashCode());

        tag.setName("name");
        assertEquals(hashCodeCalculate(tag), tag.hashCode());

        tag.setArticles(List.of(new Article(), new Article()));
        assertEquals(hashCodeCalculate(tag), tag.hashCode());
    }

    @Test
    void testToString() {
        final var articleList = List.of(new Article(), new Article());
        tag.setId("id");
        tag.setName("name");
        tag.setArticles(articleList);

        final var tagString = "Tag(id=id, name=name, articles=[Article(id=null, title=null, createdAt=null, updatedAt=null, coverImage=null, body=null, teaser=null, authors=null, tags=null, imageGallery=null, archive=null, admin=null), Article(id=null, title=null, createdAt=null, updatedAt=null, coverImage=null, body=null, teaser=null, authors=null, tags=null, imageGallery=null, archive=null, admin=null)])";
        assertEquals(tagString, tag.toString());
    }

    private int hashCodeCalculate(Tag tag) {
        final var idHashCode = tag.getId() == null ? 43 : tag.getId().hashCode();
        final var nameHashCode = tag.getName() == null ? 43 : tag.getName().hashCode();
        final var articlesHashCode = tag.getArticles() == null ? 43 : tag.getArticles().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + nameHashCode;
        result = result * 59 + articlesHashCode;

        return result;
    }
}