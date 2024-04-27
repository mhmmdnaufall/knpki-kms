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
        AUTHOR.setName("author");

        final var author = new Author();
        author.setName("author");

        assertEquals(AUTHOR, author);
        assertNotEquals(AUTHOR, new Author());
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
        final var idHashCode = AUTHOR.getId() == null ? 43 : AUTHOR.getId().hashCode();
        final var nameHashCode = AUTHOR.getName() == null ? 43 : AUTHOR.getName().hashCode();
        final var articlesHashCode = AUTHOR.getArticles() == null ? 43 : AUTHOR.getArticles().hashCode();
        final var quotesHashCode = AUTHOR.getQuotes() == null ? 43 : AUTHOR.getQuotes().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + nameHashCode;
        result = result * 59 + articlesHashCode;
        result = result * 59 + quotesHashCode;

        return result;
    }

}
