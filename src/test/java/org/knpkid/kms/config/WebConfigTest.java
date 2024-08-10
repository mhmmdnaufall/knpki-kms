package org.knpkid.kms.config;

import org.junit.jupiter.api.Test;
import org.knpkid.kms.resolver.AdminArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class WebConfigTest {

    private final AdminArgumentResolver adminArgumentResolver = new AdminArgumentResolver();

    private final WebConfig webConfig = new WebConfig(adminArgumentResolver);

    @Test
    void addArgumentResolvers() {
        var listSpy = spy(new ArrayList<HandlerMethodArgumentResolver>());

        webConfig.addArgumentResolvers(listSpy);

        verify(listSpy).add(adminArgumentResolver);
    }

}