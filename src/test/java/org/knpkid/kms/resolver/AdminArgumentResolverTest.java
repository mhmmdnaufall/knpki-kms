package org.knpkid.kms.resolver;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.entity.Admin;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminArgumentResolverTest {

    private final AdminArgumentResolver adminArgumentResolver = new AdminArgumentResolver();

    @Test
    void supportsParameter() {
        final var methodParameter = mock(MethodParameter.class);

        // true
        when(methodParameter.getParameterType()).thenAnswer(invocation -> Admin.class);
        var isAdminClass = adminArgumentResolver.supportsParameter(methodParameter);
        assertTrue(isAdminClass);

        //false
        when(methodParameter.getParameterType()).thenAnswer(invocation -> String.class);
        isAdminClass = adminArgumentResolver.supportsParameter(methodParameter);
        assertFalse(isAdminClass);


    }

    @Test
    void resolveArgument() {
        final var admin = new Admin();

        final var securityContext = mock(SecurityContext.class);
        final var authentication = mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(admin);

        final var parameter = mock(MethodParameter.class);
        final var mavContainer = mock(ModelAndViewContainer.class);
        final var webRequest = mock(NativeWebRequest.class);
        final var binderFactory = mock(WebDataBinderFactory.class);

        final var adminResult = adminArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        assertSame(admin,adminResult);
    }
}