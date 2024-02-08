package org.knpkid.kms.config;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.resolver.AdminArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class WebConfigTest {

    private final AdminArgumentResolver adminArgumentResolver = new AdminArgumentResolver();

    private final WebConfig webConfig = new WebConfig(adminArgumentResolver);

    @Test
    void addArgumentResolvers() {
        final var listSpy = spy(new ArrayList<HandlerMethodArgumentResolver>());

        webConfig.addArgumentResolvers(listSpy);

        verify(listSpy).add(adminArgumentResolver);
    }

    @Test
    void addCorsMappings() {
        final var corsRegistryMock = mock(CorsRegistry.class);
        final var corsRegistrationMock = mock(CorsRegistration.class);

        {
            when(corsRegistryMock.addMapping("/**")).thenReturn(corsRegistrationMock);
            when(corsRegistrationMock.allowedOriginPatterns("https://*.knpkid.org/")).thenReturn(corsRegistrationMock);
            when(corsRegistrationMock.allowedMethods(
                    HttpMethod.GET.name(),
                    HttpMethod.POST.name(),
                    HttpMethod.PUT.name(),
                    HttpMethod.DELETE.name()
            )).thenReturn(corsRegistrationMock);
            when(corsRegistrationMock.allowedHeaders(
                    HttpHeaders.AUTHORIZATION,
                    HttpHeaders.CONTENT_TYPE,
                    HttpHeaders.ACCEPT,
                    "X-CSRF-TOKEN")
            ).thenReturn(corsRegistrationMock);

        }

        webConfig.addCorsMappings(corsRegistryMock);

        {
            verify(corsRegistryMock).addMapping("/**");
            verify(corsRegistrationMock).allowedOriginPatterns("https://*.knpkid.org/");
            verify(corsRegistrationMock)
                    .allowedMethods(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.DELETE.name()
                    );
            verify(corsRegistrationMock)
                    .allowedHeaders(
                            HttpHeaders.AUTHORIZATION,
                            HttpHeaders.CONTENT_TYPE,
                            HttpHeaders.ACCEPT,
                            "X-CSRF-TOKEN"
                    );
        }

    }
}