package org.knpkid.kms.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileFormatTest {

    @Test
    void values() {
        assertEquals(4, FileFormat.values().length);
    }

    @Test
    void valueOf() {
        var jpg = FileFormat.valueOf("JPG");
        var jpeg = FileFormat.valueOf("JPEG");
        var png = FileFormat.valueOf("PNG");
        var pdf = FileFormat.valueOf("PDF");

        assertEquals(FileFormat.JPG, jpg);
        assertEquals(FileFormat.JPEG, jpeg);
        assertEquals(FileFormat.PNG, png);
        assertEquals(FileFormat.PDF, pdf);
    }
}