package org.knpkid.kms;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class KnpkiKmsApplicationTest {

    private final KnpkiKmsApplication app = new KnpkiKmsApplication();

    @Test
    void passwordEncoder() {
        final var passwordEncoder = app.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void app() {
        assertDoesNotThrow(() -> KnpkiKmsApplication.main(new String[]{}));
    }
}