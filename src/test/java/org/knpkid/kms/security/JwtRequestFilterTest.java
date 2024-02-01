package org.knpkid.kms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.service.AdminService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @ParameterizedTest(name = "doFilterIternal condition-{0}")
    @CsvSource({
            "1, false, Bearer, true, true",     "9, true, Bearer, true, true",
            "2, false, Bearer, true, false",    "10, true, Bearer, true, false",
            "3, false, Bearer, false, true",    "11, true, Bearer, false, true",
            "4, false, Bearer, false, false",   "12, true, Bearer, false, false",
            "5, false, false, true, true",      "13, true, false, true, true",
            "6, false, false, true, false",     "14, true, false, true, false",
            "7, false, false, false, true",     "15, true, false, false, true",
            "8, false, false, false, false",    "16, true, false, false, false"
    })
    void doFilterInternal(
            String ignoredIteration,
            boolean isTokenHeaderNull,
            String tokenHeaderPrefixWord,
            boolean isSecContextAuthIsNull,
            boolean isTokenValidate
    ) throws ServletException, IOException {

        final var request = new MockHttpServletRequest();
        if (!isTokenHeaderNull) request.addHeader("Authorization", tokenHeaderPrefixWord + " token");

        final var response = new MockHttpServletResponse();
        final var filterChain = mock(FilterChain.class);

        {
            lenient().when(jwtTokenUtils.getUsernameFromToken(anyString())).thenReturn("admintest");
            lenient().when(adminService.loadUserByUsername("admintest")).thenReturn(new Admin());
            lenient().when(jwtTokenUtils.validateToken(anyString(), any())).thenReturn(isTokenValidate);

            final var securityContext = mock(SecurityContext.class);
            final var authentication = mock(Authentication.class);
            SecurityContextHolder.setContext(securityContext);
            lenient().when(securityContext.getAuthentication()).thenReturn(isSecContextAuthIsNull ? null : authentication);

        }

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}