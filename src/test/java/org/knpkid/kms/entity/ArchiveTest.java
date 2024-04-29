package org.knpkid.kms.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArchiveTest {

    @Test
    void archive() {
        final var uuid = UUID.randomUUID();
        final var archive = new Archive();
        archive.setId(uuid.toString());
        archive.setFormat(ArchiveFormat.PDF);

        assertEquals(uuid.toString(), archive.getId());
        assertEquals(ArchiveFormat.PDF, archive.getFormat());
    }

    @Test
    void testEquals() {
        final var uuid = UUID.randomUUID();

        final var archive1 = new Archive();
        archive1.setId(uuid.toString());
        archive1.setFormat(ArchiveFormat.PDF);

        final var archive2 = new Archive();
        archive2.setId(uuid.toString());
        archive2.setFormat(ArchiveFormat.PDF);

        assertEquals(archive1, archive2);

        archive1.setId(null);
        archive2.setId(null);
        assertEquals(archive1, archive2);

        archive1.setFormat(null);
        archive2.setFormat(null);
        assertEquals(archive1, archive2);

        final var archiveUnequal = new Archive();
        archiveUnequal.setId("unequal");

        assertNotEquals(archive1, archiveUnequal);
    }

    @Test
    void canEqual() {
        final var uuid = UUID.randomUUID();
        final var archive = new Archive();
        archive.setId(uuid.toString());
        archive.setFormat(ArchiveFormat.PDF);

        assertTrue(archive.canEqual(new Archive()));
        assertFalse(archive.canEqual(""));
    }

    @Test
    void testHashCode() {
        final var uuid = UUID.randomUUID();
        final var archive = new Archive();
        assertEquals(hashCodeCalculate(archive), archive.hashCode());

        archive.setId(uuid.toString());
        assertEquals(hashCodeCalculate(archive), archive.hashCode());

        archive.setFormat(ArchiveFormat.PDF);
        assertEquals(hashCodeCalculate(archive), archive.hashCode());
    }

    @Test
    void testToString() {
        final var uuid = UUID.randomUUID();
        final var archive = new Archive();
        archive.setId(uuid.toString());
        archive.setFormat(ArchiveFormat.PDF);

        assertEquals("%s.%s".formatted(archive.getId(), archive.getFormat().name().toLowerCase()), archive.toString());
    }

    private int hashCodeCalculate(Archive archive) {
        final var idHashCode = archive.getId() == null ? 43 : archive.getId().hashCode();
        final var formatHashCode = archive.getFormat() == null ? 43 : archive.getFormat().hashCode();

        var result = 1;
        result = result * 59 + idHashCode;
        result = result * 59 + formatHashCode;

        return result;
    }
}
