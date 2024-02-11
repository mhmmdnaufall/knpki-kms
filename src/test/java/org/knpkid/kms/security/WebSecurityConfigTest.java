package org.knpkid.kms.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.service.AdminService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSecurityConfigTest {

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @Mock
    private AdminService adminService;

    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private AuthenticationConfiguration authConfig;

    @Mock
    private HttpSecurity httpSecurity;

    @Test
    void authenticationManager() throws Exception {
        webSecurityConfig.authenticationManager(authConfig);
        verify(authConfig).getAuthenticationManager();
    }

    @Test
    void daoAuthenticationProvider() {
        final var daoAuthenticationProvider = webSecurityConfig.daoAuthenticationProvider();
        assertNotNull(daoAuthenticationProvider);
    }

    @Test
    void filterChain() throws Exception {

        {
            when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
            when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
            when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
            when(httpSecurity.exceptionHandling(any())).thenReturn(httpSecurity);
            when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
            when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
            when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
            when(httpSecurity.build()).thenReturn(new DefaultSecurityFilterChain(any()));
        }

        final var securityFilterChain = webSecurityConfig.filterChain(httpSecurity);
        assertNotNull(securityFilterChain);
    }
}