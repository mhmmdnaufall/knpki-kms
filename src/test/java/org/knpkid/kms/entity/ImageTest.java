package org.knpkid.kms.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageTest {

    private static final Image IMAGE = new Image();

    @BeforeEach
    void setUp() {
        IMAGE.setId(null);
        IMAGE.setFormat(null);
    }

    @Test
    void idGetterSetter() {
        IMAGE.setId("imageId");
        assertEquals("imageId", IMAGE.getId());
    }

    @Test
    void formatGetterSetter() {
        IMAGE.setFormat(ImageFormat.PNG);
        assertEquals(ImageFormat.PNG, IMAGE.getFormat());
    }

    @Test
    void testEquals() {
        var imageEqual = new Image();
        assertEquals(IMAGE, imageEqual);

        IMAGE.setId("imageId");
        imageEqual.setId("imageId");
        assertEquals(IMAGE, imageEqual);

        var imageNotEqual = new Image();
        imageNotEqual.setId("diffId");
        assertNotEquals(IMAGE, imageNotEqual);

        IMAGE.setFormat(ImageFormat.PNG);
        imageEqual.setFormat(ImageFormat.PNG);
        assertEquals(IMAGE, imageEqual);

        imageNotEqual.setFormat(ImageFormat.JPG);
        assertNotEquals(IMAGE, imageNotEqual);

        IMAGE.setId(null);
        imageEqual.setId(null);
        assertEquals(IMAGE, imageEqual);

        IMAGE.setId("imageId");
        imageEqual.setId("imageId");
        IMAGE.setFormat(null);
        imageEqual.setFormat(null);
        assertEquals(IMAGE, imageEqual);

    }

    @Test
    void canEqual() {
        assertTrue(IMAGE.canEqual(new Image()));
    }

    @Test
    void testHashCode() {
        assertEquals(hashCodeCalculate(), IMAGE.hashCode());

        IMAGE.setId("imageId");
        assertEquals(hashCodeCalculate(), IMAGE.hashCode());

        IMAGE.setFormat(ImageFormat.PNG);
        assertEquals(hashCodeCalculate(), IMAGE.hashCode());
    }

    @Test
    void testToString() {
        IMAGE.setId("imageId");
        IMAGE.setFormat(ImageFormat.PNG);
        assertEquals("imageId.png", IMAGE.toString());
    }


    private int hashCodeCalculate() {
        var idHashCode = IMAGE.getId() == null ? 43 : IMAGE.getId().hashCode();
        var formatHashCode = IMAGE.getFormat() == null ? 43 : IMAGE.getFormat().hashCode();
        return (59 + idHashCode) * 59 + formatHashCode;
    }
}