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
        var articleList = List.of(new Article(), new Article());
        tag.setArticles(articleList);
        assertEquals(articleList, tag.getArticles());
        assertEquals(2, tag.getArticles().size());
    }

    @Test
    void testEquals() {
        var articleList = List.of(new Article(), new Article());
        tag.setId("id");
        tag.setName("name");
        tag.setArticles(articleList);

        var tagEqual = new Tag();
        tagEqual.setId("id");
        tagEqual.setName("name");
        tagEqual.setArticles(articleList);
        assertEquals(tag, tagEqual);

        tag.setId(null);
        tagEqual.setId(null);
        assertEquals(tag, tagEqual);

        tag.setName(null);
        tagEqual.setName(null);
        assertEquals(tag, tagEqual);

        tag.setArticles(null);
        tagEqual.setArticles(null);
        assertEquals(tag, tagEqual);

        var tagNotEqual = new Tag();
        tagNotEqual.setName("unequal");
        assertNotEquals(tag, tagNotEqual);
    }

    @Test
    void canEqual() {
        assertTrue(tag.canEqual(new Tag()));
    }

    @Test
    void testHashCode() {
        var tag = new Tag();
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
        var articleList = List.of(new Article(), new Article());
        tag.setId("id");
        tag.setName("name");
        tag.setArticles(articleList);

        var tagString = "Tag(id=id, name=name, articles=[Article(id=null, title=null, createdAt=null, updatedAt=null, coverImage=null, body=null, teaser=null, authors=null, tags=null, imageGallery=null, archive=null, admin=null), Article(id=null, title=null, createdAt=null, updatedAt=null, coverImage=null, body=null, teaser=null, authors=null, tags=null, imageGallery=null, archive=null, admin=null)])";
        assertEquals(tagString, tag.toString());
    }

    private int hashCodeCalculate(Tag tag) {
        var idHashCode = tag.getId() == null ? 43 : tag.getId().hashCode();
        var nameHashCode = tag.getName() == null ? 43 : tag.getName().hashCode();
        var articlesHashCode = tag.getArticles() == null ? 43 : tag.getArticles().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + nameHashCode;
        result = result * 59 + articlesHashCode;

        return result;
    }
}