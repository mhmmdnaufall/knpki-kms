package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.service.AuthService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

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
        final var response = new MockHttpServletResponse();
        final var request = new LoginAdminRequest("username", "password");

        when(authService.getLoginToken(request)).thenReturn("token value");

        final var webResponse = authController.login(request, response);

        assertNotNull(response.getCookie("token"));
        assertNotNull(response.getCookie("token").getValue());
        assertEquals("token value", response.getCookie("token").getValue());

        assertEquals("OK", webResponse.data());
        assertNull(webResponse.errors());
    }
}