package org.knpkid.kms.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeTest {

    @Test
    void getSize() {
        var kb = FileSize.KB;
        var mb = FileSize.MB;
        var gb = FileSize.GB;

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
        var kb = FileSize.valueOf("KB");
        var mb = FileSize.valueOf("MB");
        var gb = FileSize.valueOf("GB");

        assertEquals(FileSize.KB, kb);
        assertEquals(FileSize.MB, mb);
        assertEquals(FileSize.GB, gb);
    }
}