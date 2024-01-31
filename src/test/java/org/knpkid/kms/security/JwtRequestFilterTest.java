package org.knpkid.kms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.service.AdminService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {
    
    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;
    
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    
    @Mock
    private AdminService adminService;

    @Test
    void doFilterInternal() throws ServletException, IOException {
        final var request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");

        final var response = new MockHttpServletResponse();
        final var filterChain = mock(FilterChain.class);

        {
            when(jwtTokenUtils.getUsernameFromToken(anyString())).thenReturn("admintest");
            when(adminService.loadUserByUsername("admintest")).thenReturn(new Admin());
            when(jwtTokenUtils.validateToken(anyString(), any())).thenReturn(true);
        }

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}