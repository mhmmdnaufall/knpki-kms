package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.security.JwtTokenUtils;
import org.knpkid.kms.service.AdminService;
import org.knpkid.kms.service.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AdminService adminService;

    @Mock
    private ValidationService validationService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void getLoginToken() {

        var request = new LoginAdminRequest("admin", "password");

        // success getLoginToken
        doNothing().when(validationService).validate(any());
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(adminService.loadUserByUsername(anyString())).thenReturn(new Admin());
        when(jwtTokenUtils.generateToken(any())).thenReturn("token");

        var loginToken = authService.getLoginToken(request);
        assertNotNull(loginToken);
        assertEquals("token", loginToken);

        // failed getLoginToken
        when(authenticationManager.authenticate(any())).thenThrow(RuntimeException.class);
        var exception = assertThrows(ResponseStatusException.class, () -> authService.getLoginToken(request));
        assertEquals("username or password wrong", exception.getReason());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}