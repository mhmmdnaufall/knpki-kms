package org.knpkid.kms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class QuoteTest {

    private final Quote quote = new Quote();

    @BeforeEach
    void setUp() {
        quote.setId(null);
        quote.setBody(null);
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
    void authorGetterSetter() {
        final var author = new Author();
        author.setName("author");

        quote.setAuthor(author);

        assertEquals(author, quote.getAuthor());
        assertEquals("author", quote.getAuthor().getName());
    }

    @Test
    void adminGetterSetter() {
        final var admin = new Admin();
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
        final var fill = (UnaryOperator<Quote>) quote -> {
            final var admin = new Admin();
            admin.setUsername("admin");

            final var author = new Author();
            author.setName("author");

            quote.setId(1);
            quote.setBody("body");
            quote.setAdmin(admin);
            quote.setAuthor(author);
            return quote;
        };

        final var equalQuote = new Quote();
        fill.apply(quote);
        fill.apply(equalQuote);

        assertEquals(quote, equalQuote);
        assertNotEquals(quote, new Quote());
    }

    @Test
    void testToString() {
        quote.setId(1);
        quote.setBody("body");

        final var quoteString = "Quote("
                    + "id="+quote.getId()+", "
                    + "body="+quote.getBody()+", "
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

        quote.setAuthor(new Author());
        assertEquals(hashCodeCalculate(quote), quote.hashCode());

        quote.setAdmin(new Admin());
        assertEquals(hashCodeCalculate(quote), quote.hashCode());
    }

    private int hashCodeCalculate(Quote quote) {
        final var idHashCode = quote.getId() == null ? 43 : quote.getId().hashCode();
        final var bodyHashCode = quote.getBody() == null ? 43 : quote.getBody().hashCode();
        final var authorHashCode = quote.getAuthor() == null ? 43 : quote.getAuthor().hashCode();
        final var adminHashCode = quote.getAdmin() == null ? 43 : quote.getAdmin().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + bodyHashCode;
        result = result * 59 + authorHashCode;
        result = result * 59 + adminHashCode;

        return result;
    }
}
