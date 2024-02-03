package org.knpkid.kms;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnpkiKmsApplicationTest {

    @Test
    void app() {
        assertDoesNotThrow(() -> KnpkiKmsApplication.main(new String[]{}));
    }
}