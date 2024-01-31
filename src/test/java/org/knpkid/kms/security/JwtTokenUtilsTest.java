package org.knpkid.kms.security;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Admin;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilsTest {

    private final JwtTokenUtils jwtTokenUtils = new JwtTokenUtils(
            "TestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKey",
            30
    );

    @Test
    void generateToken() {
        final var token = getJwtToken();

        System.out.println(token);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void getUsernameFromToken() {
        final var username = jwtTokenUtils.getUsernameFromToken(getJwtToken());
        assertEquals("admintest", username);
    }

    @Test
    void getExpirationDateFromToken() {
        final var expirationDate = jwtTokenUtils.getExpirationDateFromToken(getJwtToken());
        final var expirationLocalDate = LocalDate.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault());
        final var expectedExpired = LocalDate.ofInstant(
                Instant.now().plus(30, ChronoUnit.DAYS), ZoneId.systemDefault()
        );
        assertEquals(expectedExpired, expirationLocalDate);
    }

    @Test
    void validateToken() {
        final var isTokenTrue = jwtTokenUtils.validateToken(getJwtToken(), getAdmin());
        assertTrue(isTokenTrue);
    }

    @Test
    void validateUnvalidatedToken() {
        final var isTokenTrue = jwtTokenUtils.validateToken(getJwtToken(), new Admin());
        assertFalse(isTokenTrue);
    }

    @Test
    void jwtTokenUtilNoArgsConstructor() {
        final var jwtTokenUtilsNoArgsConstructor = new JwtTokenUtils();
        assertNotSame(jwtTokenUtils, jwtTokenUtilsNoArgsConstructor);
    }

    private Admin getAdmin() {
        final var admin = new Admin();
        admin.setUsername("admintest");
        admin.setPassword("password");
        admin.setName("Admin Test");
        return admin;
    }

    private String getJwtToken() {
        return jwtTokenUtils.generateToken(getAdmin());
    }
}