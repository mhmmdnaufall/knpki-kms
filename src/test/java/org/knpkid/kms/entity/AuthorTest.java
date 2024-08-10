package org.knpkid.kms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    private static final Author AUTHOR = new Author();

    @BeforeEach
    void setUp() {
        AUTHOR.setId(null);
        AUTHOR.setName(null);
        AUTHOR.setArticles(null);
        AUTHOR.setQuotes(null);
    }

    @Test
    void idGetterSetter() {
        AUTHOR.setId(1);
        assertEquals(1, AUTHOR.getId());
    }

    @Test
    void nameGetterSetter() {
        AUTHOR.setName("author");
        assertEquals("author", AUTHOR.getName());
    }

    @Test
    void articlesGetterSetter() {
        AUTHOR.setArticles(List.of(new Article(), new Article()));
        assertFalse(AUTHOR.getArticles().isEmpty());
        assertEquals(2, AUTHOR.getArticles().size());
    }

    @Test
    void quotesGetterSetter() {
        AUTHOR.setQuotes(List.of(new Quote(), new Quote()));
        assertFalse(AUTHOR.getQuotes().isEmpty());
        assertEquals(2, AUTHOR.getQuotes().size());
    }

    @Test
    void testEquals() {
        var author = new Author();
        assertEquals(AUTHOR, author);

        AUTHOR.setId(1);
        author.setId(1);
        assertEquals(AUTHOR, author);

        AUTHOR.setName("author");
        author.setName("author");
        assertEquals(AUTHOR, author);

        AUTHOR.setQuotes(List.of(new Quote()));
        author.setQuotes(List.of(new Quote()));
        assertEquals(AUTHOR, author);

        AUTHOR.setArticles(List.of(new Article()));
        author.setArticles(List.of(new Article()));
        assertEquals(AUTHOR, author);

        AUTHOR.setId(null);
        author.setId(null);
        assertEquals(AUTHOR, author);

        AUTHOR.setName(null);
        author.setName(null);
        assertEquals(AUTHOR, author);

        AUTHOR.setQuotes(null);
        author.setQuotes(null);
        assertEquals(AUTHOR, author);

        AUTHOR.setArticles(null);
        author.setArticles(null);
        assertEquals(AUTHOR, author);

        var authorUnequal = new Author();
        authorUnequal.setName("unequal");

        assertNotEquals(AUTHOR, authorUnequal);
    }

    @Test
    void canEqual() {
        assertTrue(AUTHOR.canEqual(new Author()));
        assertFalse(AUTHOR.canEqual(""));
    }

    @Test
    void testHashCode() {
        assertEquals(hashCodeCalculate(), AUTHOR.hashCode());

        AUTHOR.setId(384810242);
        assertEquals(hashCodeCalculate(), AUTHOR.hashCode());

        AUTHOR.setName("author");
        assertEquals(hashCodeCalculate(), AUTHOR.hashCode());

        AUTHOR.setArticles(List.of(new Article(), new Article()));
        assertEquals(hashCodeCalculate(), AUTHOR.hashCode());

        AUTHOR.setQuotes(List.of(new Quote(), new Quote()));
        assertEquals(hashCodeCalculate(), AUTHOR.hashCode());
    }

    @Test
    void testToString() {
        AUTHOR.setId(1);
        AUTHOR.setName("author");
        assertEquals("Author(id=1, name=author, articles=null, quotes=null)", AUTHOR.toString());
    }

    private int hashCodeCalculate() {
        var idHashCode = AUTHOR.getId() == null ? 43 : AUTHOR.getId().hashCode();
        var nameHashCode = AUTHOR.getName() == null ? 43 : AUTHOR.getName().hashCode();
        var articlesHashCode = AUTHOR.getArticles() == null ? 43 : AUTHOR.getArticles().hashCode();
        var quotesHashCode = AUTHOR.getQuotes() == null ? 43 : AUTHOR.getQuotes().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + nameHashCode;
        result = result * 59 + articlesHashCode;
        result = result * 59 + quotesHashCode;

        return result;
    }

}
