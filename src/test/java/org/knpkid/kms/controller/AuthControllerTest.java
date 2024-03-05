package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.model.TokenResponse;
import org.knpkid.kms.service.AuthService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Test
    void login() {
        final var request = new LoginAdminRequest("username", "password");

        when(authService.getLoginToken(request)).thenReturn("token value");

        final var webResponse = authController.login(request);

        assertEquals(new TokenResponse("token value"), webResponse.data());
        assertNull(webResponse.errors());
        assertNull(webResponse.paging());
    }

    @Test
    void logout() {
        try (final var securityContextHolderMockStatic = mockStatic(SecurityContextHolder.class)) {
            authController.logout();
            securityContextHolderMockStatic.verify(SecurityContextHolder::clearContext);
        }
    }

    @Test
    void getCsrfToken() {
        final var csrfTokenMock = mock(CsrfToken.class);
        final var webResponse = authController.getCsrfToken(csrfTokenMock);
        assertEquals(csrfTokenMock.getToken(), webResponse.data().token());
        assertNull(webResponse.errors());
        assertNull(webResponse.paging());
    }
}