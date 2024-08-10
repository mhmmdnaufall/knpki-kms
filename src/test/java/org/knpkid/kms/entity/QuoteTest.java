package org.knpkid.kms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QuoteTest {

    private final Quote quote = new Quote();

    @BeforeEach
    void setUp() {
        quote.setId(null);
        quote.setBody(null);
        quote.setCreatedAt(null);
        quote.setUpdatedAt(null);
        quote.setAuthor(null);
        quote.setAdmin(null);
    }

    @Test
    void idGetterSetter() {
        quote.setId(1);
        assertEquals(1, quote.getId());
    }

    @Test
    void bodyGetterSetter() {
        quote.setBody("body");
        assertEquals("body", quote.getBody());
    }

    @Test
    void createdAtGetterSetter() {
        var now = LocalDateTime.now();
        quote.setCreatedAt(now);
        assertEquals(now, quote.getCreatedAt());
    }

    @Test
    void updatedAtGetterSetter() {
        var now = LocalDateTime.now();
        quote.setUpdatedAt(now);
        assertEquals(now, quote.getUpdatedAt());
    }

    @Test
    void authorGetterSetter() {
        var author = new Author();
        author.setName("author");

        quote.setAuthor(author);

        assertEquals(author, quote.getAuthor());
        assertEquals("author", quote.getAuthor().getName());
    }

    @Test
    void adminGetterSetter() {
        var admin = new Admin();
        admin.setUsername("admin");

        quote.setAdmin(admin);
        assertEquals(admin, quote.getAdmin());
        assertEquals("admin", quote.getAdmin().getUsername());
    }

    @Test
    void canEqual() {
        assertTrue(quote.canEqual(new Quote()));
        assertFalse(quote.canEqual(""));
    }

    @Test
    void testEquals() {
        var quoteEqual = new Quote();
        assertEquals(quote, quoteEqual);

        quote.setId(1);
        quoteEqual.setId(1);
        assertEquals(quote, quoteEqual);

        quote.setAdmin(new Admin());
        quoteEqual.setAdmin(new Admin());
        assertEquals(quote, quoteEqual);

        quote.setAuthor(new Author());
        quoteEqual.setAuthor(new Author());
        assertEquals(quote, quoteEqual);

        quote.setBody("body");
        quoteEqual.setBody("body");
        assertEquals(quote, quoteEqual);

        var now = LocalDateTime.now();
        quote.setCreatedAt(now);
        quoteEqual.setCreatedAt(now);
        assertEquals(quote, quoteEqual);

        quote.setUpdatedAt(now);
        quoteEqual.setUpdatedAt(now);
        assertEquals(quote, quoteEqual);

        assertNotEquals(quote, new Quote());
    }

    @Test
    void testToString() {
        var now = LocalDateTime.now();
        quote.setId(1);
        quote.setBody("body");
        quote.setCreatedAt(now);
        quote.setUpdatedAt(now);

        var quoteString = "Quote("
                    + "id="+quote.getId()+", "
                    + "body="+quote.getBody()+", "
                    + "createdAt="+now+", "
                    + "updatedAt="+now+", "
                    + "author=null, "
                    + "admin=null"
                + ")";

        assertEquals(quoteString, quote.toString());
    }

    @Test
    void testHashCode() {
        assertEquals(hashCodeCalculate(quote), quote.hashCode());

        quote.setId(1);
        assertEquals(hashCodeCalculate(quote), quote.hashCode());

        quote.setBody("body");
        assertEquals(hashCodeCalculate(quote), quote.hashCode());

        var now = LocalDateTime.now();
        quote.setCreatedAt(now);
        assertEquals(hashCodeCalculate(quote), quote.hashCode());

        quote.setUpdatedAt(now);
        assertEquals(hashCodeCalculate(quote), quote.hashCode());

        quote.setAuthor(new Author());
        assertEquals(hashCodeCalculate(quote), quote.hashCode());

        quote.setAdmin(new Admin());
        assertEquals(hashCodeCalculate(quote), quote.hashCode());
    }

    private int hashCodeCalculate(Quote quote) {
        var idHashCode = quote.getId() == null ? 43 : quote.getId().hashCode();
        var bodyHashCode = quote.getBody() == null ? 43 : quote.getBody().hashCode();
        var createdAtHashCode = quote.getCreatedAt() == null ? 43 : quote.getCreatedAt().hashCode();
        var updatedAtHashCode = quote.getUpdatedAt() == null ? 43 : quote.getUpdatedAt().hashCode();
        var authorHashCode = quote.getAuthor() == null ? 43 : quote.getAuthor().hashCode();
        var adminHashCode = quote.getAdmin() == null ? 43 : quote.getAdmin().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + bodyHashCode;
        result = result * 59 + createdAtHashCode;
        result = result * 59 + updatedAtHashCode;
        result = result * 59 + authorHashCode;
        result = result * 59 + adminHashCode;

        return result;
    }
}
