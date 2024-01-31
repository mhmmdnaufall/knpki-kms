package org.knpkid.kms.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> login(@RequestBody LoginAdminRequest request, HttpServletResponse response) {
        final var jwtToken = authService.getLoginToken(request);
        final var cookie = new Cookie("token", jwtToken);
        response.addCookie(cookie);

        return new WebResponse<>("OK", null);
    }

}
