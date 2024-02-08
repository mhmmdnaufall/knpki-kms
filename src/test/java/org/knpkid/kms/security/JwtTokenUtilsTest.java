package org.knpkid.kms.security;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Admin;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenUtilsTest {

    private final JwtTokenUtils jwtTokenUtils = new JwtTokenUtils(
            "TestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKeyTestJWTSecretKey",
            60_000
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
                Instant.now().plus(60_000, ChronoUnit.MILLIS), ZoneId.systemDefault()
        );
        assertEquals(expectedExpired, expirationLocalDate);
    }

    @Test
    void validateToken() {
        final var admin = getAdmin();
        final var jwtToken = getJwtToken();

        // valid
        var isTokenValid = jwtTokenUtils.validateToken(getJwtToken(), admin);
        assertTrue(isTokenValid);

        // not valid expired token
        final var mockJwtTokenUtils = spy(jwtTokenUtils);
        when(mockJwtTokenUtils.getExpirationDateFromToken(jwtToken))
                .thenReturn(new Date(System.currentTimeMillis() - 10000000000L));
        when(mockJwtTokenUtils.getUsernameFromToken(jwtToken)).thenReturn(admin.getUsername());

        isTokenValid = mockJwtTokenUtils.validateToken(jwtToken, admin);
        assertFalse(isTokenValid);

        // not valid mismatch username
        admin.setUsername("notadmin");
        isTokenValid = jwtTokenUtils.validateToken(getJwtToken(), admin);
        assertFalse(isTokenValid);

        // not valid both expired and not mismatch username
        isTokenValid = mockJwtTokenUtils.validateToken(getJwtToken(), admin);
        assertFalse(isTokenValid);
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