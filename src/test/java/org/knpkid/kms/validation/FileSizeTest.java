package org.knpkid.kms.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeTest {

    @Test
    void getSize() {
        final var kb = FileSize.KB;
        final var mb = FileSize.MB;
        final var gb = FileSize.GB;

        assertEquals(1024, kb.getSize());
        assertEquals(1_048_576, mb.getSize());
        assertEquals(1_073_741_824, gb.getSize());
    }

    @Test
    void values() {
        assertEquals(3, FileSize.values().length);
    }

    @Test
    void valueOf() {
        final var kb = FileSize.valueOf("KB");
        final var mb = FileSize.valueOf("MB");
        final var gb = FileSize.valueOf("GB");

        assertEquals(FileSize.KB, kb);
        assertEquals(FileSize.MB, mb);
        assertEquals(FileSize.GB, gb);
    }
}